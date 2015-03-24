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

class WrmiEvalSpec extends FlatSpec with ShouldMatchers with Checkers with TestTools {

  "Wrmi" should "converge in a 1x1 grid in async mode with global termination condition in less than 5 seconds" in {
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
        val algorithms = List(new Wrmi(1.0, 0.1), new Wrmi(1.0, 0.2), new Wrmi(1.0, 0.4), new Wrmi(1.0, 0.6), new Wrmi(1.0, 0.8), new Wrmi(1.0, 0.9), new Wrmi(1.0, 1.0))

        val myAggregationInterval = 100
        val myFullHistory = false

        for (myAlgorithm <- algorithms) {
          val myGrid = new GridInstantiator(myAlgorithm, gridWidth, domain = (0 until numberOfColors).toSet)
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
        }
        evaluation.execute

        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val algo = res.getOrElse("optimizer", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")
          
          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing = true, mustConverge = true)
          
//          assert(!(isNe != "true" && terminationReason == "Converged"), s"Computation did not converged but not in NE in run $runId for: $em ${algo.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
//          assert(terminationReason == "Converged", s"Computation did not converge in run $runId for: $em ${algo.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
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
        val algorithms = List(new Wrmi(1.0, 0.1), new Wrmi(1.0, 0.2), new Wrmi(1.0, 0.4), new Wrmi(1.0, 0.6), new Wrmi(1.0, 0.8), new Wrmi(1.0, 0.9), new Wrmi(1.0, 1.0))

        val myAggregationInterval = 100
        val myFullHistory = false

        for (myAlgorithm <- algorithms) {
          val myGrid = new GridInstantiator(myAlgorithm, gridWidth, domain = (0 until numberOfColors).toSet)
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
        }
        evaluation.execute

        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val algo = res.getOrElse("optimizer", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")

          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing = true, mustConverge = true)
          //assert(Boolean.equiv(isNe == "true", terminationReason == "Converged"), s"Termination reason $terminationReason, NE $isNe in run $runId for: $em ${algo.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
          // assert(terminationReason == "Converged", s"Computation did not converge in run $runId for: $em ${algo.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
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

        val numberOfColors = 11
        val gridWidth = 10
        val em = ExecutionMode.Synchronous
        val myAlgorithm = new Wrmi(0.1, 0.9)
        val myGrid = new GridInstantiator(myAlgorithm, gridWidth, domain = (0 until numberOfColors).toSet)
        val myAggregationInterval = 1
        val myFullHistory = true

        evaluation = evaluation.addEvaluationRun(myAlgorithm.DcopAlgorithmRun(
          graphInstantiator = myGrid,
          maxUtility = myGrid.maxUtility,
          domainSize = numberOfColors,
          executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(5000),
          runNumber = runId,
          aggregationInterval = 0, //myAggregationInterval,
          fullHistoryStats = myFullHistory,
          revision = "-1",
          evaluationDescription = evalName).runAlgorithm)

        evaluation.execute

        for (res <- results) {
          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
          val isNe = res.getOrElse("isNe", "NotDetected")
          val isOptimal = res.getOrElse("isOptimal", "NotDetected")

          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing = true, mustConverge = false)
          //          assert(Boolean.equiv(isNe == "true", terminationReason == "Converged"), s"Termination reason $terminationReason, NE $isNe in run $runId for: $em ${myAlgorithm.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
          //          assert(!(isOptimal == "true")||(isNe == "true"), s"It is optimal but not in a NEin run $runId for: $em ${myAlgorithm.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
          //          assert(!(isNe != "true" && terminationReason == "Converged"), s"Computation did converge but not in NE in run $runId for: $em ${myAlgorithm.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
          //assert(terminationReason == "Converged", s"Computation did not converge in run $runId for: $em ${myAlgorithm.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
        }

        true
      },
      minSuccessful(10))
  }
//
//  it should "converge synchronous in a grid in less than 1 minute" in {
//    check(
//      (width: Int, colors: Int, degPar: Double, rho: Double, aggregation: Boolean, full: Boolean) => {
//        runId += 1
//
//        var results = List[Map[String, String]]()
//
//        class ResultList extends Function1[Map[String, String], Unit]
//          with Serializable {
//
//          def apply(data: Map[String, String]) = {
//            results = data :: results
//          }
//        }
//
//        /*********/
//        val evalName = "testEvaluation"
//        var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = 1, executionHost = localHost).addResultHandler(new ResultList) //.addResultHandler(mySql)
//        /*********/
//
//        assert(rho <= 1 && rho >= 0 && degPar <= 1 && degPar >= 0)
//
//        val numberOfColors = colors % 5 + 4 //between 4 and 8
//        val gridWidth = width % 100
//        val em = ExecutionMode.Synchronous
//        val myAlgorithm = new Wrmi(degPar, rho)
//        val myGrid = new GridInstantiator(myAlgorithm, gridWidth, domain = (0 until numberOfColors).toSet)
//        val myAggregationInterval = if (aggregation) { if (em == ExecutionMode.Synchronous) 1 else 100 } else 0
//        val myFullHistory = false
//
//        evaluation = evaluation.addEvaluationRun(myAlgorithm.DcopAlgorithmRun(
//          graphInstantiator = myGrid,
//          maxUtility = myGrid.maxUtility,
//          domainSize = numberOfColors,
//          executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(60000), //1000000),
//          runNumber = runId,
//          aggregationInterval = myAggregationInterval,
//          fullHistoryStats = myFullHistory,
//          revision = "-1",
//          evaluationDescription = evalName).runAlgorithm)
//
//        evaluation.execute
//
//        println("The results")
//        for (res <- results) {
//          val terminationReason = res.getOrElse("terminationReason", "NotDetected")
//          val isNe = res.getOrElse("isNe", "NotDetected")
//          val isOptimal = res.getOrElse("isOptimal", "NotDetected")
//
//          checkAssertions(runId, terminationReason, isNe, isOptimal, isAbsorbing = true, mustConverge = true)
//          //          assert(Boolean.equiv(isNe == "true", terminationReason == "Converged"), s"Termination reason $terminationReason, NE $isNe in run $runId for: $em ${myAlgorithm.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
//          //          assert(!(isOptimal == "true")||(isNe == "true"), s"It is optimal but not in a NE in run $runId for: $em ${myAlgorithm.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
//          //          assert(!(isNe != "true" && terminationReason == "Converged"), s"Computation did not converged but not in NE in run $runId for: $em ${myAlgorithm.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
//          //assert(terminationReason == "Converged", s"Computation did not converge in run $runId for: $em ${myAlgorithm.toString}, GRID(width = $gridWidth, colors = $numberOfColors), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
//        }
//
//        true
//      },
//      minSuccessful(10))
//  }

}



