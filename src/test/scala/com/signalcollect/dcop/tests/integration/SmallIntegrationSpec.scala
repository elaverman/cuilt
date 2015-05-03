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

class GridEvalSpec extends FlatSpec with ShouldMatchers with Checkers with TestTools {

  "DsaA" should "converge in a 1x1 grid in async mode with global termination condition in less than 5 seconds" in {
    check(
      (execModePar: Int) => {
        runId += 1

        var results = List[Map[String, String]]()

        class ResultList extends Function1[Map[String, String], Unit]
          with Serializable {

          def apply(data: Map[String, String]) = {
            results = data :: results
          }
        }

        /*********/
        val evalName = "miniTestEvaluation"
        var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = 1, executionHost = localHost).addResultHandler(new ResultList)
        /*********/

        val numberOfColors = 4
        val gridWidth = 1
        val em = ExecutionMode.PureAsynchronous
        val myAlgorithm = new DsaA(0.7)
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

        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")

          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing = true, mustConverge = true)
        }

        true
      },
      minSuccessful(10))
  }

  it should "converge in an overconstrained grid in async mode in less than 5 seconds" in {
    check(
      (execModePar: Int) => {
        runId += 1

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

        val numberOfColors = 1
        val gridWidth = 5
        val em = ExecutionMode.OptimizedAsynchronous
        val myAlgorithm = new DsaB(1.0)
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

        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")

          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing = true, mustConverge = true)
        }

        true
      },
      minSuccessful(10))
  }

  it should "converge in a 3x3 grid in Sync mode with high inertia condition in less than 5 seconds" in {
    check(
      (execModePar: Int) => {
        runId += 1

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
        val gridWidth = 3
        val em = ExecutionMode.Synchronous
        val myAlgorithm = new DsaA(0.2)
        val myGrid = new GridInstantiator(myAlgorithm, gridWidth, domain = (0 until numberOfColors).toSet)
        val myAggregationInterval = 1
        val myFullHistory = true

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

        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")

          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing = true, mustConverge = true)
        }

        true
      },
      minSuccessful(10))
  }

  "Dsa" should "converge in a 10 vertex graph in async mode with global termination condition in less than 5 seconds" in {
    check(
      (execModePar: Int) => {
        runId += 1

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
        val myAlgorithm = new DsaA(0.7)
        val myGraph = new RandomGraphReader(myAlgorithm, 10, 3, 3, 0)
        val myAggregationInterval = 100
        val myFullHistory = false

        evaluation = evaluation.addEvaluationRun(myAlgorithm.DcopAlgorithmRun(
          graphInstantiator = myGraph,
          maxUtility = myGraph.maxUtility,
          domainSize = numberOfColors,
          executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(5000),
          runNumber = runId,
          aggregationInterval = myAggregationInterval,
          fullHistoryStats = myFullHistory,
          revision = "-1",
          evaluationDescription = evalName).runAlgorithm)

        evaluation.execute

        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")

          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing = true, mustConverge = true)
        }

        true
      },
      minSuccessful(10))
  }

  "Dsa" should "converge in a 100 graph in async mode in less than 5 seconds" in {
    check(
      (execModePar: Int) => {
        runId += 1

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

        val em = ExecutionMode.OptimizedAsynchronous
        val myAlgorithm = new DsaB(1.0)
        val myGraph = new RandomGraphReader(myAlgorithm, 10, 3, 3, 0)
        val myAggregationInterval = 10
        val myFullHistory = true

        evaluation = evaluation.addEvaluationRun(myAlgorithm.DcopAlgorithmRun(
          graphInstantiator = myGraph,
          maxUtility = myGraph.maxUtility,
          domainSize = 3,
          executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(5000),
          runNumber = runId,
          aggregationInterval = myAggregationInterval,
          fullHistoryStats = myFullHistory,
          revision = "-1",
          evaluationDescription = evalName).runAlgorithm)

        evaluation.execute

        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")

          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing = true, mustConverge = true)
        }

        true
      },
      minSuccessful(100))
  }

  "Dsa" should "converge in a randomgraph in Sync mode with high inertia condition in less than 5 seconds" in {
    check(
      (execModePar: Int) => {
        runId += 1

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

        val em = ExecutionMode.Synchronous
        val myAlgorithm = new DsaA(0.2)
        val myGraph = new RandomGraphReader(myAlgorithm, 10, 3, 3, 0)
        val myAggregationInterval = 1
        val myFullHistory = true

        evaluation = evaluation.addEvaluationRun(myAlgorithm.DcopAlgorithmRun(
          graphInstantiator = myGraph,
          maxUtility = myGraph.maxUtility,
          domainSize = 3,
          executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(5000),
          runNumber = runId,
          aggregationInterval = myAggregationInterval,
          fullHistoryStats = myFullHistory,
          revision = "-1",
          evaluationDescription = evalName).runAlgorithm)

        evaluation.execute

        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")

          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing = true, mustConverge = true)
        }

        true
      },
      minSuccessful(10))
  }

}



