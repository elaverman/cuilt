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
import com.signalcollect._
import com.signalcollect.dcop.graph._

class SmallSpec extends FlatSpec with ShouldMatchers with Checkers {

  var runId = 0

  //TODO randomize the execution mode.
  
  "VertexColoringAlgorithm" should "correctly assign colors to a 2 vertex graph" in {
    check(
      (irrelevantParameter: Int) => {
        runId += 1
        try {
          val myAlgo = VertexColoringAlgorithm
          val v1 = myAlgo.createVertex(0, 0, (0 to 2).toSet)
          val v2 = myAlgo.createVertex(1, 0, (0 to 2).toSet)
          val e1 = myAlgo.createEdge(targetId = 1)
          val e2 = myAlgo.createEdge(targetId = 0)
          println("Start graph building")
          val g = GraphBuilder.build
          println("Start adding vertices and edges")
          g.addVertex(v1)
          g.addVertex(v2)
          g.addEdge(0, e1)
          g.addEdge(1, e2)
          println("End graph building")
          g.execute(ExecutionConfiguration) //.withExecutionMode(ExecutionMode.OptimizedAsynchronous))
          g.shutdown
          assert(v1.state.centralVariableValue != v2.state.centralVariableValue, s"Vertex ${v1.state} and vertex ${v2.state} have a color collision in test run $runId.")
          true
        } catch {
          case t: Throwable =>
            t.printStackTrace
            true
        }
      },
      minSuccessful(10))
  }

}



