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

import com.signalcollect.dcop.modules.SimpleState
import com.signalcollect.dcop.modules.IntAlgorithm

/**
 * State definition for Room and TimeSlot agent. For now, just a simple state. 
 */
trait SlotState extends SimpleState {

}

trait SlotUtility extends IntAlgorithm {

  def computeUtility(c: State) = {
    // A Slot should only be neighbour with Events.
    val neighboringEvents = c.neighborActions.filter(_._1 % 2 == 0)

    neighboringEvents.map(x => computeEventSlotUtility(c, x)).sum
  }

  def computeEventSlotUtility(c: State, event: (AgentId, Action)): Double = {
   //The slot for the event must be the same as the event for the slot or different
   val slotId = c.agentId
   val eventOfSlot = c.centralVariableValue
   val eventId = event._1
   val slotOfEvent = event._2
   
   if ((slotOfEvent == slotId && eventId == eventOfSlot) || (slotOfEvent != slotId && eventId != eventOfSlot)) {
     1.0
   } else {
     0.0
   } 
  }
}
