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

package com.signalcollect.dcop.evaluation

import com.signalcollect.dcop.modules._
import com.signalcollect._
import java.util.Date
import com.signalcollect.dcop._
import com.signalcollect.dcop.graph._
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.interfaces.ModularAggregationOperation
import scala.util.Random
import com.signalcollect.dcop.modules.SignalCollectAlgorithmBridge
import com.signalcollect.dcop.custom.EventUtility
import com.signalcollect.dcop.custom.SlotUtility
import com.signalcollect.dcop.modules.SimpleState

case class SchedulingAlgorithmRun(
  graphInstantiator: GraphInstantiator,
  eventAlgo: IntAlgorithm with EventUtility,
  slotAlgo: IntAlgorithm with SlotUtility,
  domainSize: Int,
  executionConfig: ExecutionConfiguration[Any, Any],
  runNumber: Int,
  aggregationInterval: Int,
  fullHistoryStats: Boolean,
  resultsWithComma: Boolean = false,
  revision: String,
  evaluationDescription: String) {

  def roundToMillisecondFraction(nanoseconds: Long): Double = {
    ((nanoseconds / 100000.0).round) / 10.0
  }

  def runAlgorithm(): List[Map[String, String]] = {

    val evaluationGraph = graphInstantiator.build(GraphBuilder)

    println("Graph is " + graphInstantiator)
    println("Starting.")

    val numberOfVertices = evaluationGraph.mapReduce[Vertex[_, _, _, _], Long](
      { v => 1 },
      { case (t1, t2) => t1 + t2 },
      0)
    println(numberOfVertices)
    var computeRanks = false

    println("Preparing Execution configuration.")
    println(executionConfig.executionMode)

    val algorithmName = eventAlgo.algorithmName + ";" + slotAlgo.algorithmName

    println(algorithmName)

    val numberOfEdges = evaluationGraph.mapReduce[Vertex[_, _, _, _], Long](
      { v => v.edgeCount },
      { case (t1, t2) => t1 + t2 },
      0)

    val allSatisfiedUtility = graphInstantiator.maxUtility

    var finalResults = List[Map[String, String]]()
    var runResult = Map[String, String]()

    val conflictsHistory = collection.mutable.Map.empty[Int, Long]
    val localOptimaHistory = collection.mutable.Map.empty[Int, Long]

    val usedExecutionConfig =
      executionConfig

    println(s"*Execution started at time${System.nanoTime()}...")

    val date: Date = new Date
    val startTime = System.nanoTime()
    val stats = evaluationGraph.execute(usedExecutionConfig)

    val finishTime = System.nanoTime
    val executionTime = roundToMillisecondFraction(finishTime - startTime)

    val numberOfAfterVertices = evaluationGraph.mapReduce[Vertex[_, _, _, _], Long](
      { v => { println(v.state); 1 } },
      { case (t1, t2) => t1 + t2 },
      0)
    println(numberOfAfterVertices)

// TODO: Move computeExpectedNumberOfConflicts from the state, on the Utility function    
//    val conflictCount = evaluationGraph.mapReduce[Vertex[_, _, _, _], Long](
//      { v =>
//        v.state match {
//          case x: eventAlgo.State => x.computeExpectedNumberOfConflicts
//          case y: slotAlgo.State => y.computeExpectedNumberOfConflicts
//        }
//      },
//      { case (t1, t2) => t1 + t2 },
//      0)

    println("local optima")
    val numberOfLocalOptima = evaluationGraph.mapReduce[Vertex[_, _, _, _], Long](
      { v =>
        v.state match {
          case x: eventAlgo.State => if (eventAlgo.isInLocalOptimum(x)) 1 else 0
          case y: slotAlgo.State => if (slotAlgo.isInLocalOptimum(y)) 1 else 0
        }
      },
      { case (t1, t2) => t1 + t2 },
      0)

    val isNe = evaluationGraph.mapReduce[Vertex[_, _, _, _], Boolean](
      { v =>
        v.state match {
          case x: eventAlgo.State => eventAlgo.isInLocalOptimum(x)
          case y: slotAlgo.State => slotAlgo.isInLocalOptimum(y)
        }
      },
      { case (t1, t2) => t1 && t2 },
      true)

    def precision(number: Double): Double = {
      math.floor(number * 10000) / 10000
    }

    val utility =  evaluationGraph.mapReduce[Vertex[_, _, _, _], Double](
      { v =>
        v.state match {
          case x: eventAlgo.State => eventAlgo.computeUtility(x)
          case y: slotAlgo.State => slotAlgo.computeUtility(y)
        }
      },
      { case (t1, t2) => t1 + t2 },
      0)
      
    val avgGlobalUtilityRatio = -1
    val endUtilityRatio = utility / allSatisfiedUtility
    val isOptimal = (utility == allSatisfiedUtility) //if (conflictCount == 0) 1 else 0
    val timeToFirstLocOptimum = (-1)
    val graphSize = stats.aggregatedWorkerStatistics.verticesAdded
    val messagesPerVertexPerStep = stats.aggregatedWorkerStatistics.signalMessagesReceived.toDouble / (graphSize.toDouble * executionConfig.stepsLimit.getOrElse(1.toLong))
    runResult += s"evaluationDescription" -> evaluationDescription //
    runResult += s"optimizer" -> algorithmName //
    runResult += s"utility" -> { if (resultsWithComma) utility.toString.replace(".", ",") else utility.toString }
    runResult += s"domainSize" -> domainSize.toString
    runResult += s"graphSize" -> graphSize.toString //
    runResult += s"numberOfEdges" -> numberOfEdges.toString //
    runResult += s"executionMode" -> executionConfig.executionMode.toString //
    runResult += s"conflictCount" -> "-1" //conflictCount.toString //
    runResult += s"numberOfLocOptima" -> numberOfLocalOptima.toString //
    runResult += s"isNe" -> isNe.toString //
    runResult += s"avgGlobalUtilityRatio" -> { if (resultsWithComma) precision(avgGlobalUtilityRatio).toString.replace(".", ",") else precision(avgGlobalUtilityRatio).toString } // Measure (1)
    runResult += s"endUtilityRatio" -> { if (resultsWithComma) precision(endUtilityRatio).toString.replace(".", ",") else precision(endUtilityRatio).toString } // Measure (2)
    runResult += s"isOptimal" -> isOptimal.toString // Measure (3)
    runResult += s"timeToFirstLocOptimum" -> timeToFirstLocOptimum.toString // Measure (4)
    runResult += s"messagesPerVertexPerStep" -> { if (resultsWithComma) precision(messagesPerVertexPerStep).toString.replace(".", ",") else precision(messagesPerVertexPerStep).toString } // Measure (5)
    runResult += s"revision" -> revision
    runResult += s"aggregationInterval" -> aggregationInterval.toString
    runResult += s"run" -> runNumber.toString
    runResult += s"stepsLimit" -> executionConfig.stepsLimit.toString
    runResult += s"timeLimit" -> executionConfig.timeLimit.toString
    runResult += s"graphStructure" -> graphInstantiator.toString //

    runResult += s"computationTimeInMilliseconds" -> { if (resultsWithComma) executionTime.toString.replace(".", ",") else executionTime.toString }
    runResult += s"date" -> date.toString //
    runResult += s"executionHostname" -> java.net.InetAddress.getLocalHost.getHostName //
    runResult += s"numberOfCollectSteps" -> stats.executionStatistics.collectSteps.toString //
    runResult += s"numberOfSignalSteps" -> stats.executionStatistics.signalSteps.toString //
    runResult += s"computationTime" -> stats.executionStatistics.computationTime.toString //
    runResult += s"terminationReason" -> stats.executionStatistics.terminationReason.toString //
    runResult += s"signalThreshold" -> { if (resultsWithComma) executionConfig.signalThreshold.toString.replace(".", ",") else executionConfig.signalThreshold.toString }
    runResult += s"collectThreshold" -> { if (resultsWithComma) executionConfig.collectThreshold.toString.replace(".", ",") else executionConfig.collectThreshold.toString }

    var a: Array[Long] = new Array(conflictsHistory.size)
    conflictsHistory.foreach(x => { a(x._1) = x._2 })

    runResult += s"conflictsHistory" -> a.mkString(", ") //

    localOptimaHistory.foreach(x => { a(x._1) = x._2 })
    runResult += s"localOptimaHistory" -> a.mkString(", ")

    println("Shutting down.")
    evaluationGraph.shutdown
    println("Shut down.")
    runResult :: finalResults
  }
}



