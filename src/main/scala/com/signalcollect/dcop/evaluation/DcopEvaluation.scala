package com.signalcollect.dcop.evaluation

import java.io.File
import scala.io.Source
import scala.util.Random
import com.signalcollect.Edge
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.nodeprovisioning.torque.LocalHost
import com.signalcollect.nodeprovisioning.torque.TorqueHost
import com.signalcollect.nodeprovisioning.torque.TorqueJobSubmitter
import com.signalcollect.nodeprovisioning.torque.TorquePriority
import com.signalcollect.dcop.graph._
import com.signalcollect.dcop.modules._

object DcopEvaluation extends App {

  def jvmParameters = " -Xmx10240m" +
    // " -Xms512m" +
    " -XX:+AggressiveOpts" +
    " -XX:+AlwaysPreTouch" +
    " -XX:+UseNUMA" +
    " -XX:-UseBiasedLocking" +
    " -XX:MaxInlineSize=1024"

  def assemblyPath = "./target/scala-2.11/dcop-algorithms-evaluation-assembly-1.0-SNAPSHOT.jar"
  val assemblyFile = new File(assemblyPath)
  val kraken = new TorqueHost(
    jobSubmitter = new TorqueJobSubmitter(username = System.getProperty("user.name"), hostname = "kraken.ifi.uzh.ch"),
    coresPerNode = 23,
    localJarPath = assemblyPath, jvmParameters = jvmParameters, jdkBinPath = "/home/user/verman/jdk1.7.0_45/bin/", priority = TorquePriority.superfast)
  //  val gru = new SlurmHost(
  //    jobSubmitter = new SlurmJobSubmitter(username = System.getProperty("user.name"), hostname = "gru.ifi.uzh.ch"),
  //    coresPerNode = 10,
  //    localJarPath = assemblyPath, jvmParameters = jvmParameters, jdkBinPath = "/home/user/verman/jdk1.7.0_45/bin/")
  val localHost = new LocalHost
  val googleDocs = new GoogleDocsResultHandler(args(0), args(1), "evaluationEUMAS", "data")
  // val mySql = new MySqlResultHandler(args(2), args(3), args(4))

  def getRevision: String = {
    try {
      val gitLogPath = ".git/logs/HEAD"
      val gitLog = new File(gitLogPath)
      val lines = Source.fromFile(gitLogPath).getLines
      val lastLine = lines.toList.last
      val revision = lastLine.split(" ")(1)
      revision
    } catch {
      case t: Throwable => "Unknown revision."
    }
  }

  def randomFromDomain(domain: Set[Int]) = domain.toSeq(Random.nextInt(domain.size))
  def zeroInitialized(domain: Set[Int]) = 0
  val debug = false

  /*********/
  def evalName = s"sample localhost"
  def evalNumber = 5
  def runs = 1
  def pure = true
//  var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = kraken).addResultHandler(googleDocs) //.addResultHandler(mySql)
  //  var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = gru) //.addResultHandler(mySql)
    var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = localHost)//.addResultHandler(googleDocs) //.addResultHandler(mySql)
  /*********/

  //TODO Why do we have the probl since we SimpleConfig used by DsaAVC extends Configuration??
  val simpleOptimizers: List[IntAlgorithm] = List(
    VertexColoringAlgorithm)

  //  val adoptGraphNamesList = new java.io.File("adoptInput").listFiles.filter(x => (x.getName.startsWith("Problem-GraphColor-40_3_"))).map(_.getName)
  //  val dimacsGraphNamesList = new java.io.File("dimacsInput").listFiles.filter(x => (x.getName.endsWith("flat1000_76_0.col"))).map(_.getName)

  def initial0Value = 0

//  def simpleDcopVertexCreator(
//    optimizer: Optimizer[Int, Int, SimpleConfig[Int, Int], Double],
//    domainSize: Int,
//    debug: Boolean = false): Int => SimpleDcopVertex[Int, Int, Double] = {
//    vertexId: Int =>
//      new SimpleDcopVertex[Int, Int, Double](
//        optimizer,
//        SimpleConfig[Int, Int](
//          neighborhood = Map.empty[Int, Int].withDefaultValue(0),
//          numberOfCollects = 0,
//          domain = (0 to domainSize - 1).toSet,
//          centralVariableAssignment = (vertexId, initial0Value)),
//        debug)
//  }
//
//  def simpleDcopEdgeCreator: Int => Edge[Int] = {
//    targetId: Int =>
//      new SimpleDcopEdge(targetId)
//  }
//
//  def memoryDcopVertexCreator(
//    optimizer: Optimizer[Int, Int, SimpleMemoryConfig[Int, Int, Double], Double],
//    domainSize: Int,
//    debug: Boolean = false): Int => MemoryDcopVertex[Int, Int] = {
//    vertexId: Int =>
//      new MemoryDcopVertex[Int, Int](
//        optimizer,
//        SimpleMemoryConfig[Int, Int, Double](
//          neighborhood = Map.empty[Int, Int].withDefaultValue(0),
//          memory = Map.empty[Int, Double].withDefaultValue(0),
//          numberOfCollects = 0,
//          domain = (0 to domainSize - 1).toSet,
//          centralVariableAssignment = (vertexId, initial0Value)),
//        debug)
//  }
//
//  def memoryDcopEdgeCreator: Int => Edge[Int] = {
//    targetId: Int =>
//      new MemoryDcopEdge(targetId)
//  }
//
//  def rankedDcopVertexCreator(
//    optimizer: Optimizer[Int, Int, RankedConfig[Int, Int], Double],
//    domainSize: Int,
//    debug: Boolean = false): Int => RankedDcopVertex[Int, Int, Double] = {
//    vertexId: Int =>
//      new RankedDcopVertex[Int, Int, Double](
//        optimizer,
//        RankedConfig[Int, Int](
//          neighborhood = Map.empty[Int, Int].withDefaultValue(0),
//          ranks = Map.empty[Int, Double].withDefaultValue(0),
//          numberOfCollects = 0,
//          domain = (0 to domainSize - 1).toSet,
//          centralVariableAssignment = (vertexId, initial0Value)),
//        debug = debug)
//  }
//
//  def rankedDcopEdgeCreator: Int => Edge[Int] = {
//    targetId: Int =>
//      new RankedDcopEdge(targetId)
//  }

  for (repetitions <- (1 to 1))
    for (numberOfColors <- Set(9)) {
      for (gridWidth <- Set(10)) {
        for (em <- List(ExecutionMode.Synchronous, ExecutionMode.OptimizedAsynchronous)) {
          for (myOptimizer <- simpleOptimizers) {

          val myGrid = new GridBuilder(myOptimizer, gridWidth, domain= Set(numberOfColors))

          evaluation = evaluation.addEvaluationRun(DcopAlgorithmRun(
            optimizer = myOptimizer, //TODO: redundant. take from grid?? 
            graphInstantiator = myGrid,
            maxUtility = myGrid.maxUtility,
            domainSize = numberOfColors,
            executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(1000000),
            runNumber = 1,
            aggregationInterval = 0,
            revision = "2",
            evaluationDescription = "Dsan eval").runAlgorithm)

          }
        }
      }
    }


  evaluation.execute

}