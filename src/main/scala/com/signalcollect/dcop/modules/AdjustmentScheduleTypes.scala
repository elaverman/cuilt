package com.signalcollect.dcop.modules

import scala.util.Random

trait AdjustmentSchedule extends Algorithm {
  def shouldConsiderMove(c: State): Boolean 
}

/**
 * All vertices/agents update every time.
 */
trait FloodAdjustmentSchedule extends AdjustmentSchedule {
  def shouldConsiderMove(c: State) = true
}

/**
 * Vertices update with a certain probability
 */
trait ParallelRandomAdjustmentSchedule extends AdjustmentSchedule {
  
  def changeProbability: Double
  
  def shouldConsiderMove(c: State) = {
    Random.nextDouble <= changeProbability
  }
}


/**
 * Agents/vertices update with inertia depending on their rank relative to their maximum-ranked neighbour.
 */
trait RankedBasedAdjustmentSchedule extends AdjustmentSchedule with RankedState {

  //override type State = RankedState[AgentId, Action, SignalType, UtilityType]

  def relativeChangeProbability: Double

  def shouldConsiderMove(c: State) = {
    val maxNeighbourRank = c.ranks.values.max
    val rankForCurrentConfig = c.ranks(c.agentId)
    val relativeRankRatio = rankForCurrentConfig / maxNeighbourRank
    val changeProbability = 1 - relativeRankRatio * relativeChangeProbability // The higher the rank ratio, the lower the probability to change.
    Random.nextDouble <= changeProbability
  }
}

