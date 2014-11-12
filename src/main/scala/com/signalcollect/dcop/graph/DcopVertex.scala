package com.signalcollect.dcop.graph

import com.signalcollect.dcop.modules._
import com.signalcollect.DataGraphVertex

/**
 * A Dcop vertex. Description: The Vertex state is of type Config.
 *
 * @param id The Vertex Id
 * @param domain The variable Domain
 * @param optimizer The optimizer used
 * @param initialState Initial state of the vertex
 * @param debug Boolean idicating if there should be any printlines
 */
abstract class DcopVertex[VertexId, VertexState <: StateTrait[VertexId, VertexAction, VertexSignalType, VertexUtilityType], VertexAction, VertexSignalType, VertexUtilityType](
  val algorithm: Algorithm {
    type AgentId = VertexId
    type Action = VertexAction
    type SignalType = VertexSignalType
    type State = VertexState
  },
  val initialState: VertexState,
  debug: Boolean = false)
  extends DataGraphVertex(initialState.centralVariableAssignment._1, initialState) {

  type Signal = Any

  def changeMove(c: VertexState): VertexState = {
    val move = algorithm.computeMove(c)
    val newConfig = c.withCentralVariableAssignment(move)
    val newState = newConfig
    if (debug) {
      println(s"Vertex $id has changed its state from $state to $newState.")
    }
    newState
  }

  def collect = {
    val signalMap = mostRecentSignalMap.toMap
    //signalMap.asInstanceOf[Map[Id, Signal]]
    //val neighborhoodUpdated = state.withUpdatedNeighborhood(Map.empty[VertexId, Signal].asInstanceOf[Map[VertexId, VertexSignalType]])
    val neighborhoodUpdated = state.withUpdatedNeighborhood(signalMap.asInstanceOf[Map[VertexId, VertexSignalType]])
    val c = algorithm.updateMemory(neighborhoodUpdated)
    // val c = currentConfig
    if (algorithm.shouldConsiderMove(c)) {
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

  def isConverged(c: VertexState): Boolean = {
    algorithm.shouldTerminate(c)
  }

  //def currentState: VertexState

  def isStateUnchanged(oldState: VertexState, newState: VertexState): Boolean = {
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