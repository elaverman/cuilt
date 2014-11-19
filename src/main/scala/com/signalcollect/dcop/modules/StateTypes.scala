package com.signalcollect.dcop.modules



/**
 * State implementations.
 */
trait SimpleState extends Algorithm {
  type State = SimpleStateImplementation
  
  case class SimpleStateImplementation(
    neighborhood: Map[AgentId, SignalType],
    centralVariableValue: Action,
    centralVariableAssignment: (AgentId, Action),
    memory: Map[Action, UtilityType],
    numberOfCollects: Long,
    domain: Set[Action]) extends StateInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableAssignment = (centralVariableAssignment._1, value)).asInstanceOf[this.type]
    }
    def withUpdatedNeighborhood(newNeighborhood: Map[AgentId, SignalType]) = {
      this.copy(neighborhood = newNeighborhood).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(memory = newMemory).asInstanceOf[this.type]
    }
  }
}

trait RankedState extends Algorithm {
  type State = RankedStateImplementation

  case class RankedStateImplementation(
    neighborhood: Map[AgentId, SignalType],
    centralVariableValue: Action,
    centralVariableAssignment: (AgentId, Action),
    memory: Map[Action, UtilityType],
    ranks: Map[AgentId, Double],
    numberOfCollects: Long,
    domain: Set[Action]) extends StateInterface {
    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableAssignment = (centralVariableAssignment._1, value)).asInstanceOf[this.type]
    }
    //TODO: Should we also update and calculate the ranks here?
    def withUpdatedNeighborhood(newNeighborhood: Map[AgentId, SignalType]) = {
      this.copy(neighborhood = newNeighborhood).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(memory = newMemory).asInstanceOf[this.type]
    }
  }

}




