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
import com.signalcollect.dcop.graph._
import com.signalcollect.dcop.modules._
import com.signalcollect.dcop.algorithms._
import com.signalcollect.dcop.custom.SchedulingEventAlgorithm
import com.signalcollect.dcop.custom.SchedulingSlotAlgorithm
import com.signalcollect.dcop.custom.ScheduleGraphReader
import com.signalcollect.dcop.custom.SlotUtility
import com.signalcollect.dcop.custom.EventUtility
import com.signalcollect.dcop.custom.StateWithParticipants
import com.signalcollect.dcop.custom.EventSimpleState


object SchedulingDcopEvaluation extends App {

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
    localJarPath = assemblyPath,
    jvmParameters = jvmParameters,
    partition = "minion_superfast",
    excludeNodes = "minion[01-02]",
    copyExecutable = false, //true,
    jdkBinPath = "/home/user/verman/jdk1.7.0_45/bin/")
  val localHost = new LocalHost
  val mySql = new MySqlResultHandler(args(0), args(1), args(2))

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
  def evalName = s"test new"
  def evalNumber = 69
  def runs = 1 //5
  def pure = false
  def evalNumberOfNodes = 1 //2

  //    var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = gru, numberOfNodes = evalNumberOfNodes).addResultHandler(mySql) //.addResultHandler(googleDocs)
  var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = localHost, numberOfNodes = 1) //.addResultHandler(mySql) //.addResultHandler(googleDocs)
  /*********/

  var graphs: List[String] = List()

  val eventsNumber = 4
  val timeSlots = 2
  val rooms = 2
  val commonPeopleFile = "inputSchedulingTest/test_llsp.csv"
  val lectureSizeFile = "inputSchedulingTest/test_lsp.csv"
  val roomsFile = "inputSchedulingTest/test_rc.csv"
  val minOccupationRate: Double = 0.01

  //Sync
  val eventOptimizersSync: List[IntAlgorithm with EventUtility] = MixedEventAlgorithmList.algorithmsSync
  val slotOptimizersSync: List[IntAlgorithm with SlotUtility] = MixedSlotAlgorithmList.algorithmsSync

  //Async
  val eventOptimizersAsync: List[IntAlgorithm with EventUtility] = MixedEventAlgorithmList.algorithmsAsync
  val slotOptimizersAsync: List[IntAlgorithm with SlotUtility] = MixedSlotAlgorithmList.algorithmsAsync

  for (repetitions <- (1 to runs)) {
    for (em <- List(ExecutionMode.Synchronous, ExecutionMode.PureAsynchronous)) {
      for (myOptimizerEvent <- if (em == ExecutionMode.Synchronous) eventOptimizersSync else eventOptimizersAsync) {
        for (myOptimizerSlot <- if (em == ExecutionMode.Synchronous) slotOptimizersSync else slotOptimizersAsync) {

          //Random graphs

          val myGraphInstantiator = new ScheduleGraphReader(
            myOptimizerEvent, myOptimizerSlot, eventsNumber, timeSlots, rooms, commonPeopleFile, lectureSizeFile, roomsFile, minOccupationRate)

          evaluation = evaluation.addEvaluationRun( 
            new SchedulingAlgorithmRun(
              graphInstantiator = myGraphInstantiator,
              eventAlgo = myOptimizerEvent, 
              slotAlgo = myOptimizerSlot,
              domainSize = -1,
              executionConfig = ExecutionConfiguration.withExecutionMode(em).withTimeLimit(30000), //for 40 and 80 only 30 seconds, for big graphs 300 s
              runNumber = repetitions,
              aggregationInterval = 0, //if (em == ExecutionMode.Synchronous) 1 else 100, //every step or every 100 ms
              fullHistoryStats = false,
              resultsWithComma = false, //google docs wants comma, mysql period
              revision = getRevision,
              evaluationDescription = evalName).runAlgorithm)
        }
      }
    }
  }

  evaluation.execute

}
