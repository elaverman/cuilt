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

trait Execution extends SignalCollectAlgorithmBridge {

  def logger = false
  def isGrid = true

  /*
 * Returns true iff for each vertex, there is no state with better expected utility than the current one.
 * The values for the target function are computed with the potentially new action but the actions of the neighbours
 * received from the previous step.
 */
  object LocalOptimumDetector extends ModularAggregationOperation[Boolean] {

    def extract(v: Vertex[_, _, _, _]) = v match {
      //TODO: Investigate the change here
      case v: DcopVertex => {
        val res = v.lastSignalState match {
          case Some(oldState) => {
            //It's determining if at the last step it was in a LocalOptimum. Equivalent to if it was in a local optimum at this step before it changed action.
            isInLocalOptimum(v.state.withCentralVariableAssignment(oldState.centralVariableValue))
            //            if (v.state != v.lastSignalState.get)
            //              println("different: " + v.state + " old: " + v.lastSignalState)
          }
          case None => false
        }

        //        if (!res) {
        //          println("!!!" + v.id + "->" + v.state.centralVariableValue + ": " + v.state.neighborActions)
        //        } else println(v.id + "->" + v.state.centralVariableValue + ": " + v.state.neighborActions)
        res
      }
      case other => throw new Exception("This detector can only handle Dcop vertices.")
    }

    override def aggregate(a: Boolean, b: Boolean) = a && b

    override val neutralElement = true

  }

  /*
 * Returns the number of vertices for which there is no state with better expected utility than the current one.
 * The values for the target function are computed with the potentially new action but the actions of the neighbours
 * received from the previous step.
 */
  object NumberOfLocalOptimaCounter extends ModularAggregationOperation[Long] {

    def extract(v: Vertex[_, _, _, _]) = v match {
      //TODO: Check next line
      case v: DcopVertex => {
        v.lastSignalState match {
          case Some(oldState) => {
            //It's determining if at the last step it was in a LocalOptimum.
            if (isInLocalOptimum(v.state.withCentralVariableAssignment(oldState.centralVariableValue))) 1 else 0
          }
          case None => 0
        }
      }
      case other => throw new Exception("This detector can only handle Dcop vertices.")
    }

    override def aggregate(a: Long, b: Long) = a + b

    override val neutralElement = 0l

  }

  /*
 * Returns a set with all the actions chosen by each vertex.
 * Very expensive. Do NOT use on large graphs!
 */
  object ActionDetector extends ModularAggregationOperation[Set[(Int, Int)]] {

    def extract(v: Vertex[_, _, _, _]) = v match {
      case v: DcopVertex =>
        //TODO: Check next line
        val assignment = (v.state.agentId, v.state.centralVariableValue)
        assignment match {
          case (t1: Int, t2: Int) => Set((t1, t2))
          case other => throw new Exception("Can only handle vertices with int Ids and int Actions.")
        }
      case other => throw new Exception("This detector can only handle Dcop vertices.")
    }

    override def aggregate(a: Set[(Int, Int)], b: Set[(Int, Int)]) = a union b

    override val neutralElement = Set.empty[(Int, Int)]

  }

  /*
 * Returns the sum of conflicts that each agent has, given its potentially new state and the 
 * information that he has about his neighbors from the previous step.
 */
  object NumberOfConflictsCounter extends ModularAggregationOperation[Long] {
    def extract(v: Vertex[_, _, _, _]) = v match {
      //TODO: Check next line
      case v: DcopVertex => {
        v.lastSignalState match {
          case Some(oldState) => {
            //It's determining the number of conflicts at the last step.
            (v.state.withCentralVariableAssignment(oldState.centralVariableValue)).computeExpectedNumberOfConflicts
          }
          case None => 0
        }
      }
      case other => throw new Exception("This counter can only handle Dcop vertices.")
    }

    override def aggregate(a: Long, b: Long) = a + b

    override val neutralElement = 0l
  }

