package com.signalcollect.dcop.modules

/**
 * Target function implementations.
 * Should contain implementations for the functions: updateMemory and computeExpectedUtilities
 */

trait MemoryLessTargetFunction extends Algorithm {

  def computeCandidates(c: State) = {
    for {
      assignment <- c.domain
    } yield c.withCentralVariableAssignment(assignment)
  }

  def computeExpectedUtilities(c: State) = {
    val configUtilities = computeCandidates(c).map(c => (c.centralVariableValue, computeUtility(c))).toMap
    configUtilities
  }

  override def updateMemory(c: State): State = c
}

trait AverageExpectedUtilityTargetFunction extends MemoryLessTargetFunction with StateWithMemory {

  override def computeExpectedUtilities(conf: State) = {
    val configUtilities = computeCandidates(conf).map(c =>
      (c.centralVariableValue, if (conf.numberOfCollects == 0) computeUtility(c) else (computeUtility(c) + (c.numberOfCollects - 1) * c.memory(c.centralVariableValue)) / c.numberOfCollects)).toMap
    configUtilities
  }

  override def updateMemory(c: State): State = {
    val newMemory = computeExpectedUtilities(c)
    c.withUpdatedMemory(newMemory)
  }
}

