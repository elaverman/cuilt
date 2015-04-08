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
  type State <: StateType

  /*
   * An Algorithm needs to have a state type that implements the state interface. 
   * Standard implementations can be mixed in from SimpleState and RankedState.
   */
  trait StateType {
    def agentId: AgentId
    def centralVariableValue: Action
    def domain: Set[Action]
    def neighborActions: Map[AgentId, Action]

    def withCentralVariableAssignment(value: Action): this.type
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]): this.type
    //TODO: Used for ArgmaxB decision rule and for ZeroConflictConvergenceDetection.
    def computeExpectedNumberOfConflicts: Int
    def updateNeighbourhood(n: Map[AgentId, Any]): this.type

  }

  def algorithmName: String
  
  def createVertex(id: AgentId, initialAction: Action, domain: Set[Action]): Vertex[AgentId, State, Any, Any] // SignalCollectAlgorithmBridge or similar

  def createEdge(targetId: AgentId): Edge[AgentId] 
  //TODO: Shouldn't we also require createEdge from the bridge?

  def createInitialState(id: AgentId, action: Action, domain: Set[Action]): State //state

  def shouldConsiderMove(c: State): Boolean //adjustment schedule

  def computeMove(c: State): Action //decision rule

  def isInLocalOptimum(c: State): Boolean //decision rule

  def shouldTerminate(c: State): Boolean //termination rule

  def updateMemoryOfStateFromState(toUpdateState: State, fromState: State): State //target function

  def computeExpectedUtilities(c: State): Map[Action, UtilityType] //target function

  def computeUtility(c: State): UtilityType //utility

}

trait IntAlgorithm extends Algorithm {
  type Action = Int
  type AgentId = Int
}
