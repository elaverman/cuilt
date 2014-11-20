package com.signalcollect.dcop.modules

/**
 * State implementations.
 */
trait SimpleState extends Algorithm {
  type State = SimpleStateImplementation

  case class SimpleStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action]) extends StateInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
  }
}

trait StateWithMemory extends Algorithm {
  type State <: StateWithMemoryInterface

  trait StateWithMemoryInterface extends StateInterface {
    def memory: Map[Action, UtilityType]
    def numberOfCollects: Long //to rename to numberOfUpdates and check
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]): this.type
  }
}

trait SimpleMemoryState extends StateWithMemory {
  type State = SimpleMemoryStateImplementation

  case class SimpleMemoryStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    memory: Map[Action, UtilityType],
    numberOfCollects: Long //to rename to numberOfUpdates and check
    ) extends StateWithMemoryInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(memory = newMemory).asInstanceOf[this.type]
    }
  }
}

trait StateWithRank extends Algorithm {
  type State <: StateWithRankInterface

  trait StateWithRankInterface extends StateInterface {
    def neighborMetadata: Map[AgentId, NeighborMetadata]
    def withUpdatedNeighborMetadata(newNeighborMetadata: Map[AgentId, NeighborMetadata]): this.type
    def ranks: Map[AgentId, Double]
  }
}

trait RankedState extends StateWithRank {
  type State = RankedStateImplementation
  type NeighborMetadata = Double

  case class RankedStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    neighborMetadata: Map[AgentId, NeighborMetadata]) extends StateWithRankInterface {
    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    //TODO: Should we also update and calculate the ranks here?
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedNeighborMetadata(newNeighborMetadata: Map[AgentId, NeighborMetadata]) = {
      this.copy(neighborMetadata = newNeighborMetadata).asInstanceOf[this.type]
    }
    def ranks = neighborMetadata
  }
}

trait RankedMemoryState extends StateWithMemory with StateWithRank {
  type State = RankedMemoryStateImplementation
  type NeighborMetadata = Double

  case class RankedMemoryStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    memory: Map[Action, UtilityType],
    numberOfCollects: Long,
    neighborMetadata: Map[AgentId, NeighborMetadata]) extends StateWithMemoryInterface with StateWithRankInterface {
    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    //TODO: Should we also update and calculate the ranks here?
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(memory = newMemory).asInstanceOf[this.type]
    }
    def withUpdatedNeighborMetadata(newNeighborMetadata: Map[AgentId, NeighborMetadata]) = {
      this.copy(neighborMetadata = newNeighborMetadata).asInstanceOf[this.type]
    }
    def ranks = neighborMetadata
  }
}




