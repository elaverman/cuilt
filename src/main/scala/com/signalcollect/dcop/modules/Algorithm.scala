package com.signalcollect.dcop.modules

import com.signalcollect._
import com.signalcollect.dcop.graph._

/**
 * Interface for every algorithm, containing the needed methods.
 */
trait Algorithm extends Serializable {

  type AgentId
  type Action
  type SignalType
  type UtilityType = Double
  type State <: StateInterface

  /*
   * An Algorithm needs to have a state type that implements the state interface. 
   * Standard implementations can be mixed in from SimpleState and RankedState.
   */
  trait StateInterface {
    def neighborhood: Map[AgentId, SignalType]
    def centralVariableValue: Action
    def centralVariableAssignment: (AgentId, Action)
    def memory: Map[Action, UtilityType]
    def numberOfCollects: Long //TODO: Rename to number of updates and verify it is = no of collects
    def domain: Set[Action]
    def withCentralVariableAssignment(value: Action): this.type
    def withUpdatedNeighborhood(newNeighborhood: Map[AgentId, SignalType]): this.type
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]): this.type
  }
  
  def createVertex(id: AgentId, initialAction: Action, domain: Set[Action]): Vertex[AgentId, State, Any, Any] // SignalCollectAlgorithmBridge or similar

  def createInitialState(action: Action, domain: Set[Action]): State

  def shouldConsiderMove(c: State): Boolean //adjustment schedule

  def computeMove(c: State): Action //decision rule

  def isInLocalOptimum(c: State): Boolean //decision rule

  def shouldTerminate(c: State): Boolean //termination rule

  def updateMemory(c: State): State //target function

  def computeExpectedUtilities(c: State): Map[Action, UtilityType] //target function

  def computeUtility(c: State): UtilityType //utility

}
