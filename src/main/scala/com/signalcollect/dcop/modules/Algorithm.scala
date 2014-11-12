package com.signalcollect.dcop.modules

trait Algorithm extends Serializable {

  type AgentId
  type Action
  type SignalType
  type UtilityType = Double
  type State <: StateTrait[AgentId, Action, SignalType, UtilityType] //everything is immutable

  def shouldConsiderMove(c: State): Boolean //adj schedule

  def computeMove(c: State): Action //decision rule

  def shouldTerminate(c: State): Boolean //decision rule

  def isInLocalOptimum(c: State): Boolean //decision rule

  def updateMemory(c: State): State //target function

  def computeUtility(c: State): UtilityType //utility

  def computeExpectedUtilities(c: State): Map[Action, UtilityType] //target function

}
