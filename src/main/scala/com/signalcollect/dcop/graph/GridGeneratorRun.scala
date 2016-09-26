/*
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

import java.io.FileWriter
import scala.math.Ordering.Boolean
import scala.util.Random

/**
 * The runAlgorithm() method calls the generate method for the grid.
 *
 * 1. Vertices have a maximum of 8 connections (diagonal included) and are 4-colorable
 *
 * 2. numberOfVertices will be width^2
 *
 * The Adopt file format conforms with http://teamcore.usc.edu/adopt/faq.txt
 */

case class GridGeneratorRun(
  width: Int,
  numberOfColors: Int,
  fileName: String,
  adoptGraphFormat: Boolean) extends Serializable {

  val numberOfVertices = width * width
  val edgeCounter = (width - 2) * (width - 2) * 8 + (width - 2) * 4 * 5 + 4 * 3
  val edgeDensity = math.floor((edgeCounter/2).toDouble / numberOfVertices * 100) / 100

  val v = new Array[Int](numberOfVertices)
  val e = new Array[List[Int]](numberOfVertices)

  def generate(): List[Map[String, String]] = {

    var finalResults = List[Map[String, String]]()
    println("Starting generating" + fileName)

    //Writing to file
    if (adoptGraphFormat) writeAdoptFormat() else writeStdFormat()
    finalResults
  }

  def writeAdoptFormat() = {
    val targetFile = new java.io.FileWriter(fileName)
    //Agents: AGENT agentId
    for (i <- 0 until numberOfVertices) {
      targetFile.write(s"AGENT ${i + 1}\n")
    }

    //Variables: VARIABLE variableId agentId domainSize
    for (i <- 0 until numberOfVertices) {
      targetFile.write(s"VARIABLE ${i} ${i + 1} $numberOfColors\n")
    }

    //Constraints: CONSTRAINT variableId1 variableId2 {opt: weight}
    //             NOGOOD value1 value2
    for (i <- 0 until numberOfVertices) {
      for (j <- computeNeighbours(i)) {
        if (i < j) { //No self edges
          targetFile.write(s"CONSTRAINT ${i} ${j}\n")
          for (col <- 0 until numberOfColors) {
            targetFile.write(s"NOGOOD ${col} ${col}\n")
          }
        }
      }
    }
    targetFile.close
    println
    println("Finished generating " + fileName + " in Adopt format.")
  }

  def writeStdFormat() = {
    val targetFile = new java.io.FileWriter(fileName)
    targetFile.write(numberOfVertices + " " + edgeCounter/2 + " " + edgeDensity + " " + numberOfColors + "\n")
    for (i <- 0 until numberOfVertices) {
      for (j <- computeNeighbours(i)) {
        if (i < j) { //No self edges, no double edges (will be added automatically by the reader).
          targetFile.write(i + " " + j + "\n")
        }
      }
    }
    targetFile.close
    println
    println("Finished generating " + fileName + "  in standard format.")
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
    column >= 0 && row >= 0 && column < width && row < width
  }

  def computeNeighbours(id: Int): Iterable[Int] = {
    val column: Int = id % width
    val row: Int = id / width
    potentialNeighbours(column, row).filter(coordinate => inGrid(coordinate._1, coordinate._2)) map
      (coordinate => (coordinate._2 * width + coordinate._1))
  }

}
