package com.signalcollect.dcop.modules

import com.signalcollect._
import com.signalcollect.dcop.graph._

/**
 * Interface for every algorithm, containing the needed methods.
 */
trait Algorithm extends Serializable {

  type AgentId
  type Action
  type NeighborMetadata //Ranks etc. 
  type SignalType
  type UtilityType = Double
  type State <: StateInterface

  /*
   * An Algorithm needs to have a state type that implements the state interface. 
   * Standard implementations can be mixed in from SimpleState and RankedState.
   */
  trait StateInterface {
    def agentId: AgentId
    def centralVariableValue: Action
    def domain: Set[Action]
    def neighborActions: Map[AgentId, Action]

    def withCentralVariableAssignment(value: Action): this.type
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]): this.type

    def updateNeighbourhood(n: Map[AgentId, Any]): this.type = {
      var metadataEncountered = false
      //TODO: turn this into functional code with n.unzip
      val actionMap = n.map {
        case (key, value) =>
          value match {
            //TODO investigate warning
            case action: Action => (key, action)
            case (action: Action, metadata: NeighborMetadata) =>
              metadataEncountered = true
              (key, action)
            case other => throw new Exception(s"blah blah state could not handle sifgnal blah")
          }
      }

      //      if (metadataEncountered) {
      //        val metadataMap = n.map {
      //          case (key, value) =>
      //            value match {
      //              case (action, metadata: NeighborMetadata) => (key, metadata)
      //            }
      //        }
      //      }
      this.withUpdatedNeighborActions(actionMap)

    }

  }

  def createVertex(id: AgentId, initialAction: Action, domain: Set[Action]): Vertex[AgentId, State, Any, Any] // SignalCollectAlgorithmBridge or similar

  //TODO: Shouldn't we also require createEdge from the bridge?
  
  def createInitialState(id: AgentId, action: Action, domain: Set[Action]): State //state

  def shouldConsiderMove(c: State): Boolean //adjustment schedule

  def computeMove(c: State): Action //decision rule

  def isInLocalOptimum(c: State): Boolean //decision rule

  def shouldTerminate(c: State): Boolean //termination rule

  def updateMemory(c: State): State //target function

  def computeExpectedUtilities(c: State): Map[Action, UtilityType] //target function

  def computeUtility(c: State): UtilityType //utility

}

trait IntAlgorithm extends Algorithm {
  type Action = Int
  type AgentId = Int
}
