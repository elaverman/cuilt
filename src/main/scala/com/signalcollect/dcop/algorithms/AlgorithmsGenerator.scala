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

package com.signalcollect.dcop.algorithms

import com.signalcollect.dcop.modules._
import com.signalcollect.dcop.evaluation._
import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode

class TargetFunctionTemplate(
  val name: String,
  val state: String,
  val extraDefs: Map[String, List[Double]])

//class StateTemplate {
//  def name: String
//}

class AdjustmentScheduleTemplate(
  val name: String,
  val extraDefs: Map[String, List[Double]])

class DecisionRuleTemplate(
  val name: String,
  val terminationRule: String,
  val extraDefs: Map[String, List[Double]])

object AlgorithmsGenerator extends App {

  val targetFunctions: List[TargetFunctionTemplate] = List(
    new TargetFunctionTemplate("MemoryLessTargetFunction", "SimpleState", Map.empty),
    new TargetFunctionTemplate("AverageExpectedUtilityTargetFunction", "SimpleMemoryState", Map.empty),
    new TargetFunctionTemplate("WeightedExpectedUtilityTargetFunction", "SimpleMemoryState", Map(("rho", List(0.2, 0.4, 0.6, 0.8)))),
    new TargetFunctionTemplate("AverageRegretsTargetFunction", "SimpleMemoryState", Map.empty),
    new TargetFunctionTemplate("DiscountedAverageRegretsTargetFunction", "SimpleMemoryState", Map(("rho", List(0.2, 0.4, 0.6, 0.8)))) //new TargetFunctionTemplate("FictitiousPlayTargetFunction", "NeighborMemoryState"),
    )

  val adjustmentSchedulesSync: List[AdjustmentScheduleTemplate] = List(
    new AdjustmentScheduleTemplate("ParallelRandomAdjustmentSchedule", Map(("changeProbability", List(0.2, 0.4, 0.6, 0.8)))),
    new AdjustmentScheduleTemplate("FloodAdjustmentSchedule", Map.empty) //    new AdjustmentScheduleTemplate("MaximumGainSchedule", Map.empty),
    //    new AdjustmentScheduleTemplate("SequentialRandomSchedule", Map.empty)
    )

  val adjustmentSchedulesAsync: List[AdjustmentScheduleTemplate] = List(
    new AdjustmentScheduleTemplate("ParallelRandomAdjustmentSchedule", Map(("changeProbability", List(0.95)))),
    new AdjustmentScheduleTemplate("FloodAdjustmentSchedule", Map.empty) //    new AdjustmentScheduleTemplate("MaximumGainSchedule", Map.empty),
    //    new AdjustmentScheduleTemplate("SequentialRandomSchedule", Map.empty)
    )

  val decisionRules: List[DecisionRuleTemplate] = List(
    new DecisionRuleTemplate("ArgmaxADecisionRule", "NashEquilibriumConvergence", Map.empty),
    new DecisionRuleTemplate("ArgmaxBDecisionRule", "NashEquilibriumConvergence", Map.empty),
    new DecisionRuleTemplate("EpsilonGreedyDecisionRule", "NashEquilibriumConvergence", Map(("epsilon", List(0.001, 0.01, 0.1)))),
    new DecisionRuleTemplate("SimulatedAnnealingDecisionRule", "SimulatedAnnealingConvergence", Map(("const", List(1000, 1)), ("k", List(2)), ("negDeltaMax", List(-0.01, -0.0001)))),
    new DecisionRuleTemplate("LinearProbabilisticDecisionRule", "DistributionConvergence", Map.empty) //    new DecisionRuleTemplate("LogisticDecisionRule", "DistributionConvergence", Map(("eta", List(1, 100, 10000))))
    )

  def combine(a: List[List[(String, Double)]], b: (String, List[Double])): List[List[(String, Double)]] = {
    a match {
      case head :: tail => for (aElem <- a; bElem <- b._2) yield (b._1, bElem) :: aElem
      case other => for (bElem <- b._2) yield List((b._1, bElem))
    }

  }

