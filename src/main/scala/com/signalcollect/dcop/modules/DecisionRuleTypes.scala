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
    val res = isInLocalOptimumGivenUtilitiesAndMaxUtility(c, expectedUtilities, maxUtility)
//    if (!res) 
//      println("###"+c.agentId+"->"+c.centralVariableValue+": util: "+expectedUtilities.toString)
    res
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

trait ArgmaxBDecisionRule extends DecisionRule {

  def computeMove(c: State) = {
    val expectedUtilities: Map[Action, Double] = computeExpectedUtilities(c)
    val maxUtility = expectedUtilities.values.max
    val maxUtilityMoves: Seq[Action] = expectedUtilities.filter(_._2 == maxUtility).map(_._1).toSeq
    val numberOfMaxUtilityMoves = maxUtilityMoves.size

    //If we are converged already don't stir the boat
    // Attention! If isConverged no longer depends on the utility so 
    // the maxUtility move may not be the current move anymore...
    if ((isInLocalOptimumGivenUtilitiesAndMaxUtility(c, expectedUtilities, maxUtility)) &&
      (maxUtilityMoves.contains(c.centralVariableValue)) &&
      (c.computeExpectedNumberOfConflicts == 0)) {
      c.centralVariableValue
    } else {
      val chosenMaxUtilityMove = maxUtilityMoves(Random.nextInt(maxUtilityMoves.size))
      chosenMaxUtilityMove
    }
  }

}
