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
import com.signalcollect.dcop.modules._
import com.signalcollect.dcop.algorithms._
import com.signalcollect.dcop.evaluation._

/**
 * Creates the text files for the graphs on the given host.
 */

object GraphGeneratorRunner extends App {

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
    partition = "minion_su",
    localJarPath = assemblyPath, jvmParameters = jvmParameters, jdkBinPath = "/home/user/verman/jdk1.7.0_45/bin/")
  val localHost = new LocalHost

  /*********/
  def evalName = s"Graph generator"
  def evalNumber = 21
  def runs = 1
  def pure = true
//  var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = gru)
  var evaluation = new Evaluation(evaluationName = evalName, evaluationNumber = evalNumber, executionHost = localHost)
  /*********/

  val numbersOfVertices = Set(40) //, 100, 1000, 10000, 100000, 1000000)//, 10000000)
  val edgeDensities = Set(3)
  val numbersOfColors = Set(3)
  val numberOfGraphs = 10
  val adoptGraphFormat = false
  val discardForInitialLonelyVertices = false

  //Graphs
  for (i <- 0 until numberOfGraphs) {
    for (numberOfVertices <- numbersOfVertices) {
      for (edgeDensity <- edgeDensities) {
        for (numberOfColors <- numbersOfColors) {
          evaluation = evaluation.addEvaluationRun(RandomGraphGeneratorRun(numberOfVertices, edgeDensity, numberOfColors, s"inputGraphsCP/V${numberOfVertices}_ED${edgeDensity}_Col${numberOfColors}_$i.txt", adoptGraphFormat, discardForInitialLonelyVertices).generate)
        }
      }
    }
  }

  //Grids
  for (numberOfVertices <- numbersOfVertices) {
    for (edgeDensity <- edgeDensities) {
      for (numberOfColors <- numbersOfColors) {
        evaluation = evaluation.addEvaluationRun(GridGeneratorRun((math.sqrt(numberOfVertices)).toInt, numberOfColors, s"inputGraphsCP/V${numberOfVertices}_ED${edgeDensity}_Col${numberOfColors}_Grid.txt", adoptGraphFormat).generate)
      }
    }
  }

  evaluation.execute

}
