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
 * Utility function implementations.
 * Should contain implementations for the method computeUtility.
 * 
 * For the moment, the only UtilityType that is used is Double, defined on the Algorithm trait.
 */

trait Utility extends Algorithm { 

  def computeUtility(c: State): UtilityType
}


trait VertexColoringUtility extends Algorithm {

  def computeUtility(c: State) = {
    val occupiedColors = c.neighborActions.values
    val numberOfConflicts = occupiedColors.filter(_ == c.centralVariableValue).size
    val numberOfNeighbors = occupiedColors.size
    val neighborsInSync = numberOfNeighbors - numberOfConflicts
    neighborsInSync
  }
}
