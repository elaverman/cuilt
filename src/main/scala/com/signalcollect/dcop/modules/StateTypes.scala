package com.signalcollect.dcop.modules

/**
 * State implementations.
 */

trait StateModule extends Algorithm {
  type State <: StateInterface

  def createInitialState(id: AgentId, action: Action, domain: Set[Action]): State
  
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
}

trait SimpleState extends StateModule {
  type State = SimpleStateImplementation

  def createInitialState(id: AgentId, action: Action, domain: Set[Action]): State = {
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

  def createInitialState(id: AgentId, action: Action, domain: Set[Action]): State = {
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
    numberOfCollects: Long //to rename to numberOfUpdates and check
    ) extends StateWithMemoryInterface {

    def withCentralVariableAssignment(value: Action) = {
      this.copy(centralVariableValue = value).asInstanceOf[this.type]
    }
    def withUpdatedNeighborActions(newNeighborActions: Map[AgentId, Action]) = {
      this.copy(neighborActions = newNeighborActions).asInstanceOf[this.type]
    }
    def withUpdatedMemory(newMemory: Map[Action, UtilityType]) = {
      this.copy(memory = newMemory).asInstanceOf[this.type]
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

  def createInitialState(id: AgentId, action: Action, domain: Set[Action]): State = {
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




