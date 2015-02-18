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

import scala.util.Random
import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import org.scalatest.prop.Checkers
import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import com.signalcollect._
import com.signalcollect.dcop.graph._
import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.dcop.evaluation._
import com.signalcollect.dcop.algorithms.Dsa
import com.signalcollect.dcop.modules.IntAlgorithm
import com.signalcollect.nodeprovisioning.torque.LocalHost
import java.util.ArrayList

class EvalSpec extends FlatSpec with ShouldMatchers with Checkers {

  var runId = 0
  val executionModes = List(ExecutionMode.OptimizedAsynchronous, ExecutionMode.PureAsynchronous, ExecutionMode.Synchronous)

  //    lazy val smallWidth = Gen.chooseNum(1, 10)//.map(Width(_))
  //  implicit def arbSmallWidth[Int] = Arbitrary(smallWidth)
  implicit lazy val arbInt = Arbitrary[Int](Gen.chooseNum(0, 200))

  "Dsa" should "converge in a 2x2 grid in async mode with global termination condition in less than 5 seconds" in {
    check(
      (execModePar: Int) => {
        runId += 1

        def zeroInitialized(domain: Set[Int]) = 0
        val debug = false
        val localHost = new LocalHost

        var results = List[Map[String, String]]()

        class ResultList extends Function1[Map[String, String], Unit]
          with Serializable {

          def apply(data: Map[String, String]) = {
            results = data :: results
          }
        }

        /*********/
        val evalName = "miniTestEvaluation"
        var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = 1, executionHost = localHost).addResultHandler(new ResultList) //.addResultHandler(mySql)
        /*********/

        val numberOfColors = 4
        val gridWidth = 1
        val em = ExecutionMode.PureAsynchronous
        val myAlgorithm = new Dsa(0.7)
        val myGrid = new GridInstantiator(myAlgorithm, gridWidth, domain = (0 until numberOfColors).toSet)
        val myAggregationInterval = 100
        val myFullHistory = false
        
        evaluation = evaluation.addEvaluationRun(myAlgorithm.DcopAlgorithmRun(
          graphInstantiator = myGrid,
          maxUtility = myGrid.maxUtility,
          domainSize = numberOfColors,
          executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(5000), 
          runNumber = runId,
          aggregationInterval = myAggregationInterval, 
          fullHistoryStats = myFullHistory,
          revision = "-1",
          evaluationDescription = evalName).runAlgorithm)

        evaluation.execute

        println("The results")
        for (res <- results) {
          assert(res.getOrElse("terminationReason", "NotDetected") == "Converged", s"Computation did not converge in run $runId for: $em ${myAlgorithm.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
        }

        true
      },
      minSuccessful(1))
  }
  
  
  "VertexColoringAlgorithms" should "converge in a grid in less than 1 minute" in {
    check(
      (execModePar: Int, width: Int, colors: Int, algorithmNumber: Int, aggregation: Boolean, full: Boolean) => {
        runId += 1

        def zeroInitialized(domain: Set[Int]) = 0
        val debug = false
        val localHost = new LocalHost

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

        val simpleAlgorithms: List[IntAlgorithm with Execution] = List(
          new Dsa(0.7))

        val numberOfColors = 9//colors % 6 + 4 //between 4 and 9
        val gridWidth = 3//width % 10
        val em = executionModes((Math.abs(execModePar % executionModes.size)))
        val myAlgorithm = simpleAlgorithms((Math.abs(algorithmNumber % simpleAlgorithms.size)))
        val myGrid = new GridInstantiator(myAlgorithm, gridWidth, domain = (0 until numberOfColors).toSet)
        val myAggregationInterval = if (aggregation) {if (em == ExecutionMode.Synchronous) 1 else 100} else 0
        val myFullHistory = false
        
        evaluation = evaluation.addEvaluationRun(myAlgorithm.DcopAlgorithmRun(
          graphInstantiator = myGrid,
          maxUtility = myGrid.maxUtility,
          domainSize = numberOfColors,
          executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(5000), //1000000),
          runNumber = runId,
          aggregationInterval = myAggregationInterval, //,
          fullHistoryStats = myFullHistory,
          revision = "-1",
          evaluationDescription = evalName).runAlgorithm)

        evaluation.execute

        println("The results")
        for (res <- results) {
          assert(res.getOrElse("terminationReason", "NotDetected") == "Converged", s"Computation did not converge in run $runId for: $em ${myAlgorithm.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
        }

        true
      },
      minSuccessful(10))
  }

}