  /**
   *  GlobalTerminationDetection defines a shouldTerminate function that determines if a computation should terminate.
   *  Usually this will be determined by running an aggregation operation on the graph.
   *
   *  @param aggregationInterval In a synchronous computation: aggregation interval in computation steps.
   *  						   In an asynchronous computation: aggregation interval in milliseconds
   *  @param shouldTerminate Function that takes the graph as a parameter and returns true iff the computation should
   *               be terminated.
   */

  class PhoneyDcopStatsGatherer(agg: Int) extends GlobalTerminationDetection[Any, Any] {
    override def aggregationInterval = agg
    override def shouldTerminate(g: Graph[Any, Any]) = {
      false
    }
  }

  class DcopStatsGatherer(agg: Int) extends GlobalTerminationDetection[Any, Any] {
    override def aggregationInterval = agg
    var steps = 0
    override def shouldTerminate(g: Graph[Any, Any]) = {
      //    val isInLocalOptimum = g.aggregate(LocalOptimumDetector)
      //    val numberOfConflicts = g.aggregate(NumberOfConflictsCounter)
      val (isInLocalOptimum, numberOfConflicts) = g.aggregate(MultiAggregator(LocalOptimumDetector, NumberOfConflictsCounter))
      extraStats.updateAvgGlobal(numberOfConflicts, steps)
      //TODO verify
      if (isInLocalOptimum && steps > 0) {
        extraStats.updateTimeToFirstLocOptimum(steps)
      }
      steps += 1
      false
    }
  }

  class DcopStatsGathererWithHistory(
    conflictsHistory: collection.mutable.Map[Int, Long],
    localOptimaHistory: collection.mutable.Map[Int, Long],
    agg: Int) extends DcopStatsGatherer(agg) {

    //TODO fix code repetition.
    override def shouldTerminate(g: Graph[Any, Any]) = {
      val numberOfConflictsHistory = g.aggregate(NumberOfConflictsCounter)
      val numberOfVerticesInLocalOptima = g.aggregate(NumberOfLocalOptimaCounter)
      conflictsHistory += ((steps, numberOfConflictsHistory))
      localOptimaHistory += ((steps, numberOfVerticesInLocalOptima))
      val (isInLocOptimum, numberOfConflicts) = g.aggregate(MultiAggregator(LocalOptimumDetector, NumberOfConflictsCounter))
      extraStats.updateAvgGlobal(numberOfConflicts, steps)
      //TODO verify
      if (isInLocOptimum && steps > 0) {
        extraStats.updateTimeToFirstLocOptimum(steps)
      }

      println("Step " + steps + ", conflicts " + numberOfConflicts + ", localOptima " + numberOfVerticesInLocalOptima)
      val actions = g.aggregate(ActionDetector)

      var a: Array[Long] = new Array(actions.size)

      actions.foreach(x => a(x._1) = x._2)

      //      for (i <- 0 until a.size) {
      //        print(a(i) + " ")
      //      }
      //      println

      val width = math.floor(math.sqrt(a.size)).toInt

      // ---
      def info(c: State): String = {
        val expectedUtilities: Map[Action, Double] = computeExpectedUtilities(c)
        val normFactor = expectedUtilities.values.sum
        val selectionProb = Random.nextDouble

        var string = c.agentId + "INFO[" + isInLocalOptimum(c).toString + " regret: " + expectedUtilities + " " + c + "]" //" " + normFactor + " " + selectionProb + " "
        /*
        var partialSum: Double = 0.0
        for (action <- expectedUtilities.keys) {
          partialSum += expectedUtilities(action)
          if (selectionProb * normFactor <= partialSum) {
            string = string + action.toString + "]"
            return string
          }
        }
        */
        string
      }

      def stateOfVertex(id: Int) = g.forVertexWithId[Vertex[AgentId, State, Any, Any], State](id, x => x.state)

      if (logger == true && isGrid == true) {

        println("Step" + steps)

        //TODO put this under logging
        for (i <- 0 until width) {
          for (j <- 0 until width) {
            println(info(stateOfVertex(i * width + j)) + " ")
          }
        }
        println

        for (i <- 0 until width) {
          for (j <- 0 until width) {
            print(a(i * width + j) + " ")
          }
          println
        }
        println
      }

      steps += 1
      false
    }
  }

