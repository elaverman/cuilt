package com.signalcollect.dcop.modules


/**
 * Utility function implementations.
 * Should contain implementations for the method computeUtility.
 */

trait Utility extends Algorithm {

  def computeUtility(c: State): UtilityType
}


trait VertexColoringUtility extends Algorithm {

  type UtilityType = Double

  def computeUtility(c: State) = {
    val occupiedColors = c.neighborActions.values
    val numberOfConflicts = occupiedColors.filter(_ == c.centralVariableValue).size
    val numberOfNeighbors = occupiedColors.size
    val neighborsInSync = numberOfNeighbors - numberOfConflicts
    neighborsInSync
  }
}