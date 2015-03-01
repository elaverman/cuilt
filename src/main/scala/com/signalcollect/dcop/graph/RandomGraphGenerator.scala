package com.signalcollect.dcop.graph

import java.io.FileWriter
import scala.math.Ordering.Boolean
import scala.util.Random

/*
1. A chromatic number is chosen, and then each vertex of the graph is given a random colour label, 
with the number of colours being the chromatic number.  
The results in the JAAMAS paper were for graphs with chromatic numbers from 3 to 5.

2. Links between nodes are proposed at random, and are accepted if the two connecting 
nodes are of different colours.  This step was iterated until an average edge density threshold 
was passed, and in the paper I have stated that this value was 3 
- I trust that is correct -but I had evaulated a range from 2 up to 8 or 10, 
where the very dense graphs caused the algorithms I was testing to run very slowly (there were many local optima).

3. Each graph was checked to see if it contained any independent vertices (unconnected nodes), 
and those that did were discarded.

Steps 2 and 3 were repeated until enough graphs were generated. 
This produced a batch of graphs with known upper bounds on their chromatic number.
*/

case class GraphGeneratorRun() extends Serializable {

  def runAlgorithm(): List[Map[String, String]] = {

    println("Starting.")

    var finalResults = List[Map[String, String]]()

    val numbersOfVertices = Set(10, 100, 1000, 10000, 100000, 1000000, 10000000)
    val edgeDensities = Set(3)
    val numbersOfColors = Set(5)
    val numberOfGraphs = 3

    for (i <- 0 until numberOfGraphs) {
      for (numberOfVertices <- numbersOfVertices) {
        for (edgeDensity <- edgeDensities) {
          for (numberOfColors <- numbersOfColors) {
            generate(numberOfVertices, edgeDensity, numberOfColors, s"inputGraphs/V${numberOfVertices}_ED${edgeDensity}_Col${numberOfColors}_$i.txt")
          }
        }
      }
    }

    finalResults

  }

  def generate(numberOfVertices: Int, edgeDensity: Int, numberOfColors: Int, fileName: String) = {
    var ok = false
    println("Starting generating" + fileName)
    while (!ok) {
      print(".")
      val v = new Array[Int](numberOfVertices)
      val e = new Array[List[Int]](numberOfVertices)

      val domain = (0 until numberOfColors).toSet
      def randomFromDomain = domain.toSeq(Random.nextInt(domain.size))

      for (i <- 0 until numberOfVertices) {
        v(i) = randomFromDomain
        e(i) = List()
      }

      var edgeCounter = 0

      while (edgeCounter < numberOfVertices * edgeDensity) {
        val src = Random.nextInt(numberOfVertices)
        val trg = Random.nextInt(numberOfVertices)
        //println(src+" "+v(src)+" "+trg+" "+v(trg))
        if (src != trg && !e(src).contains(trg) && v(src) != v(trg)) {
          edgeCounter += 2
          e(src) = trg :: e(src)
          e(trg) = src :: e(trg)
        }
      }

      ok = true

      println("binding phase")
      for (i <- 0 until numberOfVertices) {
        if (e(i).isEmpty) {
          //Normally, done like this, but for very large sparse graphs, it has a high failure rate.
          ok = false

          var counter = 0 //we give up if we can't find anything...
          //We look for another vertex with different color and we delete one of its edges
          while (!ok && counter < numberOfVertices * 2) {
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
                ok = true
              }
            }
          }
        }
      }

      if (ok) {

        //verification
        for (i <- 0 until numberOfVertices) {
          assert(e(i).nonEmpty, s"vertex without connections $i")
          for (j <- e(i)) {
            assert(v(i) != v(j), s"colors are the same for $i and $j")
            assert(Boolean.equiv(e(i).contains(j), e(j).contains(i)), s"edges are not bidirectional for $i and $j")
          }
        }

        val targetFile = new java.io.FileWriter(fileName)
        targetFile.write(numberOfVertices + " " + edgeCounter + " " + edgeDensity + " " + numberOfColors + "\n")
        for (i <- 0 until numberOfVertices) {
          for (j <- e(i)) {
            targetFile.write(i + " " + j + "\n")
          }
        }
        targetFile.close
        println
        println("Finished generating " + fileName)

      }

    }
  }
}
