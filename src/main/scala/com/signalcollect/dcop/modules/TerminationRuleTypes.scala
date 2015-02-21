package com.signalcollect.dcop.modules

/**
 * Termination rule implementations.
 * Should contain implementations for the shouldTerminate method.
 */

trait TerminationRule extends Algorithm {
  
  def shouldTerminate(c: State): Boolean
  
}

trait NashEquilibriumConvergence extends TerminationRule {

  def shouldTerminate(c: State): Boolean = isInLocalOptimum(c)

}


trait SimulatedAnnealingConvergence extends TerminationRule {

  def deltaComp: Double
  def iteration: Int
  
  def shouldTerminate(c: State): Boolean = isInLocalOptimum(c) && (scala.math.exp(deltaComp * iteration * iteration) < 0.01)

}
