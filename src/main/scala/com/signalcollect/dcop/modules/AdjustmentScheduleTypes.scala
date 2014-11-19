package com.signalcollect.dcop.modules

import scala.util.Random

/**
 * All vertices/agents update every time.
 */
trait FloodAdjustmentSchedule extends Algorithm {
  def shouldConsiderMove(c: State) = true
}

/**
 * Agents/vertices update with inertia depending on their rank relative to their maximum-ranked neighbour.
 */
trait RankedBasedAdjustmentSchedule extends RankedState {

  //override type State = RankedState[AgentId, Action, SignalType, UtilityType]

  def relativeChangeProbability: Double

  def shouldConsiderMove(c: State) = {
    val maxNeighbourRank = c.ranks.values.max
    val rankForCurrentConfig = c.ranks(c.centralVariableAssignment._1)
    val relativeRankRatio = rankForCurrentConfig / maxNeighbourRank
    val changeProbability = 1 - relativeRankRatio * relativeChangeProbability // The higher the rank ratio, the lower the probability to change.
    Random.nextDouble <= changeProbability
  }
}

