package com.signalcollect.dcop.graph

import com.signalcollect.dcop.modules._


object Test extends App {

  val myAlgo = new Algorithm with VertexColoringUtility with FloodAdjustmentSchedule with ArgmaxADecisionRule with NashEquilibriumConvergence with MemoryLessTargetFunction
}

