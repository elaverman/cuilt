/*
 *  @author Philip Stutz
 *  @author Mihaela Verman
 *  
 *  Copyright 2013 University of Zurich
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

package com.signalcollect.dcop.graph
import com.signalcollect._
import com.signalcollect.dcop.modules._
import com.signalcollect.dcop.impl.SimpleConfig

class SimpleDcopEdge[Id](targetId: Id) extends DefaultEdge(targetId) {
  type Source = SimpleDcopVertex[_, _, _, _, _]

  def signal = {
    val sourceState = source.state
    ???
  }
}

class SimpleDcopVertex[VertexId, VertexState <: StateTrait[VertexId, VertexAction, VertexSignalType, VertexUtilityType], VertexAction, VertexSignalType, VertexUtilityType](
  sAlgorithm: Algorithm {
    type AgentId = VertexId
    type Action = VertexAction
    type SignalType = VertexSignalType
    type State = VertexState
  },
  sInitialState: VertexState,
  debug: Boolean = false) extends DcopVertex[VertexId, VertexState, VertexAction, VertexSignalType, VertexUtilityType](sAlgorithm, sInitialState, debug) {

  val x = state.centralVariableValue
}
