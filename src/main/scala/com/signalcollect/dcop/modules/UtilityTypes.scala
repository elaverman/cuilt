package com.signalcollect.dcop.modules


/**
 * Utility function implementations.
 * Should contain implementations for the method computeUtility.
 */

trait VertexColoringUtility extends Algorithm {

  //type UtilityType = Double

  def computeUtility(c: State) = {
    val occupiedColors = c.neighborhood.values
    val numberOfConflicts = occupiedColors.filter(_ == c.centralVariableValue).size
    val numberOfNeighbors = occupiedColors.size
    val neighborsInSync = numberOfNeighbors - numberOfConflicts
    neighborsInSync
  }
}