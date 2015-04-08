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
import com.signalcollect.dcop.algorithms._
import com.signalcollect.interfaces.ModularAggregationOperation
import com.signalcollect.dcop._
import com.signalcollect.dcop.modules

class SmallSpec extends FlatSpec with ShouldMatchers with Checkers with TestTools {

  val myAlgo = new Wrmi(0.7, 0.7)

  //  "A vertexColoringAlgorithm" should "correctly assign colors to a 2 vertex graph" in {
  //    check(
  //      (execModePar: Int, algorithmPar: Int) => {
  //        runId += 1
  //        val g = GraphBuilder.build
  //        try {
  //
  //          val v1 = myAlgo.createVertex(0, 0, (0 to 2).toSet)
  //          val v2 = myAlgo.createVertex(1, 0, (0 to 2).toSet)
  //          val e1 = myAlgo.createEdge(targetId = 1)
  //          val e2 = myAlgo.createEdge(targetId = 0)
  //
  //          g.addVertex(v1)
  //          g.addVertex(v2)
  //          g.addEdge(0, e1)
  //          g.addEdge(1, e2)
  //          val execMode = executionModes((Math.abs(execModePar % executionModes.size)))
  //          g.execute(ExecutionConfiguration.withExecutionMode(execMode))
  //          assert(v1.state.centralVariableValue != v2.state.centralVariableValue, s"Vertex ${v1.state} and vertex ${v2.state} have a color collision in test run $runId, with execution mode $execMode.")
  //          true
  //        } catch {
  //          case t: Throwable =>
  //            t.printStackTrace
  //            true
  //        } finally {
  //          g.shutdown
  //        }
  //      },
  //      minSuccessful(minRuns))
  //  }
  //
  //  it should "correctly assign colors to a 4 vertex grid" in {
  //    check(
  //      (execModePar: Int) => {
  //        runId += 1
  //        var res = false
  //        val g = GraphBuilder.build
  //        try {
  //          val domain = (0 until 4).toSet
  //          def randomFromDomain = domain.toSeq(Random.nextInt(domain.size))
  //
  //          val v = new Array[Vertex[myAlgo.AgentId, myAlgo.State, Any, Any]](4)
  //          for (i <- (0 to 3)) {
  //            v(i) = myAlgo.createVertex(i, randomFromDomain, domain)
  //          }
  //          for (i <- (0 to 3)) {
  //            g.addVertex(v(i))
  //          }
  //          for (i <- (0 to 3)) {
  //            for (j <- (0 to 3)) {
  //              if (i != j) {
  //                g.addEdge(i, myAlgo.createEdge(targetId = j))
  //              }
  //            }
  //          }
  //          val execMode = executionModes((Math.abs(execModePar % executionModes.size)))
  //          g.execute(ExecutionConfiguration.withExecutionMode(execMode))
  //          for (i <- (0 to 3)) {
  //            for (j <- (0 to 3)) {
  //              if (i != j) {
  //                assert(v(i).state.centralVariableValue != v(j).state.centralVariableValue, s"Vertex ${v(i).state} and vertex ${v(j).state} have a color collision in test run $runId, with execution mode $execMode.")
  //              }
  //            }
  //          }
  //          res = true
  //        } catch {
  //          case t: Throwable =>
  //            t.printStackTrace
  //            res = false
  //        } finally {
  //          g.shutdown
  //        }
  //        res
  //      },
  //      minSuccessful(minRuns))
  //  }

  "Wrmi" should "correctly assign colors to a relaxed random grid" in {
    check(
      (execModePar: Int /*,width: Int*/ ) => {
        val width = 4
        runId += 1
        var res = false
        val gb = new GridInstantiator(myAlgo, width, (0 to 8).toSet)
        val g = gb.build()
        try {

          val execMode = ExecutionMode.Synchronous//executionModes((Math.abs(execModePar % executionModes.size)))

          val conflictsHistory = collection.mutable.Map.empty[Int, Long]
          val localOptimaHistory = collection.mutable.Map.empty[Int, Long]

          val executionConfig = ExecutionConfiguration.withExecutionMode(execMode).withTimeLimit(5000)

          val stats = g.execute(executionConfig)
          
          

          def stateOfVertex(id: Int) = g.forVertexWithId[Vertex[myAlgo.AgentId, myAlgo.State, Any, Any], myAlgo.State](id, x => x.state)

          val isNe = g.mapReduce[myAlgo.DcopVertex, Boolean](
            { v => { myAlgo.isInLocalOptimum(v.state) } },
            { case (t1, t2) => t1 && t2 },
            true)

          val nonNe = g.mapReduce[myAlgo.DcopVertex, String](
            { v => { if (!myAlgo.isInLocalOptimum(v.state)) v.state.toString + " " + myAlgo.computeExpectedUtilities(v.state).toString + "\n" else "" } },
            { case (t1, t2) => t1 + t2 },
            "")

          val conflictCount = g.mapReduce[myAlgo.DcopVertex, Long](
            { v => v.state.computeExpectedNumberOfConflicts },
            { case (t1, t2) => t1 + t2 },
            0)

          val terminationReason = stats.executionStatistics.terminationReason.toString
          val isOptimal = conflictCount == 0

          println("After execution of Grid" + (width * width).toString)

          //assert(stats.executionStatistics.terminationReason.toString == "Converged", "Not converged within time limit")

          //          for (i <- 0 until width) {
          //            for (j <- 0 until width) {
          //              print(stateOfVertex(i * width + j).centralVariableValue + " ")
          //            }
          //            println
          //          }
          //          println

          def info(c: myAlgo.State): String = {
            val expectedUtilities: Map[myAlgo.Action, Double] = myAlgo.computeExpectedUtilities(c)
            val normFactor = expectedUtilities.values.sum
            val selectionProb = Random.nextDouble

            var string = "INFO[" + expectedUtilities + " " + normFactor + " " + selectionProb + " "

            var partialSum: Double = 0.0
            for (action <- expectedUtilities.keys) {
              partialSum += expectedUtilities(action)
              if (selectionProb * normFactor <= partialSum) {
                string = string + action.toString + "]"
                return string
              }
            }
            string
          }

          for (i <- (0 until width * width)) {
            for (j <- gb.computeNeighbours(i)) {
              val stateOfVi = stateOfVertex(i)
              val stateOfVj = stateOfVertex(j)
              assert(stateOfVi.centralVariableValue != stateOfVj.centralVariableValue, s"Vertex ${stateOfVi} and vertex ${stateOfVj} have a color collision in test run $runId, with execution mode $execMode. \n ${info(stateOfVi)}, ${info(stateOfVj)}")
            }
          }
          checkAssertions(runId, terminationReason, isNe.toString, isOptimal.toString, isAbsorbing = true, mustConverge = true, extraInfo = nonNe)

          res = true

        } catch {
          case t: Throwable =>
            t.printStackTrace
            res = false
        } finally {
          g.shutdown
        }
        res
      },
      minSuccessful(100))
  }

}