  val algorithmsFile = new java.io.FileWriter("src/main/scala/com/signalcollect/dcop/algorithms/MixedAlgorithms.scala")
  val listFile = new java.io.FileWriter("src/main/scala/com/signalcollect/dcop/algorithms/AlgorithmsList.scala")

  algorithmsFile.write("""
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

/*
 * File automatically generated with "AlgorithmsGenerator"
 */ 

package com.signalcollect.dcop.algorithms

import com.signalcollect.dcop.modules._
import com.signalcollect.dcop.evaluation._
import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode

""")

  listFile.write("""
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

/*
 * File automatically generated with "AlgorithmsGenerator"
 */ 

package com.signalcollect.dcop.algorithms

import com.signalcollect.dcop.graph._
import com.signalcollect.dcop.modules._
import com.signalcollect.dcop.algorithms._
import com.signalcollect.dcop.evaluation._
import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode

object MixedAlgorithmList {
    val algorithmsSync = List(
""")

  def printClass(
    file1: java.io.FileWriter,
    file2: java.io.FileWriter,
    t: TargetFunctionTemplate,
    a: AdjustmentScheduleTemplate,
    d: DecisionRuleTemplate,
    isEnd: Boolean,
    isSecondTime: Boolean) = {

    val allParams = (t.extraDefs ++ a.extraDefs ++ d.extraDefs).toList
    val allCombinations: List[List[(String, Double)]] = allParams.foldLeft(List[List[(String, Double)]]())((a, b) => combine(a, b))
    var counter = 0
    for (comb <- allCombinations) {
      counter += 1
      val classNameShort = s"${t.name}_${d.name}_${a.name}"
      var className = s""""$classNameShort""""
      var defs = ""
      var parListClass = ""
      var parListList = ""
      for (par <- comb) {
        className = className + s"""+"${par._1}" + par${par._1} + "" """ //par._2.toString.replaceAll(".", "")
        defs = defs + s"               def ${par._1} = par${par._1} \n"
        parListClass = parListClass + s"par${par._1}: Double, "
        parListList = parListList + s"par${par._1} = ${par._2}, "
      }

      parListClass = parListClass.subSequence(0, parListClass.size - 2).toString
      parListList = parListList.subSequence(0, parListList.size - 2).toString

      defs = defs + s"""               def algorithmName = $className"""

      val theState = if (t.state == "SimpleState" && d.name == "SimulatedAnnealingDecisionRule") {
        "SimpleNumberOfCollectsState"
      } else if (d.name == "LinearProbabilisticDecisionRule") {
        "ExtendedMemoryState"
      } else { t.state }

      if (counter == allCombinations.size && !isSecondTime)
        file1.write(s"""class $classNameShort($parListClass) extends IntAlgorithm
            with ${theState}
            with VertexColoringUtility
            with ${a.name}
            with ${d.name}
            with ${d.terminationRule}
            with ${if (t.name == "MemoryLessTargetFunction" && d.name == "SimulatedAnnealingDecisionRule") "NumberOfCollectsTargetFunction" else t.name}
            with SignalCollectAlgorithmBridge
            with Execution {
${defs}
            }
""")
      if (isEnd && counter == allCombinations.size) {
        file2.write(s"            new $classNameShort($parListList) \n")
      } else {
        file2.write(s"            new $classNameShort($parListList), \n")
      }

    }
  }

  var counter = 0
  val numberOfSync = targetFunctions.size * decisionRules.size * adjustmentSchedulesSync.size
  for (asSync <- adjustmentSchedulesSync) {
    for (d <- decisionRules) {
      for (t <- targetFunctions) {

        counter += 1
        printClass(algorithmsFile, listFile, t, asSync, d, counter == numberOfSync, isSecondTime = false)
      }
    }
  }
  counter = 0

  listFile.write("""
      )
    val algorithmsAsync = List(
""")

  for (asAsync <- adjustmentSchedulesAsync) {
    for (d <- decisionRules) {
      for (t <- targetFunctions) {

        counter += 1
        printClass(algorithmsFile, listFile, t, asAsync, d, counter == numberOfSync, isSecondTime = true)
      }
    }
  }

  listFile.write(""")
            
}""")

  algorithmsFile.close()
  listFile.close()

}

