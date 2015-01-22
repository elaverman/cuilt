package com.signalcollect.dcop.graph

import com.signalcollect.dcop.modules._
import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode

object Test extends App {

  println("Start")
  val myAlgo = VertexColoringAlgorithm
  val v1 = myAlgo.createVertex(0, 0, (0 to 2).toSet)
  val v2 = myAlgo.createVertex(1, 0, (0 to 2).toSet)
  val e1 = myAlgo.createEdge(targetId = 1)
  val e2 = myAlgo.createEdge(targetId = 0)
  println("Start graph building")
  val g = GraphBuilder.withConsole(true).build
  println("Start adding vertices and edges")
  g.addVertex(v1)
  g.addVertex(v2)
  g.addEdge(0,e1)
  g.addEdge(1,e2)
  println("End graph building")
  g.execute(ExecutionConfiguration.withExecutionMode(ExecutionMode.OptimizedAsynchronous))
  println(v1.state)
  println(v2.state)
  //g.execute
  println("Shutting down")
  g.shutdown
  println("bye")
}



object VertexColoringAlgorithm extends IntAlgorithm
  with SimpleState
  with VertexColoringUtility
  with ParallelRandomAdjustmentSchedule
  with ArgmaxADecisionRule
  with NashEquilibriumConvergence
  with MemoryLessTargetFunction
  with SignalCollectAlgorithmBridge {
  def changeProbability = 0.7
}

object RankedVertexColoringAlgorithm extends Algorithm with RankedState
  with VertexColoringUtility
  with FloodAdjustmentSchedule
  with ArgmaxADecisionRule
  with NashEquilibriumConvergence
  with MemoryLessTargetFunction
  with SignalCollectAlgorithmBridge