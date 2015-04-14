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

import java.io.File
import scala.io.Source
import scala.util.Random
import com.signalcollect.Edge
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.nodeprovisioning.slurm.LocalHost
import com.signalcollect.nodeprovisioning.slurm.SlurmHost
import com.signalcollect.nodeprovisioning.slurm.SlurmJobSubmitter
//import com.signalcollect.nodeprovisioning.slurm.TorquePriority
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
//  val kraken = new TorqueHost(
//    jobSubmitter = new TorqueJobSubmitter(username = System.getProperty("user.name"), hostname = "kraken.ifi.uzh.ch"),
//    coresPerNode = 23,
//    localJarPath = assemblyPath, jvmParameters = jvmParameters, jdkBinPath = "/home/user/verman/jdk1.7.0_45/bin/", priority = TorquePriority.superfast)
    val gru = new SlurmHost(
      jobSubmitter = new SlurmJobSubmitter(username = System.getProperty("user.name"), hostname = "gru.ifi.uzh.ch"),
      coresPerNode = 10,
      localJarPath = assemblyPath, jvmParameters = jvmParameters, jdkBinPath = "/home/user/verman/jdk1.7.0_45/bin/")
  val localHost = new LocalHost
  val googleDocs = new GoogleDocsResultHandler(args(0), args(1), "evaluationCP", "bigGraph")
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
  def evalName = s"Kraken grid all"
  def evalNumber = 21
  def runs = 10
  def pure = true
 // var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = kraken).addResultHandler(googleDocs) //.addResultHandler(mySql)
    var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = gru).addResultHandler(googleDocs) //.addResultHandler(mySql)
//    var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = localHost).addResultHandler(googleDocs) //.addResultHandler(mySql)
  /*********/

  var graphs: List[String] = List()

  val numbersOfVertices = Set(10)//, 100, 1000, 10000, 100000, 1000000)
  val edgeDensities = Set(3)
  val numbersOfColors = Set(5) //Set(3)
  val numberOfGraphs = 3

  val simpleOptimizersSync: List[IntAlgorithm with Execution] = List(
    new Dsan(0.2, 1000, 2),
    new Dsan(0.2, 1, 2),
    new Dsan(0.4, 1000, 2),
    new Dsan(0.4, 1, 2),
    new Dsan(0.6, 1000, 2),
    new Dsan(0.6, 1, 2),
    new Dsan(0.8, 1000, 2),
    new Dsan(0.8, 1, 2),
    new Jsfpi(0.2),
    new Jsfpi(0.4),
    new Jsfpi(0.6),
    new Jsfpi(0.8),
    new DsaA(0.8),
    new DsaA(0.6),
    new DsaA(0.4),
    new DsaA(0.2),
    new DsaB(0.8),
    new DsaB(0.6),
    new DsaB(0.4),
    new DsaB(0.2))

  val simpleOptimizersAsync: List[IntAlgorithm with Execution] = List(
    new Dsan(0.95, 1000, 2),
    new Dsan(0.95, 1, 2),
    new Jsfpi(0.95),
    new DsaA(0.95),
    new DsaB(0.95))

  for (repetitions <- (1 to runs)) {
    for (em <- List(ExecutionMode.Synchronous, ExecutionMode.OptimizedAsynchronous)) {
      for (myOptimizer <- if (em == ExecutionMode.Synchronous) simpleOptimizersSync else simpleOptimizersAsync) {
        
        //Random graphs
        for (graphNumber <- 0 until numberOfGraphs) {
          for (numberOfVertices <- numbersOfVertices) {
            for (edgeDensity <- edgeDensities) {
              for (numberOfColors <- numbersOfColors) {

                val myGraphInstantiator = new RandomGraphReader(myOptimizer, numberOfVertices, edgeDensity, numberOfColors, graphNumber)

                evaluation = evaluation.addEvaluationRun(myOptimizer.DcopAlgorithmRun(
                  graphInstantiator = myGraphInstantiator,
                  maxUtility = myGraphInstantiator.maxUtility,
                  domainSize = numberOfColors,
                  executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(300000),
                  runNumber = repetitions,
                  aggregationInterval = 0, //if (em == ExecutionMode.Synchronous) 1 else 100, //every step or every 100 ms
                  fullHistoryStats = false,
                  revision = getRevision,
                  evaluationDescription = evalName).runAlgorithm)
              }
            }
          }
        }

        //Grids
        for (numberOfColors <- Set(8, 6, 4)) {
          for (gridWidth <- Set(1000, 100, 10)) {

            val myGrid = new GridInstantiator(myOptimizer, gridWidth, domain = (0 until numberOfColors).toSet)

            evaluation = evaluation.addEvaluationRun(myOptimizer.DcopAlgorithmRun(
              graphInstantiator = myGrid,
              maxUtility = myGrid.maxUtility,
              domainSize = numberOfColors,
              executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(300000),
              runNumber = repetitions,
              aggregationInterval = 0, //if (em == ExecutionMode.Synchronous) 1 else 100, //every step or every 100 ms
              fullHistoryStats = false,
              revision = getRevision,
              evaluationDescription = evalName).runAlgorithm)
          }
        }
      }
    }
  }

  evaluation.execute

}


