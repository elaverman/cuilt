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

package com.signalcollect.dcop.graph

import scala.util.Random
import com.signalcollect.dcop.modules._
import com.signalcollect._
import com.signalcollect.dcop.graph._
import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.dcop.modules.SignalCollectAlgorithmBridge


trait GraphInstantiator extends Serializable {
  def build(graphBuilder: GraphBuilder[Any, Any]): Graph[Any, Any]
}

//TODO: Decouple the functions for building vertices/edges from the algorithm. Add pluggable function for action initialization.
class GridInstantiator(myAlgo: IntAlgorithm, gridWidth: Int, domain: Set[Int]) extends GraphInstantiator {

  def build(graphBuilder: GraphBuilder[Any, Any] = GraphBuilder): Graph[Any, Any] = {

    def randomFromDomain = domain.toSeq(Random.nextInt(domain.size))

    val g = graphBuilder.build
    
    //Add vertices
    for (i <- (0 until gridWidth * gridWidth)) {
      g.addVertex(myAlgo.createVertex(i, randomFromDomain, domain))
    }

    //Add edges
    for (i <- 0 until gridWidth * gridWidth) {
      for (n <- computeNeighbours(i)) {
        g.addEdge(i, myAlgo.createEdge(targetId = n))
      }
    }

    g
  }

  // Returns all the neighboring cells of the cell with the given row/column
  def potentialNeighbours(column: Int, row: Int): List[(Int, Int)] = {
    List(
      (column - 1, row - 1), (column, row - 1), (column + 1, row - 1),
      (column - 1, row), (column + 1, row),
      (column - 1, row + 1), (column, row + 1), (column + 1, row + 1))
  }

  // Tests if a cell is within the grid boundaries
  def inGrid(column: Int, row: Int): Boolean = {
    column >= 0 && row >= 0 && column < gridWidth && row < gridWidth
  }

  def computeNeighbours(id: Int): Iterable[Int] = {
    val column: Int = id % gridWidth
    val row: Int = id / gridWidth
    potentialNeighbours(column, row).filter(coordinate => inGrid(coordinate._1, coordinate._2)) map
      (coordinate => (coordinate._2 * gridWidth + coordinate._1))
  }

  def size = gridWidth * gridWidth

  def maxUtility = (gridWidth - 2) * (gridWidth - 2) * 8 + (gridWidth - 2) * 20 + 12

  override def toString = "Grid" + size.toString

}