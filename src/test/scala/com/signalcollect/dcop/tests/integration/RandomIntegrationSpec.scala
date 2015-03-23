/*
 *  @author Philip Stutz
 *  @author Mihaela Verman
 *  
 *  Copyright 2013 University of Zurich
 *      
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *         http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */

package com.signalcollect.dcop.tests.integration

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import org.scalatest.prop.Checkers
import scala.math.Ordering.Boolean

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.dcop.algorithms._
import com.signalcollect.dcop.evaluation._
import com.signalcollect.dcop.graph._
import com.signalcollect.dcop.modules.IntAlgorithm
import com.signalcollect.nodeprovisioning.slurm.LocalHost

class RandomGraphEvalSpec extends FlatSpec with ShouldMatchers with Checkers with TestTools {

  "VertexColoringAlgorithms" should "converge in a randgraph in less than 1 minute" in {
    check(
      (execModePar: Int, algorithmNumber: Int, aggregation: Boolean, full: Boolean) => {
        runId += 1

        println("RUNID: " + runId)

        var results = List[Map[String, String]]()

        class ResultList extends Function1[Map[String, String], Unit]
          with Serializable {
          def apply(data: Map[String, String]) = {
            results = data :: results
          }
        }

        /*********/
        val evalName = "testEvaluation"
        var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = 1, executionHost = localHost).addResultHandler(new ResultList) //.addResultHandler(mySql)
        /*********/

        val absorbingAlgorithms: List[IntAlgorithm with Execution] = List(
          new DsaA(0.95),
          new DsaB(0.95))

        val nonAbsorbingAlgorithms: List[IntAlgorithm with Execution] = List(
          new DsaA(0.95),
          new DsaB(0.95))

        val em = executionModes((Math.abs(execModePar % executionModes.size)))

        val algorithmInitialNumber = Math.abs(algorithmNumber % (absorbingAlgorithms.size + nonAbsorbingAlgorithms.size))

        val (myAlgorithm, isAbsorbing) = if (algorithmInitialNumber < absorbingAlgorithms.size) {
          (absorbingAlgorithms(algorithmInitialNumber), true)
        } else {
          (nonAbsorbingAlgorithms(algorithmInitialNumber - absorbingAlgorithms.size), false)
        }

        val myGraph = new RandomGraphReader(myAlgorithm, 1000, 3, 5, 0)
        val myAggregationInterval = if (aggregation) { if (em == ExecutionMode.Synchronous) 1 else 100 } else 0
        val myFullHistory = false

        evaluation = evaluation.addEvaluationRun(myAlgorithm.DcopAlgorithmRun(
          graphInstantiator = myGraph,
          maxUtility = myGraph.maxUtility,
          domainSize = 5,
          executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(5000), //1000000),
          runNumber = runId,
          aggregationInterval = myAggregationInterval,
          fullHistoryStats = myFullHistory,
          revision = "-1",
          evaluationDescription = evalName).runAlgorithm)

        println("assertion" + runId)
        //assert(false, "fake")

        evaluation.execute

        println("The results")
        assert(results.size == 1, "more results")
        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")

          //Can't be converged without being in a NE.
          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing, mustConverge = false)
        }

        true
      },
      minSuccessful(1))
  }

  "VertexColoringAlgorithms" should "converge in a grid in less than 1 minute" in {
    check(
      (execModePar: Int, width: Int, colors: Int, algorithmNumber: Int, aggregation: Boolean, full: Boolean) => {
        runId += 1

        println("RUNID: " + runId)

        var results = List[Map[String, String]]()

        class ResultList extends Function1[Map[String, String], Unit]
          with Serializable {

          def apply(data: Map[String, String]) = {
            results = data :: results
          }
        }

        /*********/
        val evalName = "testEvaluation"
        var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = 1, executionHost = localHost).addResultHandler(new ResultList) //.addResultHandler(mySql)
        /*********/

        val absorbingAlgorithms: List[IntAlgorithm with Execution] = List(
          new DsaA(0.9),
          new DsaB(0.9),
          new Jsfpi(0.9))

        val nonAbsorbingAlgorithms: List[IntAlgorithm with Execution] = List(
          new Dsan(0.9, 1000, 2))

        val numberOfColors = colors % 6 + 4 //between 4 and 9
        val gridWidth = width % 900
        val em = executionModes(Math.abs(execModePar % executionModes.size))

        val algorithmInitialNumber = Math.abs(algorithmNumber % (absorbingAlgorithms.size + nonAbsorbingAlgorithms.size))

        val (myAlgorithm, isAbsorbing) = if (algorithmInitialNumber < absorbingAlgorithms.size) {
          (absorbingAlgorithms(algorithmInitialNumber), true)
        } else {
          (nonAbsorbingAlgorithms(algorithmInitialNumber - absorbingAlgorithms.size), false)
        }
        val myGrid = new GridInstantiator(myAlgorithm, gridWidth, domain = (0 until numberOfColors).toSet)
        val myAggregationInterval = if (aggregation) { if (em == ExecutionMode.Synchronous) 1 else 100 } else 0
        val myFullHistory = false

        evaluation = evaluation.addEvaluationRun(myAlgorithm.DcopAlgorithmRun(
          graphInstantiator = myGrid,
          maxUtility = myGrid.maxUtility,
          domainSize = numberOfColors,
          executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(10000), //1000000),
          runNumber = runId,
          aggregationInterval = myAggregationInterval,
          fullHistoryStats = myFullHistory,
          revision = "-1",
          evaluationDescription = evalName).runAlgorithm)

        //        assert(false, "fake")

        evaluation.execute

        println("The results")
        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")

          //Can't be converged without being in a NE.
          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing, mustConverge = false)

        }

        true
      },
      minSuccessful(1))
  }

}



