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

/**
 * State implementations.
 *
 * A state module extends Algorithm and it provides a method to create an initial state and a state implementation.
 * 
 * I[1] StateModule (Main StateInterface)
 *    C[2] SimpleState (Case Class) extends [1]
 *    I[3] StateWithMemory (Interface) extends [1]
 *        C[4] SimpleMemoryState (Case Class) extends [3]
 *        C[5] SimpleNumberOfCollectsState (Case Class) extends [3]
 *        C[6] ExtendedMemoryState (Case Class) extends [3]
 *    I[7] StateWithNeighborMemory (Interface) extends [1]
 *        C[8] SimpleNeighborMemoryState (Case Class) extends [7]
 *    I[9] StateWithRank (Interface) extends [1]
 *        C[10] RankedState (Case Class) extends [9]
 *        C[11] RankedMemoryState (Case Class) extends [3 and 7]
 */

trait StateModule extends Algorithm {
  type State <: StateInterface

  // Called by createVertex() on SignalCollectAlgorithmBridge with default value None for extraInfo.
  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State

  // TODO: Add extraInfo here or on the extending traits to support more general states.
  trait StateInterface extends StateType {
    def agentId: AgentId
    def centralVariableValue: Action
    def domain: Set[Action]
    def neighborActions: Map[AgentId, Action]

    def withCentralVariableAssignment(value: Action): this.type
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]): this.type

    //TODO: Used for ArgmaxB decision rule and for ZeroConflictConvergenceDetection.
    def computeExpectedNumberOfConflicts = {
      val occupiedColors = neighborActions.values
      val numberOfConflicts = occupiedColors.filter(_ == centralVariableValue).size
      numberOfConflicts
    }

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
            case other => throw new Exception(s"State could not handle signal.")
          }
      }

      this.withUpdatedNeighborActions(actionMap)

    }
  }
}

trait SimpleState extends StateModule {
  type State = SimpleStateImplementation

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {
    SimpleStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head))
  }

  case class SimpleStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action]) extends StateInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
  }
}

trait StateWithMemory extends StateModule {
  type State <: StateWithMemoryInterface

  trait StateWithMemoryInterface extends StateInterface {
    def memory: Map[Action, UtilityType]
    def numberOfCollects: Long //to rename to numberOfUpdates and check
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]): this.type
  }
}

trait SimpleMemoryState extends StateWithMemory {
  type State = SimpleMemoryStateImplementation

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {
    SimpleMemoryStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      memory = Map.empty[Action, Double].withDefaultValue(0.0),
      numberOfCollects = 0)
  }

  case class SimpleMemoryStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    memory: Map[Action, UtilityType],
    numberOfCollects: Long //TODO: rename to numberOfUpdates and check
    ) extends StateWithMemoryInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(memory = newMemory, numberOfCollects = this.numberOfCollects + 1).asInstanceOf[this.type]
    }

    override def toString = {
      s"agentId $agentId, value $centralVariableValue, memory $memory, neighbors $neighborActions, collects $numberOfCollects"
    }
  }
}

trait SimpleNumberOfCollectsState extends StateWithMemory {
  type State = SimpleNumberOfCollectsStateImplementation

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {
    SimpleNumberOfCollectsStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      memory = Map.empty[Action, Double].withDefaultValue(0.0),
      numberOfCollects = 0)
  }

  case class SimpleNumberOfCollectsStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    memory: Map[Action, UtilityType],
    numberOfCollects: Long //TODO: rename to numberOfUpdates and check
    ) extends StateWithMemoryInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(numberOfCollects = this.numberOfCollects + 1).asInstanceOf[this.type]
    }

    override def toString = {
      s"agentId $agentId, value $centralVariableValue, memory $memory, neighbors $neighborActions, collects $numberOfCollects"
    }
  }
}

trait ExtendedMemoryState extends StateWithMemory {
  type State = ExtendedMemoryStateImplementation

  def computeExpectedUtilities(c: State): Map[Action, UtilityType]

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {

    //    this match {
    //      case a: AverageRegretsTargetFunction => {
    //        println("Regrets!")
    //      }
    //      case other => {
    //        println("No regrets!")
    //      }
    //    }

    ExtendedMemoryStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      memory = Map.empty[Action, Double].withDefaultValue(0.0),
      numberOfCollects = 0,
      memoryConverged = false)
  }

  case class ExtendedMemoryStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    memory: Map[Action, UtilityType],
    numberOfCollects: Long, //TODO: rename to numberOfUpdates and check
    memoryConverged: Boolean) extends StateWithMemoryInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }

    def areMemoriesSimilar(mem1: Map[Action, UtilityType], mem2: Map[Action, UtilityType]): Boolean = {
      if (mem1.keySet != mem2.keySet) {
        false
      } else {
        var results = true
        val tf1 = computeExpectedUtilities(this.copy(memory = mem1))
        val tf2 = computeExpectedUtilities(this.copy(memory = mem2))
        for (key <- mem1.keySet) {

          val res = (tf1.get(key), tf2.get(key)) match {
            case (Some(val1), Some(val2)) => {

              //              this match {
              //                case a: AverageRegretsTargetFunction => {
              ////                  println("Regrets!")
              //                  math.abs(math.max(0, val1) - math.max(0, val2)) < 0.000001
              //                }
              //                case b: DiscountedAverageRegretsTargetFunction => {
              ////                  println("Regrets!")
              //                  math.abs(math.max(0, val1) - math.max(0, val2)) < 0.000001
              //                }
              //                case other => {
              ////                  println("No regrets!")
              // assert(val1 >= 0 && val2 >= 0, "have to be positive regrets")
              math.abs(val1 - val2) < 0.001
              //                }
              //              }

            }
            case _ => false
          }
          results = results && res
        }
        results
      }
    }

    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(memory = newMemory, numberOfCollects = this.numberOfCollects + 1, memoryConverged = areMemoriesSimilar(this.memory, newMemory)).asInstanceOf[this.type]
    }

    override def toString = {
      s"agentId $agentId, value $centralVariableValue, memory $memory, neighbors $neighborActions, collects $numberOfCollects, memoryConverged $memoryConverged"
    }
  }
}

