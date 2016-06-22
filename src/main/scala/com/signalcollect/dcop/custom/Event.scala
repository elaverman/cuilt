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
import com.signalcollect.dcop.modules.Algorithm

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
    def commonParticipants: Map[Long, (Long,Long)]
  }
}

trait EventState extends StateWithParticipants {
  
  type State = EventStateImplementation

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], commonParticipants: Map[Long, (Long,Long)]): State = {
    EventStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      commonParticipants = commonParticipants)
  }

  case class EventStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    commonParticipants: Map[Long, (Long,Long)]) extends StateWithParticipantsInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
  }
}
