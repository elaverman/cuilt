package com.signalcollect.dcop.graph

import com.signalcollect.dcop.modules.Algorithm
import com.signalcollect.DataGraphVertex
import com.signalcollect.Vertex

trait SignalCollectAlgorithmBridge extends Algorithm {
  
  def createVertex(id: AgentId, initialState: State): Vertex[AgentId, State, Any, Any] = {
    new DcopVertex(id, initialState, false)
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
      if (debug) {
        println(s"Vertex $id has changed its state from $state to $newState.")
      }
      newState
    }

    def collect = {
      val c = updatedState
      if (shouldConsiderMove(c)) {
        changeMove(c)
      } else {
        if (debug) {
          if (isConverged(c)) {
            println(s"Vertex $id has converged and stays at move of state $c, prior state $state.")
          } else {
            println(s"Vertex $id still NOT converged, stays at move, and has state $c, prior state $state.")
          }
        }
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
      val neighborhoodUpdated = state.withUpdatedNeighborhood(signalMap.asInstanceOf[Map[AgentId, SignalType]])
      val c = updateMemory(neighborhoodUpdated)
      c
    }

    def isConverged(c: State): Boolean = {
      shouldTerminate(c)
    }

    def isStateUnchanged(oldState: State, newState: State): Boolean = {
      oldState.centralVariableAssignment == newState.centralVariableAssignment &&
        oldState.neighborhood == newState.neighborhood
    }

    override def scoreSignal: Double = {
      if (edgesModifiedSinceSignalOperation) {
        1
      } else {
        lastSignalState match {
          case Some(oldState) => {
            if (isStateUnchanged(oldState, state) && isConverged(state)) {
              //println("=>" + state)
              0
            } else {
              1
            }
          }
          case noStateOrStateChanged => 1
        }
      }
    }
  }
}