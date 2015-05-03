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
 * Termination rule implementations.
 * Should contain implementations for the shouldTerminate method.
 */

trait TerminationRule extends Algorithm {

  def shouldTerminate(c: State): Boolean

}

trait NashEquilibriumConvergence extends TerminationRule {

  def shouldTerminate(c: State): Boolean = isInLocalOptimum(c)

}

trait SimulatedAnnealingConvergence extends TerminationRule with StateWithMemory {

  def negDeltaMax: Double
  def etaInverse(i: Long): Double

  def shouldTerminate(c: State): Boolean = {
    val shouldTerminateVal = isInLocalOptimum(c) && (scala.math.exp(negDeltaMax * etaInverse(c.numberOfCollects)) < 0.001)
    //    println("Iteration in shouldTerminate:" + c.agentId + "-" + shouldTerminateVal + "-" + isInLocalOptimum(c))
    shouldTerminateVal
  }

}

trait DistributionConvergence extends TerminationRule with ExtendedMemoryState {

  def shouldTerminate(c: State): Boolean = {

    val shouldTerminateVal = /*isInLocalOptimum(c) &&*/ c.memoryConverged
    
//      println("Iteration in shouldTerminate:" + c.agentId + "-" + shouldTerminateVal + "-" + isInLocalOptimum(c))
//    if (shouldTerminateVal == false)
//      println("Iteration in shouldTerminate:" + c.agentId + "-" + shouldTerminateVal + "-" + c.memory)
    shouldTerminateVal
  }

}
