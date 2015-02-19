package com.signalcollect.dcop.modules

import com.signalcollect._
import com.signalcollect.Vertex

trait SignalCollectAlgorithmBridge extends Algorithm  {

  def createVertex(id: AgentId, initialAction: Action, domain: Set[Action]): Vertex[AgentId, State, Any, Any] = {
    new DcopVertex(id, createInitialState(id, initialAction, domain), false)
  }

  def createEdge(targetId: AgentId) = {
    new DcopEdge(targetId)
  }

  /**
   * A Dcop vertex.
   *
   * @param id The Vertex Id
   * @param initialState Initial state of the vertex
   * @param debug Boolean indicating if there should be any printlines
   */
  class DcopVertex(
    id: AgentId,
    initialState: State,
    debug: Boolean = false)
    extends DataGraphVertex(id, initialState) {

    def changeMove(c: State): State = {
      val move = computeMove(c)
      val newConfig = c.withCentralVariableAssignment(move)
      val newState = newConfig
      //if (debug) {
        println(s"Vertex $id has changed its state from $state to $newState.")
      //}
      newState
    }

    def collect = {
      val c = updatedState
      if (shouldConsiderMove(c)) {
        changeMove(c)
      } else {
        //if (debug) {
          if (isConverged(c)) {
            println(s"Vertex $id has converged and stays at move of state $c, prior state $state.")
          } else {
            println(s"Vertex $id still NOT converged, stays at move, and has state $c, prior state $state.")
          }
       // }
        c
      }
    }

    /*
     * Updates state with the new received signals. Could be overriden depending on the state type. 
     */
    def updatedState: State = {
      val signalMap = mostRecentSignalMap.toMap
      //signalMap.asInstanceOf[Map[Id, Signal]]
      //val neighborhoodUpdated = state.withUpdatedNeighborhood(Map.empty[VertexId, Signal].asInstanceOf[Map[VertexId, VertexSignalType]])
      val neighborhoodUpdated = state.updateNeighbourhood(signalMap.asInstanceOf[Map[AgentId, SignalType]])
      val c = updateMemory(neighborhoodUpdated)
      c
    }

    def isConverged(c: State): Boolean = {
      shouldTerminate(c)
    }

    def isStateUnchanged(oldState: State, newState: State): Boolean = {
      oldState.centralVariableValue == newState.centralVariableValue &&
        oldState.neighborActions == newState.neighborActions
    }

    override def scoreSignal: Double = {
      if (edgesModifiedSinceSignalOperation) {
        println("SignalCollectAlgorithmBridge.scoreSignal: edgesModified")
        1
      } else {
        lastSignalState match {
          case Some(oldState) => {
            if (isStateUnchanged(oldState, state) && isConverged(state.withCentralVariableAssignment(oldState.centralVariableValue))) {
              println("SignalCollectAlgorithmBridge.scoreSignal: No Signal for state " + state)
              0
            } else {
              println("SignalCollectAlgorithmBridge.scoreSignal: "+state.agentId + "-> unchanged? "+isStateUnchanged(oldState, state)+" converged? "+ isConverged(state) + " converged last step? "+isConverged(state.withCentralVariableAssignment(oldState.centralVariableValue)))
              1
            }
          }
          case noStateOrStateChanged => {
            println("SignalCollectAlgorithmBridge.scoreSignal: noState")
            1
          }
        }
      }
    }
  }

  class DcopEdge(targetId: AgentId) extends DefaultEdge[AgentId](targetId) {
    type Source = DcopVertex

    def signal = {
      val sourceState = source.state
      sourceState.centralVariableValue
    }
  }

}