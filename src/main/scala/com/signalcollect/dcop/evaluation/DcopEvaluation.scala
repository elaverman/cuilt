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
import com.signalcollect.dcop.algorithms._

object DcopEvaluation extends App {

  def jvmParameters = " -Xmx10240m" +
    // " -Xms512m" +
    " -XX:+AggressiveOpts" +
    " -XX:+AlwaysPreTouch" +
    " -XX:+UseNUMA" +
    " -XX:-UseBiasedLocking" +
    " -XX:MaxInlineSize=1024"

  def assemblyPath = "./target/scala-2.11/dcop-algorithms-assembly-1.0-SNAPSHOT.jar"
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
  val googleDocs = new GoogleDocsResultHandler(args(0), args(1), "evaluationMassiveMASAggreg", "data")
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
  def evalName = s"Kraken Sync Dsa, Dsan, Jsfpi aggreg"
  def evalNumber = 8
  def runs = 5
  def pure = true
  var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = kraken).addResultHandler(googleDocs) //.addResultHandler(mySql)
  //  var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = gru) //.addResultHandler(mySql)
  //    var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = localHost).addResultHandler(googleDocs) //.addResultHandler(mySql)
  /*********/

  //TODO Why do we have the probl since we SimpleConfig used by DsaAVC extends Configuration??
  val simpleOptimizers: List[IntAlgorithm with Execution] = List(
    new Dsan(0.2, 1000, 2),
    new Dsan(0.2, 1, 2),
    new Jsfpi(0.2),
    new Dsan(0.4, 1000, 2),
    new Dsan(0.4, 1, 2),
    new Jsfpi(0.4),
    new Dsan(0.6, 1000, 2),
    new Dsan(0.6, 1, 2),
    new Jsfpi(0.6),
    new Dsan(0.8, 1000, 2),
    new Dsan(0.8, 1, 2),
    new Jsfpi(0.8),
    new DsaA(0.8),
    new DsaA(0.6),
    new DsaA(0.4),
    new DsaA(0.2),
    new DsaB(0.8),
    new DsaB(0.6),
    new DsaB(0.4),
    new DsaB(0.2))
  //    new Dsan(0.2, 1000, 2),
  //    new Dsan(0.2, 1, 2),
  //    new Jsfpi(0.2),
  //    new Wrmi(0.2, 0.2),
  //    new Wrmi(0.2, 0.4),
  //    new Wrmi(0.2, 0.6),
  //    new Wrmi(0.2, 0.8),
  //    new Dsan(0.4, 1000, 2),
  //    new Dsan(0.4, 1, 2),
  //    new Jsfpi(0.4),
  //    new Wrmi(0.4, 0.2),
  //    new Wrmi(0.4, 0.4),
  //    new Wrmi(0.4, 0.6),
  //    new Wrmi(0.4, 0.8),
  //    new Dsan(0.6, 1000, 2),
  //    new Dsan(0.6, 1, 2),
  //    new Jsfpi(0.6),
  //    new Wrmi(0.6, 0.2),
  //    new Wrmi(0.6, 0.4),
  //    new Wrmi(0.6, 0.6),
  //    new Wrmi(0.6, 0.8),
  //    new Dsan(0.8, 1000, 2),
  //    new Dsan(0.8, 1, 2),
  //    new Jsfpi(0.8),
  //    new Wrmi(0.8, 0.2),
  //    new Wrmi(0.8, 0.4),
  //    new Wrmi(0.8, 0.6),
  //    new Wrmi(0.8, 0.8)

  //      new Dsan(1.0, 1000, 2),
  //      new Dsan(1.0, 1, 2),
  //      new Jsfpi(1.0),
  //      new Wrmi(1.0, 0.2),
  //      new Wrmi(1.0, 0.4),
  //      new Wrmi(1.0, 0.6),
  //      new Wrmi(1.0, 0.8))
  //    new DsaA(1.0),
  //    new DsaB(1.0))
  //    new DsaA(0.8),
  //    new DsaA(0.6),
  //    new DsaA(0.4),
  //    new DsaA(0.2),
  //    new DsaB(0.8),
  //    new DsaB(0.6),
  //    new DsaB(0.4),
  //    new DsaB(0.2))

  //  val adoptGraphNamesList = new java.io.File("adoptInput").listFiles.filter(x => (x.getName.startsWith("Problem-GraphColor-40_3_"))).map(_.getName)
  //  val dimacsGraphNamesList = new java.io.File("dimacsInput").listFiles.filter(x => (x.getName.endsWith("flat1000_76_0.col"))).map(_.getName)

  def initial0Value = 0

  for (repetitions <- (1 to runs))
    for (numberOfColors <- Set(8, 6, 4)) {
      for (gridWidth <- Set(1000, 100, 10)) {
        for (em <- List(ExecutionMode.Synchronous /*, ExecutionMode.OptimizedAsynchronous*/ )) {
          for (myOptimizer <- simpleOptimizers) {

            val myGrid = new GridInstantiator(myOptimizer, gridWidth, domain = (0 until numberOfColors).toSet)

            evaluation = evaluation.addEvaluationRun(myOptimizer.DcopAlgorithmRun(
              graphInstantiator = myGrid,
              maxUtility = myGrid.maxUtility,
              domainSize = numberOfColors,
              executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(600000),//300000), //1000000),
              runNumber = repetitions,
              aggregationInterval = 1, //100, //if (em == ExecutionMode.Synchronous) 1 else 100,
              fullHistoryStats = false,
              revision = getRevision,
              evaluationDescription = evalName).runAlgorithm)

          }
        }
      }
    }

  evaluation.execute

}