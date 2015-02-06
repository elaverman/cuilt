package com.signalcollect.dcop.evaluation

import com.signalcollect.dcop.graph._
import com.signalcollect.dcop.modules._
import com.signalcollect._
import java.util.Date
import com.signalcollect.dcop._
import com.signalcollect.dcop.graph._
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.interfaces.ModularAggregationOperation

/*
 * Returns true iff for each vertex,  there is no state with better expected utility than the current one.
 */
object LocalOptimumDetector extends ModularAggregationOperation[Boolean] with SignalCollectAlgorithmBridge{

  def extract(v: Vertex[_, _, _, _]) = v match {
    //TODO: Investigate the change here
    case v: DcopVertex => this.isInLocalOptimum(v.state)
    case other => throw new Exception("This detector can only handle Dcop vertices.")
  }

  override def aggregate(a: Boolean, b: Boolean) = a && b

  override val neutralElement = true

}

/*
 * Returns the number of vertices for which there is no state with better expected utility than the current one.
 */
object NumberOfLocalOptimaCounter extends ModularAggregationOperation[Long] with SignalCollectAlgorithmBridge{

  def extract(v: Vertex[_, _, _, _]) = v match {
    //TODO: Check next line
    case v: DcopVertex => if (this.isInLocalOptimum(v.state)) 1 else 0
    case other => throw new Exception("This detector can only handle Dcop vertices.")
  }

  override def aggregate(a: Long, b: Long) = a + b

  override val neutralElement = 0l

}

/*
 * Returns a set with all the actions chosen by each vertex.
 * Very expensive. Do NOT use on large graphs!
 */
object ActionDetector extends ModularAggregationOperation[Set[(Int, Int)]] with SignalCollectAlgorithmBridge {

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
 * Returns the total number of conflicts.
 */
object NumberOfConflictsCounter extends ModularAggregationOperation[Long] with SignalCollectAlgorithmBridge{

