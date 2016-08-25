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

import com.signalcollect.dcop.modules.StateModule
import com.signalcollect.dcop.modules.IntAlgorithm

/**
 * State definitions for Event agent.
 * Differs from SimpleState because it needs the set of participants.
 *
 * TODO: Add more complex states (memory etc.).
 */
trait StateWithParticipants extends StateModule {
  type State <: StateWithParticipantsInterface

  trait StateWithParticipantsInterface extends StateInterface {
    // Number of participants in common with another event. 
    def commonParticipants: Map[Int, (Int, Int)]
  }
}

trait EventState extends StateWithParticipants {

  type State = EventStateImplementation

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {
    val commonParticipants = extraInfo match {
      case Some(commonParticipantsInfo: scala.collection.mutable.Map[Int, (Int, Int)]) => commonParticipantsInfo
      case None => scala.collection.mutable.Map.empty[Int, (Int, Int)]
    }
    EventStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      commonParticipants = commonParticipants.toMap)
  }

  case class EventStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    commonParticipants: Map[Int, (Int, Int)]) extends StateWithParticipantsInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
  }
}

trait EventUtility extends IntAlgorithm with StateWithParticipants {

  def computeUtility(c: State) = {
    val neighboringEvents = c.neighborActions.filter(_._1 % 2 == 0)
    val neighboringSlots = c.neighborActions.filter(_._1 % 2 == 1)

    neighboringSlots.map(x => computeSlotUtility(c, x)).sum +
      neighboringEvents.map(x => computeEventUtility(c, x)).sum
  }

  def computeSlotUtility(c: State, slot: (AgentId, Action)): Double = {
    //The slot for the event must be the same as the event for the slot or different
    val eventId = c.agentId
    val slotOfEvent = c.centralVariableValue
    val slotId = slot._1
    val eventOfSlot = slot._2

    if ((slotOfEvent == slotId && eventId == eventOfSlot) || (slotOfEvent != slotId && eventId != eventOfSlot)) {
      1.0
    } else if (slotOfEvent == slotId && eventOfSlot == 0) {
      0.5
    } else {
      0.0
    }
  }

  def computeEventUtility(c: State, event: (AgentId, Action)): Double = {
    // If the events happen during the same time slot and they have common participants
    val participants = c.commonParticipants.getOrElse(event._1, (0, 0))
    if (isSameTime(c.centralVariableValue, event._2) && participants != (0, 0)) {
      val professors = participants._1
      val students = participants._2
      // TODO: Change into hard constraint for professors.
      -professors - students
    } else {
      0.0
    }
  }

  // Slot Id is (Room*100+TimeSlot)*2+1
  def isSameTime(thisSlot: Int, neighborSlot: Int): Boolean = {
    (thisSlot - 1) / 2 % 100 == (neighborSlot - 1) / 2 % 100
  }
}
