/*
 *   @author Mihaela Verman
 *  
 *  Copyright 2015 University of Zurich
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
package com.signalcollect.dcop.custom

import com.signalcollect.ExecutionConfiguration
import com.signalcollect.GraphBuilder
import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.dcop.evaluation.Execution
import com.signalcollect.dcop.modules.ArgmaxADecisionRule
import com.signalcollect.dcop.modules.IntAlgorithm
import com.signalcollect.dcop.modules.MemoryLessTargetFunction
import com.signalcollect.dcop.modules.NashEquilibriumConvergence
import com.signalcollect.dcop.modules.ParallelRandomAdjustmentSchedule
import com.signalcollect.dcop.modules.SignalCollectAlgorithmBridge

object SchedulingEventAlgorithm extends IntAlgorithm
  with EventState
  with EventUtility
  with ParallelRandomAdjustmentSchedule
  with ArgmaxADecisionRule
  with NashEquilibriumConvergence
  with MemoryLessTargetFunction
  with SignalCollectAlgorithmBridge
  with Execution {
  def changeProbability = 0.7
  def algorithmName = "simpleEvent"
}

object SchedulingSlotAlgorithm extends IntAlgorithm
  with SlotState
  with SlotUtility
  with ParallelRandomAdjustmentSchedule
  with ArgmaxADecisionRule
  with NashEquilibriumConvergence
  with MemoryLessTargetFunction
  with SignalCollectAlgorithmBridge
  with Execution {
  def changeProbability = 0.7
  def algorithmName = "simpleSlot"
}

/**
 *  Two events with no participants in common and two slots.
 *  Events must have even IDs and slots odd IDs
 */
object SchedulingSampleRun extends App {

  println("Start")
  val ev0 = SchedulingEventAlgorithm.createVertex(0, 1, List(1, 3).toSet)
  val ev2 = SchedulingEventAlgorithm.createVertex(2, 1, List(1, 3).toSet)
  val sl1 = SchedulingSlotAlgorithm.createVertex(1, 0, List(0, 2).toSet)
  val sl3 = SchedulingSlotAlgorithm.createVertex(3, 0, List(0, 2).toSet)

  val es01 = SchedulingEventAlgorithm.createEdge(targetId = 1)
  val es03 = SchedulingEventAlgorithm.createEdge(targetId = 3)
  val es21 = SchedulingEventAlgorithm.createEdge(targetId = 1)
  val es23 = SchedulingEventAlgorithm.createEdge(targetId = 3)

  val se10 = SchedulingSlotAlgorithm.createEdge(targetId = 0)
  val se12 = SchedulingSlotAlgorithm.createEdge(targetId = 2)
  val se30 = SchedulingSlotAlgorithm.createEdge(targetId = 0)
  val se32 = SchedulingSlotAlgorithm.createEdge(targetId = 2)

  println("Start graph building")
  val g = GraphBuilder.withConsole(true).build
  println("Start adding vertices and edges")
  g.addVertex(ev0)
  g.addVertex(ev2)
  g.addVertex(sl1)
  g.addVertex(sl3)
  g.addEdge(0, es01)
  g.addEdge(0, es03)
  g.addEdge(1, es21)
  g.addEdge(1, es23)
  g.addEdge(2, se10)
  g.addEdge(2, se12)
  g.addEdge(3, se30)
  g.addEdge(3, se32)

  println("End graph building")
  g.execute(ExecutionConfiguration.withExecutionMode(ExecutionMode.OptimizedAsynchronous))
  println(ev0.state)
  println(ev2.state)
  println(sl1.state)
  println(sl3.state)
  println("Shutting down")
  g.shutdown
  println("bye")
}

