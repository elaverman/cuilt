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
import com.signalcollect.dcop.modules.StateWithMemory
import com.signalcollect.dcop.modules.StateWithExtendedMemory

/**
 * State definitions for Event agent.
 * Differs from SimpleState because it needs the set of participants.
 *
 * States are very similar to SimpleState, SimpleMemoryState, SimpleNumberOfCollectsState,
 * ExtendedMemoryState, but they also extend the StateWithParticipants trait.
 */

trait StateWithParticipants extends StateModule with ScheduleState {
  type State <: StateWithParticipantsInterface

  trait StateWithParticipantsInterface extends StateInterface {
    // Number of participants in common with another event. 
    def commonParticipants: Map[Int, (Int, Int)]
  }
}

trait EventSimpleState extends StateWithParticipants {

  type State = EventSimpleStateImplementation

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {
    val commonParticipants = extraInfo match {
      case Some(commonParticipantsInfo: scala.collection.mutable.Map[Int, (Int, Int)]) => commonParticipantsInfo
      case None => scala.collection.mutable.Map.empty[Int, (Int, Int)]
    }
    EventSimpleStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      commonParticipants = commonParticipants.toMap)
  }

  case class EventSimpleStateImplementation(
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

trait EventSimpleMemoryState extends StateWithMemory with StateWithParticipants {
  type State = EventSimpleMemoryStateImplementation

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {
    val commonParticipants = extraInfo match {
      case Some(commonParticipantsInfo: scala.collection.mutable.Map[Int, (Int, Int)]) => commonParticipantsInfo
      case None => scala.collection.mutable.Map.empty[Int, (Int, Int)]
    }
    EventSimpleMemoryStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      memory = Map.empty[Action, Double].withDefaultValue(0.0),
      numberOfCollects = 0,
      commonParticipants = commonParticipants.toMap)
  }

  case class EventSimpleMemoryStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    memory: Map[Action, UtilityType],
    numberOfCollects: Long, //TODO: rename to numberOfUpdates and check
    commonParticipants: Map[Int, (Int, Int)]) extends StateWithMemoryInterface with StateWithParticipantsInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(memory = newMemory, numberOfCollects = this.numberOfCollects + 1).asInstanceOf[this.type]
    }

    override def toString = {
      s"EventSimpleMemoryStateImplementation: agentId $agentId, value $centralVariableValue, memory $memory, neighbors $neighborActions, collects $numberOfCollects"
    }
  }
}

trait EventSimpleNumberOfCollectsState extends StateWithMemory with StateWithParticipants {
  type State = EventSimpleNumberOfCollectsStateImplementation

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {
    val commonParticipants = extraInfo match {
      case Some(commonParticipantsInfo: scala.collection.mutable.Map[Int, (Int, Int)]) => commonParticipantsInfo
      case None => scala.collection.mutable.Map.empty[Int, (Int, Int)]
    }
    EventSimpleNumberOfCollectsStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      memory = Map.empty[Action, Double].withDefaultValue(0.0),
      numberOfCollects = 0,
      commonParticipants = commonParticipants.toMap)
  }

  case class EventSimpleNumberOfCollectsStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    memory: Map[Action, UtilityType],
    numberOfCollects: Long, //TODO: rename to numberOfUpdates and check
    commonParticipants: Map[Int, (Int, Int)]) extends StateWithMemoryInterface with StateWithParticipantsInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(numberOfCollects = this.numberOfCollects + 1).asInstanceOf[this.type]
    }

    override def toString = {
      s"EventSimpleNumberOfCollectsStateImplementation agentId $agentId, value $centralVariableValue, memory $memory, neighbors $neighborActions, collects $numberOfCollects"
    }
  }
}

trait EventExtendedMemoryState extends StateWithExtendedMemory with StateWithParticipants {
  type State = EventExtendedMemoryStateImplementation

  def computeExpectedUtilities(c: State): Map[Action, UtilityType]

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {
    val commonParticipants = extraInfo match {
      case Some(commonParticipantsInfo: scala.collection.mutable.Map[Int, (Int, Int)]) => commonParticipantsInfo
      case None => scala.collection.mutable.Map.empty[Int, (Int, Int)]
    }
    EventExtendedMemoryStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      memory = Map.empty[Action, Double].withDefaultValue(0.0),
      numberOfCollects = 0,
      memoryConverged = false,
      commonParticipants = commonParticipants.toMap)
  }

  case class EventExtendedMemoryStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    memory: Map[Action, UtilityType],
    numberOfCollects: Long, //TODO: rename to numberOfUpdates and check
    memoryConverged: Boolean,
    commonParticipants: Map[Int, (Int, Int)]) extends StateWithExtendedMemoryInterface with StateWithParticipantsInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }

    def areMemoriesSimilar(mem1: Map[Action, UtilityType], mem2: Map[Action, UtilityType]): Boolean = {
      if (mem1.keySet != mem2.keySet) {
        false
      } else {
        var results = true
        val tf1 = computeExpectedUtilities(this.copy(memory = mem1))
        val tf2 = computeExpectedUtilities(this.copy(memory = mem2))
        for (key <- mem1.keySet) {

          val res = (tf1.get(key), tf2.get(key)) match {
            case (Some(val1), Some(val2)) => {
              math.abs(val1 - val2) < 0.001
            }
            case _ => false
          }
          results = results && res
        }
        results
      }
    }

    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(memory = newMemory, numberOfCollects = this.numberOfCollects + 1, memoryConverged = areMemoriesSimilar(this.memory, newMemory)).asInstanceOf[this.type]
    }

    override def toString = {
      s"EventExtendedMemoryStateImplementation agentId $agentId, value $centralVariableValue, memory $memory, neighbors $neighborActions, collects $numberOfCollects, memoryConverged $memoryConverged"
    }
  }
}

