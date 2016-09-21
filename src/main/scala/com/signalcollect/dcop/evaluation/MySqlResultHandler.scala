/*
 *  @author Daniel Strebel
 *  @author Philip Stutz
 *  
 *  Copyright 2012 University of Zurich
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

import java.net.URL
import scala.collection.JavaConversions._
import scala.slick.driver.MySQLDriver.simple._
import com.signalcollect.nodeprovisioning.slurm._
import java.net.InetAddress


//TODO include table name as param
class AllResults(tag: Tag) extends Table[RowType](tag, "evalsept") {
  // def result_id = column[Int]("result_id", O.PrimaryKey, O.AutoInc, O.NotNull) // INT NOT NULL AUTO_INCREMENT,
  def evaluationDescription = column[String]("evaluationDescription")
  def optimizer = column[String]("optimizer")
  def utility = column[Double]("utility")
  def domainSize = column[Int]("domainSize")
  def graphSize = column[Int]("graphSize")
  def executionMode = column[String]("executionMode")
  def conflictCount = column[Int]("conflictCount")
  def numberOfEdges = column[Int]("numberOfEdges")
  def endUtilityRatio = column[Double]("endUtilityRatio")
  def isOptimal = column[Int]("isOptimal")
  def numberOfLocOptima = column[Int]("numberOfLocOptima")
  def messagesPerVertexPerStep = column[Double]("messagesPerVertexPerStep")
  //isOptimizerRanked VARCHAR(10),
  def revision = column[String]("revision")
  def isNe = column[Int]("isNe")
  def run = column[Int]("run")
  def numberOfCollectSteps = column[String]("numberOfCollectSteps")
  //timeLimit VARCHAR(20),
  def graphStructure = column[String]("graphStructure")
  def jobId = column[String]("jobId") //, O.PrimaryKey)
  def computationTimeInMilliseconds = column[Double]("computationTimeInMilliseconds")
  def date = column[String]("date")
  def terminationReason = column[String]("terminationReason")

  def * = (evaluationDescription, optimizer, utility, domainSize, graphSize, executionMode, conflictCount,
    numberOfEdges, endUtilityRatio, isOptimal, numberOfLocOptima, messagesPerVertexPerStep,
    revision, isNe, run, numberOfCollectSteps, graphStructure, jobId, computationTimeInMilliseconds,
    date, terminationReason)

  def pk = primaryKey("pk_composite", (evaluationDescription, jobId))

}

class MySqlResultHandler(username: String, password: String, ipAddress: String)
  extends Function1[Map[String, String], Unit]
  with Serializable {

  def allResults = TableQuery[AllResults] //if used with def it will never be serialized

  def apply(data: Map[String, String]) = {

    //TODO move from data to tuple
    val dataEvaluationDescription = data.getOrElse("evaluationDescription", "")
    val dataOptimizer = data.getOrElse("optimizer", "")
    val dataUtility = data.getOrElse("utility", "").toDouble
    val dataDomainSize = data.getOrElse("domainSize", "").toInt
    val dataGraphSize = data.getOrElse("graphSize", "").toInt
    val dataExecutionMode = data.getOrElse("executionMode", "")
    val dataConflictCount = data.getOrElse("conflictCount", "").toInt
    val dataNumberOfEdges = data.getOrElse("numberOfEdges", "").toInt // Measure (1)
    val dataEndUtilityRatio = data.getOrElse("endUtilityRatio", "").toDouble // Measure (2)
    val dataIsOptimal = if (data.getOrElse("isOptimal", "") == "false") 0 else 1 // Measure (3)
    val dataNumberOfLocOptima = data.getOrElse("numberOfLocOptima", "").toInt // Measure (4)
    val dataMessagesPerVertexPerStep = data.getOrElse("messagesPerVertexPerStep", "").toDouble // Measure (5)
    //val dataIsOptimizerRanked = data.getOrElse("isOptimizerRanked", "")
    val dataRevision = data.getOrElse("revision", "")
    val dataIsNe = if (data.getOrElse("isNe", "") == "false") 0 else 1
    val dataRun = data.getOrElse("run", "").toInt
    val dataNumberOfCollectSteps = data.getOrElse("numberOfCollectSteps", "")
    //val dataTimeLimit = data.getOrElse("timeLimit", "")
    val dataGraphStructure = data.getOrElse("graphStructure", "")
    val dataJobId = data.getOrElse("jobId", "")
    val dataComputationTimeInMilliseconds = data.getOrElse("computationTimeInMilliseconds", "").toDouble
    val dataDate = data.getOrElse("date", "")
    val dataTerminationReason = data.getOrElse("terminationReason", "")

    //    val dataSignalThreshold = data.getOrElse("signalThreshold", "").toDouble
    //    val dataCollectThreshold = data.getOrElse("collectThreshold", "").toDouble

    val runResultMySql = (dataEvaluationDescription, dataOptimizer, dataUtility, dataDomainSize,
      dataGraphSize, dataExecutionMode, dataConflictCount,
      dataNumberOfEdges, dataEndUtilityRatio, dataIsOptimal, dataNumberOfLocOptima, dataMessagesPerVertexPerStep,
      //dataIsOptimizerRanked,
      dataRevision, dataIsNe, dataRun, dataNumberOfCollectSteps, //dataTimeLimit,
      dataGraphStructure, dataJobId, dataComputationTimeInMilliseconds,
      dataDate, dataTerminationReason)

//        val address = InetAddress.getByName(ipAddress)
//        val realIp = address.getHostAddress
//        
//        println(s"Ip address for $ipAddress is $realIp")

    Database.forURL(s"jdbc:mysql://$ipAddress/optimizers_db", user = username, password = password, driver = "com.mysql.jdbc.Driver") withSession {
      implicit session =>
        //         allResults.ddl.create
        actionWithExponentialRetry(() => allResults += runResultMySql)
    }

  }

  def actionWithExponentialRetry[G](action: () => G): G = {
    try {
      action()
    } catch {
      case e: Exception =>
        // just retry a few times
        try {
          println("Database API exception: " + e)
          println("Database API retry in 1 second")
          Thread.sleep(1000)
          println("Retrying.")
          action()
        } catch {
          case e: Exception =>
            try {
              println("Database API exception: " + e)
              println("Database API retry in 10 seconds")
              Thread.sleep(10000)
              println("Retrying.")
              action()
            } catch {
              case e: Exception =>
                try {
                  println("Database API exception: " + e)
                  println("Database API retry in 100 seconds")
                  Thread.sleep(100000)
                  println("Retrying.")
                  action()
                } catch {
                  case e: Exception =>
                    println("Database did not acknowledge write: " + e)
                    null.asInstanceOf[G]
                }
            }
        }
    }
  }

}
