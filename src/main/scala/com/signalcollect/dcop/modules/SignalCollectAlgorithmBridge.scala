package com.signalcollect.dcop.modules

import com.signalcollect._
import com.signalcollect.Vertex

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
                //println(s"SignalCollectAlgorithmBridge.scoreSignal($id): " + state.agentId + "-> unchanged? " + isStateUnchanged(oldState, state) + " converged? " + isConverged(state) + " converged last step? " + isConverged(state.withCentralVariableAssignment(oldState.centralVariableValue)) + " msgBack? " + sendMessageToMyself)
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
        //println(s"$id: Sending message to myself??")
        graphEditor.sendSignal(ENSURE_COLLECT_MSG, id, id)
      } else {
        //println(s"$id: NOT sending message to myself.")
      }
      super.executeSignalOperation(graphEditor)
    }

    override def deliverSignalWithSourceId(signal: Any, sourceId: Any, graphEditor: GraphEditor[Any, Any]): Boolean = {
      if (sourceId == id) {
        //println(s"$id: Receiving message from myself??")
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

    //    override def scoreCollect = {
    //      if (ensureCollect) {
    //        println(s"collect ensured for agent $id")
    //        1
    //      } else {
    //        super.scoreCollect
    //      }
    //    }

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
          c //.withExpectedChange
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
        oldState.neighborActions == newState.neighborActions // &&
      //oldState.changeCounter == newState.changeCounter
    }

    def changeMove(c: State): State = {
      val move = computeMove(c)
      val newConfig = c.withCentralVariableAssignment(move)
      val newState = newConfig
      //if (debug) {
       // println(s"Vertex $id has changed its state from $state to $newState.")
      //}
      newState
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