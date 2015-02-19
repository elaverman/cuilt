package com.signalcollect.dcop.algorithms

import com.signalcollect.dcop.modules._
import com.signalcollect.dcop.evaluation._
import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode



class DsaA(changeProbabilityParam: Double) extends IntAlgorithm
  with SimpleState
  with VertexColoringUtility
  with ParallelRandomAdjustmentSchedule
  with ArgmaxADecisionRule
  with NashEquilibriumConvergence
  with MemoryLessTargetFunction
  with SignalCollectAlgorithmBridge 
  with Execution {
  def changeProbability = changeProbabilityParam
  def algorithmName = "DsaA"+changeProbabilityParam
}

class DsaB(changeProbabilityParam: Double) extends IntAlgorithm
  with SimpleState
  with VertexColoringUtility
  with ParallelRandomAdjustmentSchedule
  with ArgmaxBDecisionRule
  with NashEquilibriumConvergence
  with MemoryLessTargetFunction
  with SignalCollectAlgorithmBridge 
  with Execution {
  def changeProbability = changeProbabilityParam
  def algorithmName = "DsaB"+changeProbabilityParam
}