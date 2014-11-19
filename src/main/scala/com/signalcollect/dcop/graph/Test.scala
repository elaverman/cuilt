package com.signalcollect.dcop.graph

import com.signalcollect.dcop.modules._

object Test extends App {

  val myAlgo = VertexColoringAlgorithm
}

object VertexColoringAlgorithm extends Algorithm
  with VertexColoringUtility
  with FloodAdjustmentSchedule
  with ArgmaxADecisionRule
  with NashEquilibriumConvergence
  with MemoryLessTargetFunction
  with SignalCollectAlgorithmBridge

object RankedVertexColoringAlgorithm extends Algorithm with RankedState
  with VertexColoringUtility
  with FloodAdjustmentSchedule
  with ArgmaxADecisionRule
  with NashEquilibriumConvergence
  with MemoryLessTargetFunction
  with SignalCollectAlgorithmBridge