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
 * Target function implementations.
 * Should contain implementations for the functions: updateMemory and computeExpectedUtilities
 */

trait TargetFunction extends Algorithm {

  def computeCandidates(c: State): Set[State] = {
    for {
      assignment <- c.domain
    } yield c.withCentralVariableAssignment(assignment)
  }

  def computeExpectedUtilities(c: State): Map[Action, UtilityType]

  def updateMemory(c: State): State
}

trait MemoryLessTargetFunction extends TargetFunction {

  def computeExpectedUtilities(c: State): Map[Action, UtilityType] = {
    val configUtilities = computeCandidates(c).map(c => (c.centralVariableValue, computeUtility(c))).toMap
    configUtilities
  }

  override def updateMemory(c: State): State = c
}

trait AverageExpectedUtilityTargetFunction extends TargetFunction with StateWithMemory {

  def computeExpectedUtilities(conf: State) = {
    val configUtilities = computeCandidates(conf).map(c =>
      (c.centralVariableValue, if (conf.numberOfCollects == 0) computeUtility(c) else (computeUtility(c) + (c.numberOfCollects - 1) * c.memory(c.centralVariableValue)) / c.numberOfCollects)).toMap
    configUtilities
  }

  def updateMemory(c: State): State = {
    val newMemory = computeExpectedUtilities(c)
    c.withUpdatedMemory(newMemory)
  }
}

trait DiscountedAverageRegretsTargetFunction extends TargetFunction with StateWithMemory {

  def rho: Double

  /*
   * All regrets are minimum 0.
   */
  override def computeExpectedUtilities(conf: State) = {
    val configUtilities = computeCandidates(conf).map(c =>
      (c.centralVariableValue, math.max(0, rho * (computeUtility(c) - computeUtility(conf)) + (1 - rho) * c.memory(c.centralVariableValue)))).toMap
    configUtilities
  }
  
  def updateMemory(conf: State): State = {
    val newMemory = computeCandidates(conf).map(c =>
      (c.centralVariableValue, rho * (computeUtility(c) - computeUtility(conf)) + (1 - rho) * c.memory(c.centralVariableValue))).toMap
    conf.withUpdatedMemory(newMemory)
  }
  
}

