/*
 *  @author Mihaela Verman
 *  
 *  Copyright 2016 University of Zurich
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

/**
 *  Two events with no participants in common and two slots.
 *  Events must have even IDs and slots odd IDs
 */
object SchedulingRun extends App {

  println("Start")
  println("Start graph building")
  val g = new ScheduleGraphReader(
    eventAlgo = SchedulingEventAlgorithm,
    slotAlgo = SchedulingSlotAlgorithm,
    eventsNumber = 4587, 
    timeSlots = 25, 
    rooms = 153, 
    commonPeopleFile = "inputScheduling/lecture_lecture_commstudno_commprofno.csv",
    lectureSizeFile = "inputScheduling/lecture_studno_profno.csv",
    roomsFile = "inputScheduling/rooms.csv",
    minOccupationRate = 0.03).
    build(GraphBuilder.withConsole(false))
  println("Executing")
  g.execute(ExecutionConfiguration.withExecutionMode(ExecutionMode.OptimizedAsynchronous))

  g.foreachVertex(x => println(x))

  println("Shutting down")
  g.shutdown

  println("bye")
}

