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
  def algorithmName = "DsaA" + changeProbabilityParam
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
  def algorithmName = "DsaB" + changeProbabilityParam
}

class Dsan(changeProbabilityParam: Double, constant: Double, kval: Double) extends IntAlgorithm
  with SimpleState
  with VertexColoringUtility
  with ParallelRandomAdjustmentSchedule
  with SimulatedAnnealingDecisionRule
  with SimulatedAnnealingConvergence
  with MemoryLessTargetFunction
  with SignalCollectAlgorithmBridge
  with Execution {
  def changeProbability = changeProbabilityParam
  def const = constant
  def k = kval
  def algorithmName = "Dsan" + changeProbabilityParam + "const" + constant + "k" + kval
}

class Jsfpi(changeProbabilityParam: Double) extends IntAlgorithm
  with SimpleMemoryState
  with VertexColoringUtility
  with ParallelRandomAdjustmentSchedule
  with ArgmaxADecisionRule
  with NashEquilibriumConvergence //TODO: Check if it converges in beliefs, as it should.
  with AverageExpectedUtilityTargetFunction
  with SignalCollectAlgorithmBridge
  with Execution {
  def changeProbability = changeProbabilityParam
  def algorithmName = "Jsfpi" + changeProbabilityParam
}

class Wrmi(changeProbabilityParam: Double, rhoParam: Double) extends IntAlgorithm
  with SimpleMemoryState
  with VertexColoringUtility
  with ParallelRandomAdjustmentSchedule
  with LinearProbabilisticDecisionRule
  with NashEquilibriumConvergence //TODO: Check if it converges in beliefs, as it should.
  with DiscountedAverageRegretsTargetFunction
  with SignalCollectAlgorithmBridge
  with Execution {
  def changeProbability = changeProbabilityParam
  def rho = rhoParam
  def algorithmName = "Wrmi" + changeProbabilityParam+ "rhoValue" + rho
}