  case class RunStats(var avgGlobal: Option[Double], allSatisfiedUtility: Double, var timeToFirstLocOptimum: Option[Int]) {
    avgGlobal = None
    timeToFirstLocOptimum = None

    def updateAvgGlobal(globalConflicts: Double, step: Int) = {
      val globalUtility = allSatisfiedUtility - globalConflicts * 2
      avgGlobal = avgGlobal match {
        case None => Some(globalUtility)
        case Some(oldAvg) => {
          assert(step != 0)
          Some((oldAvg * (step - 1) + globalUtility) / step)
        }
      }
    }

    def updateTimeToFirstLocOptimum(step: Int) {
      timeToFirstLocOptimum = timeToFirstLocOptimum match {
        case None => Some(step)
        case Some(older) => Some(older)
      }
    }

  }

  var extraStats = RunStats(None, -1.0, None)

  case class DcopAlgorithmRun(
    graphInstantiator: GraphInstantiator,
    maxUtility: Double,
    domainSize: Int,
    executionConfig: ExecutionConfiguration[Any, Any],
    runNumber: Int,
    aggregationInterval: Int,
    fullHistoryStats: Boolean,
    revision: String,
    evaluationDescription: String) {

    def roundToMillisecondFraction(nanoseconds: Long): Double = {
      ((nanoseconds / 100000.0).round) / 10.0
    }

    def runAlgorithm(): List[Map[String, String]] = {

      val evaluationGraph = graphInstantiator.build(GraphBuilder)

      println("Graph is " + graphInstantiator)
      println("Starting.")

      var computeRanks = false

      println("Preparing Execution configuration.")
      println(executionConfig.executionMode)

      println(algorithmName)

      val numberOfEdges = evaluationGraph.mapReduce[DcopVertex, Long](
        { v => v.edgeCount },
        { case (t1, t2) => t1 + t2 },
        0)

      val allSatisfiedUtility = numberOfEdges
      extraStats = RunStats(None, allSatisfiedUtility, None)

      var finalResults = List[Map[String, String]]()
      var runResult = Map[String, String]()

      val conflictsHistory = collection.mutable.Map.empty[Int, Long]
      val localOptimaHistory = collection.mutable.Map.empty[Int, Long]

      val usedExecutionConfig =
        if (aggregationInterval <= 0) {
          println("No extensive stats will be gathered.")
          executionConfig
        } else {
          val extensiveTerminationDetector =
            if (fullHistoryStats) {
              println("Will gather extensive stats, including full history.")
              new DcopStatsGathererWithHistory(conflictsHistory, localOptimaHistory, aggregationInterval)
            } else {
              println("Will gather extensive stats, but not the history.")
              new DcopStatsGatherer(aggregationInterval)
            }
          extensiveTerminationDetector.shouldTerminate(evaluationGraph)
          executionConfig.withGlobalTerminationDetection(extensiveTerminationDetector)
        }

      println(s"*Execution started at time${System.nanoTime()}...")

      val date: Date = new Date
      val startTime = System.nanoTime()
      val stats = evaluationGraph.execute(usedExecutionConfig)

      val finishTime = System.nanoTime
      val executionTime = roundToMillisecondFraction(finishTime - startTime)

      val conflictCount = evaluationGraph.mapReduce[DcopVertex, Long](
        { v => v.state.computeExpectedNumberOfConflicts },
        { case (t1, t2) => t1 + t2 },
        0)

      val numberOfLocalOptima = evaluationGraph.mapReduce[DcopVertex, Long](
        { v => if (isInLocalOptimum(v.state)) 1 else 0 },
        { case (t1, t2) => t1 + t2 },
        0)

      val isNe = evaluationGraph.mapReduce[DcopVertex, Boolean](
        { v => { isInLocalOptimum(v.state) } },
        { case (t1, t2) => t1 && t2 },
        true)

      def precision(number: Double): Double = {
        math.floor(number * 10000) / 10000
      }

      val utility = (allSatisfiedUtility - conflictCount * 2).toDouble

      val avgGlobalUtilityRatio = extraStats.avgGlobal.getOrElse[Double](-1.0) / maxUtility
      //TODO: verify: number of edges = maxUtility for grid.
      val endUtilityRatio = (allSatisfiedUtility - conflictCount * 2).toDouble / maxUtility
      val isOptimal = (allSatisfiedUtility - conflictCount * 2 == maxUtility) //if (conflictCount == 0) 1 else 0
      val timeToFirstLocOptimum = extraStats.timeToFirstLocOptimum.getOrElse(-1)
      val graphSize = stats.aggregatedWorkerStatistics.verticesAdded
      val messagesPerVertexPerStep = stats.aggregatedWorkerStatistics.signalMessagesReceived.toDouble / (graphSize.toDouble * executionConfig.stepsLimit.getOrElse(1.toLong))
      runResult += s"evaluationDescription" -> evaluationDescription //
      runResult += s"optimizer" -> algorithmName //
      runResult += s"utility" -> utility.toString//.replace(".", ",")
      runResult += s"domainSize" -> domainSize.toString
      runResult += s"graphSize" -> graphSize.toString //
      runResult += s"numberOfEdges" -> numberOfEdges.toString //
      runResult += s"executionMode" -> executionConfig.executionMode.toString //
      runResult += s"conflictCount" -> conflictCount.toString //
      runResult += s"numberOfLocOptima" -> numberOfLocalOptima.toString //
      runResult += s"isNe" -> isNe.toString //
      runResult += s"avgGlobalUtilityRatio" -> precision(avgGlobalUtilityRatio).toString//.replace(".", ",") // Measure (1)
      runResult += s"endUtilityRatio" -> precision(endUtilityRatio).toString//.replace(".", ",") // Measure (2)
      runResult += s"isOptimal" -> isOptimal.toString // Measure (3)
      runResult += s"timeToFirstLocOptimum" -> timeToFirstLocOptimum.toString // Measure (4)
      runResult += s"messagesPerVertexPerStep" -> precision(messagesPerVertexPerStep).toString//.replace(".", ",") // Measure (5)
      runResult += s"revision" -> revision
      runResult += s"aggregationInterval" -> aggregationInterval.toString
      runResult += s"run" -> runNumber.toString
      runResult += s"stepsLimit" -> executionConfig.stepsLimit.toString
      runResult += s"timeLimit" -> executionConfig.timeLimit.toString
      runResult += s"graphStructure" -> graphInstantiator.toString //

      runResult += s"computationTimeInMilliseconds" -> executionTime.toString//.replace(".", ",") //
      runResult += s"date" -> date.toString //
      runResult += s"executionHostname" -> java.net.InetAddress.getLocalHost.getHostName //
      runResult += s"numberOfCollectSteps" -> stats.executionStatistics.collectSteps.toString //
      runResult += s"numberOfSignalSteps" -> stats.executionStatistics.signalSteps.toString //
      runResult += s"computationTime" -> stats.executionStatistics.computationTime.toString //
      runResult += s"terminationReason" -> stats.executionStatistics.terminationReason.toString //
      runResult += s"signalThreshold" -> executionConfig.signalThreshold.toString//.replace(".", ",") // 
      runResult += s"collectThreshold" -> executionConfig.collectThreshold.toString//.replace(".", ",") //

      var a: Array[Long] = new Array(conflictsHistory.size)
      conflictsHistory.foreach(x => { a(x._1) = x._2 })

      runResult += s"conflictsHistory" -> a.mkString(", ") //

      localOptimaHistory.foreach(x => { a(x._1) = x._2 })
      runResult += s"localOptimaHistory" -> a.mkString(", ")

      println("Shutting down.")
      evaluationGraph.shutdown

      runResult :: finalResults

    }
  }
}



