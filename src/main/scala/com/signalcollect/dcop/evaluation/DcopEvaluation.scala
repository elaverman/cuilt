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

  val gru = new SlurmHost(
    jobSubmitter = new SlurmJobSubmitter(username = System.getProperty("user.name"), hostname = "gru.ifi.uzh.ch"),
    coresPerNode = 10,
    partition = "minion_superfast",
    excludeNodes = "minion[01-14]",
    copyExecutable = false,
    localJarPath = assemblyPath, jvmParameters = jvmParameters, jdkBinPath = "/home/user/verman/jdk1.7.0_45/bin/")
  val localHost = new LocalHost
  val googleDocs = new GoogleDocsResultHandler(args(0), args(1), "distributedEval", "bigGraph")
  val mySql = new MySqlResultHandler(args(2), args(3), args(4))

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
  def evalName = s"test distribute"
  def evalNumber = 41
  def runs = 1 //5
  def pure = true
  def evalNumberOfNodes = 2

    var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = gru, numberOfNodes = evalNumberOfNodes).addResultHandler(googleDocs)//(mySql) //.addResultHandler(googleDocs)
//  var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = localHost, numberOfNodes = 1).addResultHandler(mySql) //.addResultHandler(googleDocs)
  /*********/

  var graphs: List[String] = List()

  val numbersOfVertices = Set(40) //10, 100, 1000, 10000, 100000, 1000000)
  val edgeDensities = Set(3)
  val numbersOfColors = Set(3) //, 4, 3)
  val numberOfGraphs = 1 //5

  val simpleOptimizersSync: List[IntAlgorithm with Execution] = MixedAlgorithmList.testAlgoSync //algorithmsSync
  val simpleOptimizersAsync: List[IntAlgorithm with Execution] = MixedAlgorithmList.testAlgoAsync //algorithmsAsync

  for (numberOfVertices <- numbersOfVertices) {
    for (repetitions <- (1 to runs)) {
      for (em <- List(ExecutionMode.Synchronous, ExecutionMode.OptimizedAsynchronous)) {
        for (myOptimizer <- if (em == ExecutionMode.Synchronous) simpleOptimizersSync else simpleOptimizersAsync) {

          //Random graphs
          for (graphNumber <- 0 until numberOfGraphs) {

            for (edgeDensity <- edgeDensities) {
              for (numberOfColors <- numbersOfColors) {

                val myGraphInstantiator = new RandomGraphReader(myOptimizer, numberOfVertices, edgeDensity, numberOfColors, graphNumber)

                evaluation = evaluation.addEvaluationRun(myOptimizer.DcopAlgorithmRun(
                  graphInstantiator = myGraphInstantiator,
                  maxUtility = myGraphInstantiator.maxUtility,
                  domainSize = numberOfColors,
                  executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(30000), //for 40 and 80 only 30 seconds, for big graphs 300 s
                  runNumber = repetitions,
                  aggregationInterval = 0, //if (em == ExecutionMode.Synchronous) 1 else 100, //every step or every 100 ms
                  fullHistoryStats = false,
                  resultsWithComma = true,
                  revision = getRevision,
                  evaluationDescription = evalName).runAlgorithm)
              }
            }
          }
        }

        //        //Grids
        //        for (numberOfColors <- Set(8, 6, 4)) {
        //          for (gridWidth <- Set(1000, 100, 10)) {
        //
        //            val myGrid = new GridInstantiator(myOptimizer, gridWidth, domain = (0 until numberOfColors).toSet)
        //
        //            evaluation = evaluation.addEvaluationRun(myOptimizer.DcopAlgorithmRun(
        //              graphInstantiator = myGrid,
        //              maxUtility = myGrid.maxUtility,
        //              domainSize = numberOfColors,
        //              executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(300000),
        //              runNumber = repetitions,
        //              aggregationInterval = 0, //if (em == ExecutionMode.Synchronous) 1 else 100, //every step or every 100 ms
        //              fullHistoryStats = false,
        //              revision = getRevision,
        //              evaluationDescription = evalName).runAlgorithm)
        //          }
        //        }
      }
    }
  }

  evaluation.execute

}
