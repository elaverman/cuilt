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

class SmallSpec extends FlatSpec with ShouldMatchers with Checkers {

  var runId = 0
  val executionModes = List(ExecutionMode.OptimizedAsynchronous, /*ExecutionMode.PureAsynchronous,*/ ExecutionMode.Synchronous)

  //    lazy val smallWidth = Gen.chooseNum(1, 10)//.map(Width(_))
  //  implicit def arbSmallWidth[Int] = Arbitrary(smallWidth)
  implicit lazy val arbInt = Arbitrary[Int](Gen.chooseNum(0, 200))

  "VertexColoringAlgorithm" should "correctly assign colors to a 2 vertex graph" in {
    check(
      (execModePar: Int) => {
        runId += 1
        val g = GraphBuilder.build
        try {
          val myAlgo = VertexColoringAlgorithm
          val v1 = myAlgo.createVertex(0, 0, (0 to 2).toSet)
          val v2 = myAlgo.createVertex(1, 0, (0 to 2).toSet)
          val e1 = myAlgo.createEdge(targetId = 1)
          val e2 = myAlgo.createEdge(targetId = 0)

          g.addVertex(v1)
          g.addVertex(v2)
          g.addEdge(0, e1)
          g.addEdge(1, e2)
          val execMode = executionModes((Math.abs(execModePar % executionModes.size)))
          g.execute(ExecutionConfiguration.withExecutionMode(execMode))
          assert(v1.state.centralVariableValue != v2.state.centralVariableValue, s"Vertex ${v1.state} and vertex ${v2.state} have a color collision in test run $runId, with execution mode $execMode.")
          true
        } catch {
          case t: Throwable =>
            t.printStackTrace
            true
        } finally {
          g.shutdown
        }
      },
      minSuccessful(10))
  }

  it should "correctly assign colors to a 4 vertex grid" in {
    check(
      (execModePar: Int) => {
        runId += 1
        var res = false
        val g = GraphBuilder.build
        try {
          val myAlgo = VertexColoringAlgorithm

          val domain = (0 until 4).toSet
          def randomFromDomain = domain.toSeq(Random.nextInt(domain.size))

          val v = new Array[Vertex[myAlgo.AgentId, myAlgo.State, Any, Any]](4)
          for (i <- (0 to 3)) {
            v(i) = myAlgo.createVertex(i, randomFromDomain, domain)
          }
          for (i <- (0 to 3)) {
            g.addVertex(v(i))
          }
          for (i <- (0 to 3)) {
            for (j <- (0 to 3)) {
              if (i != j) {
                g.addEdge(i, myAlgo.createEdge(targetId = j))
              }
            }
          }
          val execMode = executionModes((Math.abs(execModePar % executionModes.size)))
          g.execute(ExecutionConfiguration.withExecutionMode(execMode))
          for (i <- (0 to 3)) {
            for (j <- (0 to 3)) {
              if (i != j) {
                assert(v(i).state.centralVariableValue != v(j).state.centralVariableValue, s"Vertex ${v(i).state} and vertex ${v(j).state} have a color collision in test run $runId, with execution mode $execMode.")
              }
            }
          }
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
      minSuccessful(10))
  }

  it should "correctly assign colors to a relaxed random grid" in {
    check(
      (execModePar: Int, width: Int) => {
        runId += 1
        var res = false
        val myAlgo = VertexColoringAlgorithm
        val gb = new GridBuilder(myAlgo, width, (0 to 8).toSet)
        val g = gb.build()
        try {

          val execMode = executionModes((Math.abs(execModePar % executionModes.size)))
          g.execute(ExecutionConfiguration.withExecutionMode(execMode))

          def stateOfVertex(id: Int) = g.forVertexWithId[Vertex[myAlgo.AgentId, myAlgo.State, Any, Any], myAlgo.State](id, x => x.state)

          for (i <- (0 until width * width)) {
            for (j <- gb.computeNeighbours(i)) {
              val stateOfVi = stateOfVertex(i)
              val stateOfVj = stateOfVertex(j)
              assert(stateOfVi.centralVariableValue != stateOfVj.centralVariableValue, s"Vertex ${stateOfVi} and vertex ${stateOfVj} have a color collision in test run $runId, with execution mode $execMode.")
            }
          }
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
      minSuccessful(10))
  }

}



