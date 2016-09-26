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

import scala.util.Random
import com.signalcollect.dcop.modules._
import com.signalcollect._
import com.signalcollect.dcop.graph._
import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.dcop.modules.SignalCollectAlgorithmBridge
import java.io.BufferedReader
import java.io.FileReader

//TODO: Decouple the functions for building vertices/edges from the algorithm. Add pluggable function for action initialization.
class GridReader(myAlgo: IntAlgorithm, width: Int, numberOfColors: Int) extends GraphInstantiator {

  def build(graphBuilder: GraphBuilder[Any, Any] = GraphBuilder): Graph[Any, Any] = {
    val numberOfVertices = width * width
    val edgeCounter = (width - 2) * (width - 2) * 8 + (width - 2) * 4 * 5 + 4 * 3
    val edgeDensity = edgeCounter / (2.0 * numberOfVertices)
    println(edgeCounter + " " + numberOfVertices + " " + edgeDensity)

    val edgeDensityString = if (edgeDensity == edgeDensity.toInt) {
      edgeDensity.toInt.toString
    } else { ((edgeDensity * 100).toInt / 100.0).toString }

    val inputFileName: String = s"inputGraphsCP/V${numberOfVertices}_ED${edgeDensityString}_Col${numberOfColors}_Grid.txt"
    val sourceFile = new java.io.File(inputFileName)
    val br = new BufferedReader(new FileReader(sourceFile));

    val Array(contentNumberOfVertices, contentNumberOfEdges, contentEdgeDensity, contentNumberOfColors) = br.readLine().split(" ")

    assert(contentNumberOfVertices.toInt == numberOfVertices, "Number of vertices from file name and file differ")
    assert(contentEdgeDensity == edgeDensityString, "Edge densities from file name and file differ")
    assert(contentNumberOfColors.toInt == numberOfColors, "Number of colors from file name and file differ")

    val domain = (0 until numberOfColors).toSet

    def randomFromDomain = domain.toSeq(Random.nextInt(domain.size))

    val g = graphBuilder.build

    //Add vertices
    for (i <- (0 until numberOfVertices)) {
      g.addVertex(myAlgo.createVertex(i, randomFromDomain, domain))
    }

    //Add edges
    for (i <- 0 until contentNumberOfEdges.toInt) {
      val Array(src, trg) = br.readLine().split(" ").map(_.toInt)
      g.addEdge(src, myAlgo.createEdge(targetId = trg))
      g.addEdge(trg, myAlgo.createEdge(targetId = src))
    }

    br.close()
    g
  }

  def size = width * width

  def maxUtility = (width - 2) * (width - 2) * 8 + (width - 2) * 20 + 12

  override def toString = "Grid" + size.toString

}