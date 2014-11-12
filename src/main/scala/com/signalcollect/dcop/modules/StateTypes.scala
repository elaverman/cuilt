package com.signalcollect.dcop.modules

/**
 * The module. Contains the must-haves for all the State types.
 */

trait StateTrait[AgentId, Action, SignalType, UtilityType] {
  def neighborhood: Map[AgentId, SignalType]
  def centralVariableValue: Action
  def centralVariableAssignment: (AgentId, Action)
  def memory: Map[Action, UtilityType]
  def numberOfCollects: Long
  def domain: Set[Action]
  def withCentralVariableAssignment(value: Action): this.type
  def withUpdatedNeighborhood(newNeighborhood: Map[AgentId, SignalType]): this.type
  def withUpdatedMemory(newMemory: Map[Action, UtilityType]): this.type
}

/**
 * State implementations.
 */

case class SimpleState[AgentId, Action, SignalType, UtilityType](
  neighborhood: Map[AgentId, SignalType],
  centralVariableValue: Action,
  centralVariableAssignment: (AgentId, Action),
  memory: Map[Action, UtilityType],
  numberOfCollects: Long,
  domain: Set[Action]) extends StateTrait[AgentId, Action, SignalType, UtilityType] {

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


case class RankedState[AgentId, Action, SignalType, UtilityType](
  neighborhood: Map[AgentId, SignalType],
  centralVariableValue: Action,
  centralVariableAssignment: (AgentId, Action),
  memory: Map[Action, UtilityType],
  ranks: Map[AgentId, Double],
  numberOfCollects: Long,
  domain: Set[Action]) extends StateTrait[AgentId, Action, SignalType, UtilityType] {
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

