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
 * The runAlgorithm() method calls the generate method for the needed graphs.
 *
 * 1. All vertices are assigned a random color from the domain. (from 0 to the chromatic number-1)
 *
 * 2. Until we attain the desired number of edges (numberOfVertices * edgeDensity), we randomly create edges
 * between vertices with compatible colors (non-equal). An edge in Signal/Collect is directed, so we need to create two edges.
 *
 * 3. For every independent (unconnected) vertex we search for an edge between a future pair vertex and another vertex
 * that has at least one other neighbor.
 * A pair is a non-independent vertex of compatible color. We delete the old edge and create a new edge between the initial
 * vertex and the pair.
 *
 * The Adopt file format conforms with http://teamcore.usc.edu/adopt/faq.txt
 */

case class RandomGraphGeneratorRun(
  numberOfVertices: Int,
  edgeDensity: Int,
  numberOfColors: Int,
  fileName: String,
  adoptGraphFormat: Boolean,
  discardForInitialLonelyVertices: Boolean) extends Serializable {

  class GraphElements() {

    var edgeCounter = 0

    val v = new Array[Int](numberOfVertices)
    val e = new Array[List[Int]](numberOfVertices)

    val domain = (0 until numberOfColors).toSet
    def randomFromDomain = domain.toSeq(Random.nextInt(domain.size))

    for (i <- 0 until numberOfVertices) {
      v(i) = randomFromDomain
      e(i) = List()
    }

    def addAllEdges: Boolean = {
      var trialCounter = 0
      while ((edgeCounter < numberOfVertices * edgeDensity) && (trialCounter < numberOfVertices * edgeDensity)) {
        val src = Random.nextInt(numberOfVertices)
        val trg = Random.nextInt(numberOfVertices)
        println(src + " " + v(src) + " " + trg + " " + v(trg))
        if (src != trg && !e(src).contains(trg) && v(src) != v(trg)) {
          edgeCounter += 1
          trialCounter = 0
          e(src) = trg :: e(src)
          e(trg) = src :: e(trg)
        } else {
          trialCounter += 1
        }
      }
      (edgeCounter >= numberOfVertices * edgeDensity)
    }

    def bindMissingVertices: Boolean = {
      var ok = true

      for (i <- 0 until numberOfVertices) {
        if (e(i).isEmpty) {
          //Normally, done like this, but for very large sparse graphs, it has a high failure rate.
          var localok = false
          if (!discardForInitialLonelyVertices) {
            var counter = 0 //we give up if we can't find anything...
            //We look for another vertex with different color and we delete one of its edges
            while (!localok && counter < numberOfVertices * 2) {
              counter += 1
              val newPair = Random.nextInt(numberOfVertices)
              if (!e(newPair).isEmpty && v(newPair) != v(i)) {
                val pairsOfPair = e(newPair).toArray
                val pairOfPair = pairsOfPair(Random.nextInt(pairsOfPair.size))
                if (e(pairOfPair).size > 1) {
                  //remove old connection
                  e(pairOfPair) = e(pairOfPair).filter { x => x != newPair }
                  e(newPair) = e(newPair).filter { x => x != pairOfPair }
                  //add new connection
                  e(newPair) = i :: e(newPair)
                  e(i) = newPair :: e(i)
                  localok = true
                }
              }
            }
          }
          if (localok == false) println("Binding did not work.")
          ok = ok && localok
        }
      }
      ok
    }

    def verify = {
      //verification
      for (i <- 0 until numberOfVertices) {
        assert(e(i).nonEmpty, s"vertex without connections $i")
        for (j <- e(i)) {
          assert(v(i) != v(j), s"colors are the same for $i and $j")
          assert(Boolean.equiv(e(i).contains(j), e(j).contains(i)), s"edges are not bidirectional for $i and $j")
        }
      }
    }

  }

  def generate(): List[Map[String, String]] = {

    var ok = false
    var finalResults = List[Map[String, String]]()
    println("Starting generating" + fileName)

    while (!ok) {
      println("Try")
      val myGraph = new GraphElements()
      println("Adding edges")
      ok = myGraph.addAllEdges
      if (ok) {
        println("Binding")
        ok = myGraph.bindMissingVertices
        if (ok) {
          myGraph.verify
          //Writing to file
          if (adoptGraphFormat) writeAdoptFormat(myGraph) else writeStdFormat(myGraph)
        }
      }
    }
    println("Ended loop with ok=" + ok)
    finalResults
  }

  def writeAdoptFormat(myGraph: GraphElements) = {
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
      for (j <- myGraph.e(i)) {
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

  def writeStdFormat(myGraph: GraphElements) = {
    val targetFile = new java.io.FileWriter(fileName)
    targetFile.write(numberOfVertices + " " + myGraph.edgeCounter + " " + edgeDensity + " " + numberOfColors + "\n")
    for (i <- 0 until numberOfVertices) {
      for (j <- myGraph.e(i)) {
        if (i < j) { //No self edges
          targetFile.write(i + " " + j + "\n")
        }
      }
    }
    targetFile.close
    println
    println("Finished generating " + fileName + "  in standard format.")
  }

}
