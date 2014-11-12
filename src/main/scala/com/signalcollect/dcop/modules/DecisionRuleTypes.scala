package com.signalcollect.dcop.modules

import scala.util.Random

/**
 * The module. Contains methods that are usually used by decision rules.
 */

trait DecisionRule extends Algorithm {

  // type UtilityType = Double

  def computeMove(c: State): Action

  def isInLocalOptimum(c: State): Boolean = {
    val expectedUtilities: Map[Action, Double] = computeExpectedUtilities(c)
    val maxUtility = expectedUtilities.values.max
    isInLocalOptimumGivenUtilitiesAndMaxUtility(c, expectedUtilities, maxUtility)
  }

  protected final def isInLocalOptimumGivenUtilitiesAndMaxUtility(
    c: State,
    expectedUtilities: Map[Action, Double],
    maxUtility: Double): Boolean = {
    val currentUtility = expectedUtilities(c.centralVariableValue)
    maxUtility == currentUtility
  }
}

/**
 * Decision rule implementations
 */

trait ArgmaxADecisionRule extends DecisionRule {

  def computeMove(c: State) = {
    val expectedUtilities: Map[Action, Double] = computeExpectedUtilities(c)
    val maxUtility = expectedUtilities.values.max
    if (isInLocalOptimumGivenUtilitiesAndMaxUtility(c, expectedUtilities, maxUtility)) {
      c.centralVariableValue
    } else {
      val maxUtilityMoves: Seq[Action] = expectedUtilities.filter(_._2 == maxUtility).map(_._1).toSeq
      if (maxUtilityMoves.size <= 0) println(expectedUtilities)
      assert(maxUtilityMoves.size > 0)
      val chosenMaxUtilityMove = maxUtilityMoves(Random.nextInt(maxUtilityMoves.size))
      chosenMaxUtilityMove
    }
  }

}