trait StateWithNeighborMemory extends StateModule {
  type State <: StateWithNeighborMemoryInterface

  trait StateWithNeighborMemoryInterface extends StateInterface {
    def memory: Map[AgentId, Map[Action, Double]] //for each neighbor we keep their actions and "how much" they used that action
    def numberOfCollects: Long //to rename to numberOfUpdates and check
    def withUpdatedMemory(newMemory: Map[AgentId, Map[Action, Double]]): this.type
  }
}

trait SimpleNeighborMemoryState extends StateWithNeighborMemory {
  type State = SimpleNeighborMemoryStateImplementation

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {
    SimpleNeighborMemoryStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      memory = Map.empty[AgentId, Map[Action, Double]].withDefaultValue(Map.empty[Action, Double]),
      numberOfCollects = 0)
  }

  case class SimpleNeighborMemoryStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    memory: Map[AgentId, Map[Action, Double]],
    numberOfCollects: Long //TODO: rename to numberOfUpdates and check
    ) extends StateWithNeighborMemoryInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[AgentId, Map[Action, Double]]) = {
      this.copy(memory = newMemory, numberOfCollects = this.numberOfCollects + 1).asInstanceOf[this.type]
    }

    override def toString = {
      s"agentId $agentId, value $centralVariableValue, memory $memory, neighbors $neighborActions, collects $numberOfCollects"
    }
  }
}

trait StateWithRank extends StateModule {
  type State <: StateWithRankInterface

  trait StateWithRankInterface extends StateInterface {
    def neighborMetadata: Map[AgentId, NeighborMetadata]
    def withUpdatedNeighborMetadata(newNeighborMetadata: Map[AgentId, NeighborMetadata]): this.type
    def ranks: Map[AgentId, Double]

    override def updateNeighbourhood(n: Map[AgentId, Any]): this.type = {
      var metadataEncountered = false
      //TODO: turn this into functional code with n.unzip
      val actionMap = n.map {
        case (key, value) =>
          value match {
            //TODO Problem with type erasure!
            case action: Action => (key, action)
            case (action: Action, metadata: NeighborMetadata) =>
              metadataEncountered = true
              (key, action)
            case other => throw new Exception(s"blah blah state could not handle sifgnal blah")
          }
      }
      val updatedWithActions = this.withUpdatedNeighborActions(actionMap)

      if (metadataEncountered) {
        val metadataMap = n.map {
          case (key, value) =>
            value match {
              case (action, metadata: NeighborMetadata) => (key, metadata)
            }
        }.asInstanceOf[Map[AgentId, NeighborMetadata]]
        updatedWithActions.withUpdatedNeighborMetadata(metadataMap)
      } else {
        updatedWithActions
      }

    }
  }
}

trait RankedState extends StateWithRank {
  type State = RankedStateImplementation
  type NeighborMetadata = Double

  def createInitialState(id: AgentId, action: Action, domain: Set[Action], extraInfo: Option[Any]): State = {
    RankedStateImplementation(
      agentId = id,
      centralVariableValue = action,
      domain = domain,
      neighborActions = Map.empty[AgentId, Action].withDefaultValue(domain.head),
      neighborMetadata = Map.empty[AgentId, NeighborMetadata])
  }

  case class RankedStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    neighborMetadata: Map[AgentId, NeighborMetadata]) extends StateWithRankInterface {
    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    //TODO: Should we also update and calculate the ranks here?
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedNeighborMetadata(newNeighborMetadata: Map[AgentId, NeighborMetadata]) = {
      this.copy(neighborMetadata = newNeighborMetadata).asInstanceOf[this.type]
    }
    def ranks = neighborMetadata

  }
}

trait RankedMemoryState extends StateWithMemory with StateWithRank {
  type State = RankedMemoryStateImplementation
  type NeighborMetadata = Double

  case class RankedMemoryStateImplementation(
    agentId: AgentId,
    centralVariableValue: Action,
    domain: Set[Action],
    neighborActions: Map[AgentId, Action],
    memory: Map[Action, UtilityType],
    numberOfCollects: Long,
    neighborMetadata: Map[AgentId, NeighborMetadata]) extends StateWithMemoryInterface with StateWithRankInterface {
    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    //TODO: Should we also update and calculate the ranks here?
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(memory = newMemory).asInstanceOf[this.type]
    }
    def withUpdatedNeighborMetadata(newNeighborMetadata: Map[AgentId, NeighborMetadata]) = {
      this.copy(neighborMetadata = newNeighborMetadata).asInstanceOf[this.type]
    }
    def ranks = neighborMetadata
  }
}




