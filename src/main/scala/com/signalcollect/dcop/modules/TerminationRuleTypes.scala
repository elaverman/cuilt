package com.signalcollect.dcop.modules


/**
 * Termination rule implementations. 
 * Should contain implementations for the shouldTerminate method.
 */

trait NashEquilibriumConvergence extends DecisionRule {

  def shouldTerminate(c: State): Boolean = isInLocalOptimum(c)

}

