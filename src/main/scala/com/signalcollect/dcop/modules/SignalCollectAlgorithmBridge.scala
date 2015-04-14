/*
 *  @author Philip Stutz
 *  @author Mihaela Verman
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

package com.signalcollect.dcop.modules

import com.signalcollect._
import com.signalcollect.Vertex

/**
 * The bridge has methods for creating a vertex and an edge, and provides the implementations for them.
 */

trait SignalCollectAlgorithmBridge extends Algorithm {

  def createVertex(id: AgentId, initialAction: Action, domain: Set[Action]): Vertex[AgentId, State, Any, Any] = {
    new DcopVertex(id, createInitialState(id, initialAction, domain), false)
  }

  def createEdge(targetId: AgentId) = {
    new DcopEdge(targetId)
  }

  val ENSURE_COLLECT_MSG = -2

  /**
   * A Dcop vertex.
   *
   * @param id The Vertex Id
   * @param initialState Initial state of the vertex
   * @param debug Boolean indicating if there should be any printlines
   *
   * First, the vertex checks the scoreSignal function and if the result is 1, it calls
   * executeSignalOperation.
   * Afterwards, it calls deliverSignalWithSrcId. The standard implementation updates the mostRecentSignalMap.
   * Then, it calls the scoreCollect function, which returns 1 is the mostRecentSignalMap is not empty or if 
   * the edges connected to the vertex were modified. If scoreCollect returns 1, then the collect function
   * is executed. 
   * The operations are repeated until convergence is detected.
   * 
   * If a vertex should change its state but doesn't (one such case is when it is impacted by inertia),
   * the vertex needs to send a message to itself in order to ensure a next collect and that convergence
   * would not be falsely detected. 
   */
  class DcopVertex(
    id: AgentId,
    initialState: State,
    debug: Boolean = false)
    extends DataGraphVertex(id, initialState) {

    var ensureCollect = false
    var sendMessageToMyself = false

    override def scoreSignal: Double = {
      if (sendMessageToMyself) {
        //println(s"SignalCollectAlgorithmBridge.scoreSignal($id): Send messageBack " + state + " msgBack? " + sendMessageToMyself)
        1
      } else {
        if (edgesModifiedSinceSignalOperation) {
          //println(s"SignalCollectAlgorithmBridge.scoreSignal($id): edgesModified")
          1
        } else {
          lastSignalState match {
            case Some(oldState) => {
              if (isStateUnchanged(oldState, state) && isConverged(state.withCentralVariableAssignment(oldState.centralVariableValue))) {
                //println(s"SignalCollectAlgorithmBridge.scoreSignal($id): No Signal for state " + state + " msgBack? " + sendMessageToMyself)
                0
              } else {
                //println(s"SignalCollectAlgorithmBridge.scoreSignal($id): " + state.agentId + "-> unchanged? " + isStateUnchanged(oldState, state) + " " + oldState + " " + state + " converged? " + isConverged(state) + " converged last step? " + isConverged(state.withCentralVariableAssignment(oldState.centralVariableValue)) + " msgBack? " + sendMessageToMyself)
                1
              }
            }
            case noStateOrStateChanged => {
              //println(s"SignalCollectAlgorithmBridge.scoreSignal($id): noState")
              1
            }
          }
        }
      }
    }

    /**
     * Only send a signal if the signal is different from what was sent last time around.
     * Implicitly last time a 0 was sent.
     */
    override def executeSignalOperation(graphEditor: GraphEditor[Any, Any]) {
      if (sendMessageToMyself) {
        graphEditor.sendSignal(ENSURE_COLLECT_MSG, id, id)
      }
      super.executeSignalOperation(graphEditor)
    }

    override def deliverSignalWithSourceId(signal: Any, sourceId: Any, graphEditor: GraphEditor[Any, Any]): Boolean = {
      if (sourceId == id) {
        if (signal == ENSURE_COLLECT_MSG) {
          ensureCollect = true
        } else {
          ensureCollect = false
        }
        false
      } else {
        super.deliverSignalWithSourceId(signal, sourceId, graphEditor)
      }
    }

    /*
     * The collect function describes the way in which a vertex modifies its state based on
     * the signals it newly received, and returns the new state. In this context, it ties up the different
     * algorithm components.
     */
    def collect = {
      val c = updatedState
      if (shouldConsiderMove(c)) {
        val newMove = changeMove(c)
        //println(s"Vertex $id has considers new move $newMove, prior state $state.")
        newMove
      } else {
        if (isConverged(c)) {
          //println(s"Vertex $id has converged and stays at move of state $c, prior state $state.")
          sendMessageToMyself = false
          c
        } else {
          //println(s"Vertex $id still NOT converged, stays at move, and has state $c, prior state $state.")
          sendMessageToMyself = true
          c
        }
      }
    }

    /*
     * Updates state with the new received signals. Could be overriden depending on the state type. 
     */
    def updatedState: State = {
      val signalMap = mostRecentSignalMap.toMap
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

    def changeMove(c: State): State = {
      val move = computeMove(c)
      val newState = c.withCentralVariableAssignment(move)
      //if (debug) {
      // println(s"Vertex $id has changed its state from $state to $newState.")
      //}
      newState
    }

  }

  /**
   * A Dcop edge.
   *
   * @param targetId  The Id of the target vertex
   */
  class DcopEdge(targetId: AgentId) extends DefaultEdge[AgentId](targetId) {
    type Source = DcopVertex

    def signal = {
      val sourceState = source.state
      sourceState.centralVariableValue
    }
  }

}