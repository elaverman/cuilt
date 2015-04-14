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

  def computeExpectedUtilityForStateValue(c: State): (Action, UtilityType) = {
    (c.centralVariableValue, computeUtility(c))
  }

  def computeExpectedUtilities(c: State): Map[Action, UtilityType] = {
    val configUtilities = computeCandidates(c).map(computeExpectedUtilityForStateValue(_)).toMap
    configUtilities
  }

  override def updateMemory(c: State): State = c
}

/*
 * Used in JSFP-I
 */
trait AverageExpectedUtilityTargetFunction extends MemoryLessTargetFunction with StateWithMemory {

  override def computeExpectedUtilityForStateValue(conf: State): (Action, UtilityType) = {
    (conf.centralVariableValue, conf.memory(conf.centralVariableValue))
  }

  def computeMemory(state: State): Map[Action, UtilityType] = {
    val memory = computeCandidates(state).map(candidate => {
      val newMemoryForCandidate = if (state.numberOfCollects == 0) {
        computeUtility(candidate)
      } else { //the memory and number of collects should be the same for the candidate and for the state
        (computeUtility(candidate) + (state.numberOfCollects - 1) * state.memory(candidate.centralVariableValue)) / state.numberOfCollects
      }
      (candidate.centralVariableValue, newMemoryForCandidate)
    }).toMap

    memory
  }

  override def updateMemory(c: State): State = {
    val newMemory = computeMemory(c)
    c.withUpdatedMemory(newMemory)
  }
}

/*
 * Used in Fading memory JSFP-I
 */
trait WeightedExpectedUtilityTargetFunction extends AverageExpectedUtilityTargetFunction {

  def rho: Double

  override def computeMemory(state: State): Map[Action, UtilityType] = {
    val memory = computeCandidates(state).map(candidate => {
      val newMemoryForCandidate = if (state.numberOfCollects == 0) {
        computeUtility(candidate)
      } else { //the memory should be the same for the candidate and for the state
        rho * computeUtility(candidate) + (1 - rho) * state.memory(candidate.centralVariableValue)
      }
      (candidate.centralVariableValue, newMemoryForCandidate)
    }).toMap
    memory
  }

}

/*
 * Used in RM
 */
trait AverageRegretsTargetFunction extends MemoryLessTargetFunction with StateWithMemory {

  //takes max out of the 0 and expectedUtility
  override def computeExpectedUtilityForStateValue(conf: State): (Action, UtilityType) = {
    (conf.centralVariableValue, math.max(0, conf.memory(conf.centralVariableValue)))
  }

  def computeMemory(state: State): Map[Action, UtilityType] = {
    val memory = computeCandidates(state).map(candidate => {
      val newMemoryForCandidate = if (state.numberOfCollects == 0) {
        computeUtility(candidate) - computeUtility(state)
      } else { //the memory and number of collects should be the same for the candidate and for the state
        (computeUtility(candidate) - computeUtility(state) + (state.numberOfCollects - 1) * state.memory(candidate.centralVariableValue)) / state.numberOfCollects
      }
      (candidate.centralVariableValue, newMemoryForCandidate)
    }).toMap
    memory
  }

  override def updateMemory(conf: State): State = {
    val newMemory = computeMemory(conf)
    conf.withUpdatedMemory(newMemory)
  }

}

/*
 * Used in WRM-I
 */
trait DiscountedAverageRegretsTargetFunction extends AverageRegretsTargetFunction {

  def rho: Double

  override def computeMemory(state: State): Map[Action, UtilityType] = {
    val memory = computeCandidates(state).map(candidate => {
      val newMemoryForCandidate = if (state.numberOfCollects == 0) {
        computeUtility(candidate) - computeUtility(state)
      } else { //the memory should be the same for the candidate and for the state
        rho * (computeUtility(candidate) - computeUtility(state)) + (1 - rho) * state.memory(candidate.centralVariableValue)
      }
      (candidate.centralVariableValue, newMemoryForCandidate)
    }).toMap
    memory
  }

}

