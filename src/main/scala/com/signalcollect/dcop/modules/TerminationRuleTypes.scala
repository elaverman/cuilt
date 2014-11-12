package com.signalcollect.dcop.modules


/**
 * Termination rule implementations.
 */

trait NashEquilibriumConvergence extends DecisionRule {

  def shouldTerminate(c: State): Boolean = isInLocalOptimum(c)

}