  def extract(v: Vertex[_, _, _, _]) = v match {
    //TODO: Check next line
    case v: DcopVertex => v.state.computeExpectedNumberOfConflicts
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
class DcopStatsGatherer extends GlobalTerminationDetection[Any, Any] {
  override def aggregationInterval = 1
  override def shouldTerminate(g: Graph[Any, Any]) = {
    //    val isInLocalOptimum = g.aggregate(LocalOptimumDetector)
    //    val numberOfConflicts = g.aggregate(NumberOfConflictsCounter)
    val (isInLocalOptimum, numberOfConflicts) = g.aggregate(MultiAggregator(LocalOptimumDetector, NumberOfConflictsCounter))

    false
  }
}

class DcopStatsGathererWithHistory(
  conflictsHistory: collection.mutable.Map[Int, Long],
  localOptimaHistory: collection.mutable.Map[Int, Long]) extends DcopStatsGatherer {
  var steps = 0
  override def shouldTerminate(g: Graph[Any, Any]) = {
    val numberOfConflicts = g.aggregate(NumberOfConflictsCounter)
    val numberOfVerticesInLocalOptima = g.aggregate(NumberOfLocalOptimaCounter)
    conflictsHistory += ((steps, numberOfConflicts))
    localOptimaHistory += ((steps, numberOfVerticesInLocalOptima))
    steps += 1
    false
  }
}

case class DcopAlgorithmRun(//[AgentId, Action, State, Config <: Configuration[AgentId, Action], UtilityType](
  optimizer: Algorithm,
  graphInstantiator: GridBuilder, //GraphInstantiator,
  maxUtility: Double,
  domainSize: Int,
  executionConfig: ExecutionConfiguration[Any, Any],
  runNumber: Int,
  aggregationInterval: Int,
  revision: String,
  evaluationDescription: String) extends SignalCollectAlgorithmBridge{

  def roundToMillisecondFraction(nanoseconds: Long): Double = {
    ((nanoseconds / 100000.0).round) / 10.0
  }

  def runAlgorithm(): List[Map[String, String]] = {
    
    val evaluationGraph = graphInstantiator.build()//Graph(GraphBuilder)
    
    println("Starting.")

    println(optimizer)

    var computeRanks = false

    println("Preparing Execution configuration.")
    println(executionConfig.executionMode)

    println(optimizer.toString)
    var finalResults = List[Map[String, String]]()

    var runResult = Map[String, String]()

    val date: Date = new Date
    val startTime = System.nanoTime()
    var extraStats = RunStats(None, maxUtility, None)

    val conflictsHistory = collection.mutable.Map.empty[Int, Long]
    val localOptimaHistory = collection.mutable.Map.empty[Int, Long]

    val extensiveTerminationDetector = new DcopStatsGathererWithHistory(conflictsHistory, localOptimaHistory)
    extensiveTerminationDetector.shouldTerminate(evaluationGraph)

    println(evaluationGraph)

    val usedExecutionConfig =
      if (aggregationInterval <= 0) {
        executionConfig
      } else {
        println("Gathering extensive stats")
        executionConfig.withGlobalTerminationDetection(extensiveTerminationDetector)
      }

    println("*Executing...")
    val stats = evaluationGraph.execute(usedExecutionConfig)

    //   stats.aggregatedWorkerStatistics.numberOfOutgoingEdges
    val finishTime = System.nanoTime
    val executionTime = roundToMillisecondFraction(finishTime - startTime)

    val conflictCount = evaluationGraph.mapReduce[DcopVertex, Long](
      { v => v.state.computeExpectedNumberOfConflicts },
      { case (t1, t2) => t1 + t2 },
      0)

    val numberOfLocalOptima = evaluationGraph.mapReduce[DcopVertex, Long](
      { v => if (this.isInLocalOptimum(v.state)) 1 else 0 },
      { case (t1, t2) => t1 + t2 },
      0)
      
    val isNe = evaluationGraph.mapReduce[DcopVertex, Boolean](
      { v => {this.isInLocalOptimum(v.state)}},
      { case (t1, t2) => t1 && t2 },
      true)

    val utility = (maxUtility - conflictCount * 2).toDouble

    val avgGlobalUtilityRatio = extraStats.avgGlobalVsOpt.getOrElse(-1)
    val endUtilityRatio = (maxUtility - conflictCount * 2).toDouble / maxUtility
    val isOptimal = if (conflictCount == 0) 1 else 0
    val timeToFirstLocOptimum = extraStats.timeToFirstLocOptimum.getOrElse(-1)
    // val messagesPerVertexPerStep = stats.aggregatedWorkerStatistics.signalMessagesReceived.toDouble / (evaluationGraph.size.toDouble * executionConfig.stepsLimit.getOrElse(1.toLong))
    runResult += s"evaluationDescription" -> evaluationDescription //
    runResult += s"optimizer" -> optimizer.toString //
    runResult += s"utility" -> utility.toString
    runResult += s"domainSize" -> domainSize.toString
    runResult += s"graphSize" -> stats.aggregatedWorkerStatistics.verticesAdded.toString //
    runResult += s"executionMode" -> executionConfig.executionMode.toString //
    runResult += s"conflictCount" -> conflictCount.toString //
    runResult += s"numberOfLocOptima" -> numberOfLocalOptima.toString //
    runResult += s"isNe" -> isNe.toString //
    runResult += s"avgGlobalUtilityRatio" -> avgGlobalUtilityRatio.toString // Measure (1)
    runResult += s"endUtilityRatio" -> endUtilityRatio.toString // Measure (2)
    runResult += s"isOptimal" -> isOptimal.toString // Measure (3)
    runResult += s"timeToFirstLocOptimum" -> timeToFirstLocOptimum.toString // Measure (4)
    // runResult += s"messagesPerVertexPerStep" -> messagesPerVertexPerStep.toString // Measure (5)
    runResult += s"revision" -> revision
    runResult += s"aggregationInterval" -> aggregationInterval.toString
    runResult += s"run" -> runNumber.toString
    runResult += s"stepsLimit" -> executionConfig.stepsLimit.toString
    runResult += s"timeLimit" -> executionConfig.timeLimit.toString
    runResult += s"graphStructure" -> evaluationGraph.toString //

    runResult += s"computationTimeInMilliseconds" -> executionTime.toString //
    runResult += s"date" -> date.toString //
    runResult += s"executionHostname" -> java.net.InetAddress.getLocalHost.getHostName //
    runResult += s"numberOfCollectSteps" -> stats.executionStatistics.collectSteps.toString //
    runResult += s"numberOfSignalSteps" -> stats.executionStatistics.signalSteps.toString //
    runResult += s"computationTime" -> stats.executionStatistics.computationTime.toString //
    runResult += s"terminationReason" -> stats.executionStatistics.terminationReason.toString //
    runResult += s"signalThreshold" -> executionConfig.signalThreshold.toString // 
    runResult += s"collectThreshold" -> executionConfig.collectThreshold.toString //

    //  println("\nNumber of conflicts at the end: " + ColorPrinter(evaluationGraph).countConflicts(evaluationGraph.graph.aggregate(idStateMapAggregator)))
    println("Shutting down.")
    evaluationGraph.shutdown

    runResult :: finalResults

  }
}

////TODO Ugly. Rewrite
//case class DcopMixedAlgorithmRun[AgentId, Action, UtilityType](optimizer1: Optimizer[AgentId, Action, Configuration[AgentId, Action], UtilityType], optimizer2: Optimizer[AgentId, Action, Configuration[AgentId, Action], UtilityType], proportion: Double, /*domain: Set[Int], */ evaluationGraph: EvaluationGraph[AgentId, Action], executionConfig: ExecutionConfiguration, runNumber: Int, aggregationInterval: Int, revision: String, evaluationDescription: String) {
//
//  def roundToMillisecondFraction(nanoseconds: Long): Double = {
//    ((nanoseconds / 100000.0).round) / 10.0
//  }
//
//  def runAlgorithm(): List[Map[String, String]] = {
//    println("*Starting.")
//
////    val evaluationGraph = evaluationGraphParameters match {
////      case gridParameters: GridParameters => throw new Error("MIXED Dimacs graph still unsupported.")
////      case adoptGraphParameters: AdoptGraphParameters => MixedAdoptGraph(optimizer1, optimizer2, proportion, adoptGraphParameters.adoptFileName, adoptGraphParameters.initialValue, adoptGraphParameters.debug)
////      case dimacsGraphParameters: DimacsGraphParameters => throw new Error("MIXED Dimacs graph still unsupported.")
////    }
//
//    println(optimizer1 + " " + optimizer2 + " " + proportion)
//
//    var computeRanks = false
//
//    //TODO Check if the matching is correctly done
//
//    optimizer1 match {
//      case rankedOptimizer: RankedOptimizer[AgentId, Action] =>
//        println("Ranked Optimizers")
//        computeRanks = true
//      case simpleOptimizer: SimpleOptimizer[AgentId, Action] =>
//        println("Simple Optimizers")
//        computeRanks = true //TODO Verify why this was set to true instead of false
//    }
//
//    //println("Preparing Execution configuration.")
//    //println(executionConfig.executionMode)
//
//    //TODO: Replace ${evaluationGraph.domainForVertex(1)} from the file names with something better.
//    val graphDirectoryFolder = new File("output/" + evaluationGraph.toString())
//    if (!graphDirectoryFolder.exists)
//      graphDirectoryFolder.mkdir
//    //    val outAnimation = new FileWriter(s"output/${evaluationGraph}/animation${optimizer1}${optimizer2}${proportion}${executionConfig.executionMode}${executionConfig.stepsLimit}Run$runNumber.txt")
//    val outConflicts = new FileWriter(s"output/${evaluationGraph}/conflicts${optimizer1}${optimizer2}${proportion}${executionConfig.executionMode}${executionConfig.stepsLimit}Run$runNumber.txt")
//    //    val outIndConflicts = new FileWriter(s"output/${evaluationGraph}/indConflicts${optimizer1}${optimizer2}${proportion}${executionConfig.executionMode}${executionConfig.stepsLimit}Run$runNumber.txt")
//    val outLocMinima = new FileWriter(s"output/${evaluationGraph}/locMinima${optimizer1}${optimizer2}${proportion}${executionConfig.executionMode}${executionConfig.stepsLimit}Run$runNumber.txt")
//    //    var outRanks: FileWriter = null
//
//    //println(optimizer.toString)
//    var finalResults = List[Map[String, String]]()
//
//    var runResult = Map[String, String]()
//
//    val date: Date = new Date
//    val startTime = System.nanoTime()
//    var extraStats = RunStats(None, evaluationGraph.maxUtility, None)
//
//    val terminationCondition = new ColorPrintingGlobalTerminationCondition(outConflicts, outLocMinima, extraStats, startTime, aggregationOperationParam = new IdStateMapAggregator[AgentId, Action], aggregationIntervalParam = aggregationInterval, evaluationGraph = evaluationGraph)
//    //    = if (!computeRanks)
//    //      //new ColorPrintingGlobalTerminationCondition(outAnimation, outConflicts, outIndConflicts, outLocMinima, extraStats, startTime, aggregationOperationParam = new IdStateMapAggregator[Int, Int], aggregationIntervalParam = aggregationInterval, evaluationGraph = evaluationGraph)
//    //      new ColorPrintingGlobalTerminationCondition(outConflicts, outLocMinima, extraStats, startTime, aggregationOperationParam = new IdStateMapAggregator[Int, Int], aggregationIntervalParam = aggregationInterval, evaluationGraph = evaluationGraph)
//    //    else {
//    //      outRanks = new java.io.FileWriter(s"output/${evaluationGraph}/ranks${optimizer1}${optimizer2}${proportion}${executionConfig.executionMode}${executionConfig.stepsLimit}${evaluationGraph.domainForVertex(1).size}Run$runNumber.txt")
//    //      //      new ColorRankPrintingGlobalTerminationCondition(outAnimation, outConflicts, Some(outRanks), outIndConflicts, outLocMinima, extraStats, startTime, aggregationOperationParam = new IdStateMapAggregator[Int, (Int, Double)], aggregationIntervalParam = aggregationInterval, evaluationGraph = evaluationGraph)
//    //      new ColorRankPrintingGlobalTerminationCondition(outConflicts, outLocMinima, extraStats, startTime, aggregationOperationParam = new IdStateMapAggregator[Int, (Int, Double)], aggregationIntervalParam = aggregationInterval, evaluationGraph = evaluationGraph)
//    //    }
//
//    val idStateMapAggregator = if (!computeRanks)
//      IdStateMapAggregator[AgentId, Int]
//    else {
//      IdStateMapAggregator[AgentId, (Int, Double)]
//    }
//
//    val initialAggregate = evaluationGraph.graph.aggregate(idStateMapAggregator)
//    println("*Initial aggregate")
//    println(initialAggregate.toMap.mkString(" "))
//
//    //    ColorPrinter(evaluationGraph).shouldTerminate(outAnimation, outConflicts, Some(outRanks), outIndConflicts, outLocMinima, extraStats, evaluationGraph.maxUtility)(initialAggregate)
//    ColorPrinter(evaluationGraph).shouldTerminate(outConflicts, outLocMinima, extraStats, evaluationGraph.maxUtility)(initialAggregate)
//
//    println("*Executing.")
//
//    val stats = evaluationGraph.graph.execute(executionConfig.withGlobalTerminationCondition(terminationCondition))
//
//    //   stats.aggregatedWorkerStatistics.numberOfOutgoingEdges
//    val finishTime = System.nanoTime
//    val executionTime = roundToMillisecondFraction(finishTime - startTime)
//
//    val conflictCount = ColorPrinter(evaluationGraph).countConflicts(evaluationGraph.graph.aggregate(idStateMapAggregator))
//
//    val utility = (evaluationGraph.maxUtility - conflictCount * 2).toDouble
//    val domainSize = evaluationGraph match {
//      case grid: Grid[_] => grid.domain.size //TODO Change these 2 lines when they will become supported.
//      case dimacsGraph: DimacsGraph[_] => dimacsGraph.domain.size
//      case other => -1
//    }
//
//    val avgGlobalUtilityRatio = extraStats.avgGlobalVsOpt.getOrElse(-1)
//    val endUtilityRatio = (evaluationGraph.maxUtility - conflictCount * 2).toDouble / evaluationGraph.maxUtility
//    val isOptimal = if (conflictCount == 0) 1 else 0
//    val timeToFirstLocOptimum = extraStats.timeToFirstLocOptimum.getOrElse(-1)
//    val messagesPerVertexPerStep = stats.aggregatedWorkerStatistics.signalMessagesReceived.toDouble / (evaluationGraph.size.toDouble * executionConfig.stepsLimit.getOrElse(1.toLong))
//    runResult += s"evaluationDescription" -> evaluationDescription //
//    runResult += s"optimizer" -> s"${optimizer1}${optimizer2}${proportion}" //
//    runResult += s"utility" -> utility.toString
//    runResult += s"domainSize" -> domainSize.toString
//    runResult += s"graphSize" -> evaluationGraph.size.toString //
//    runResult += s"executionMode" -> executionConfig.executionMode.toString //
//    runResult += s"conflictCount" -> conflictCount.toString //
//    runResult += s"avgGlobalUtilityRatio" -> avgGlobalUtilityRatio.toString // Measure (1)
//    runResult += s"endUtilityRatio" -> endUtilityRatio.toString // Measure (2)
//    runResult += s"isOptimal" -> isOptimal.toString // Measure (3)
//    runResult += s"timeToFirstLocOptimum" -> timeToFirstLocOptimum.toString // Measure (4)
//    runResult += s"messagesPerVertexPerStep" -> messagesPerVertexPerStep.toString // Measure (5)
//    runResult += s"isOptimizerRanked" -> computeRanks.toString
//    runResult += s"revision" -> revision
//    runResult += s"aggregationInterval" -> aggregationInterval.toString
//    runResult += s"run" -> runNumber.toString
//    runResult += s"stepsLimit" -> executionConfig.stepsLimit.toString
//    runResult += s"timeLimit" -> executionConfig.timeLimit.toString
//    runResult += s"graphStructure" -> evaluationGraph.toString //
//
//    runResult += s"computationTimeInMilliseconds" -> executionTime.toString //
//    runResult += s"date" -> date.toString //
//    runResult += s"executionHostname" -> java.net.InetAddress.getLocalHost.getHostName //
//
//    runResult += s"signalThreshold" -> executionConfig.signalThreshold.toString // 
//    runResult += s"collectThreshold" -> executionConfig.collectThreshold.toString //
//
//    // runResult += s"startTime" -> startTime.toString //
//    // runResult += s"endTime" -> finishTime.toString //
//
//    println("\nNumber of conflicts at the end: " + ColorPrinter(evaluationGraph).countConflicts(evaluationGraph.graph.aggregate(idStateMapAggregator)))
//    println("Shutting down.")
//    evaluationGraph.graph.shutdown
//
//    //    outAnimation.close
//    outConflicts.close
//    outLocMinima.close
//    //    if (outRanks != null)
//    //      outRanks.close
//    //    outIndConflicts.close
//
//    runResult :: finalResults
//
//  }
//}

case class RunStats(var avgGlobalVsOpt: Option[Double], optUtility: Double, var timeToFirstLocOptimum: Option[Int]){
  avgGlobalVsOpt = None
  timeToFirstLocOptimum = None
}
