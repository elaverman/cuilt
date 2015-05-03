/*
 *  @author Philip Stutz
 *  @author Mihaela Verman
 *  
 *  Copyright 2013 University of Zurich
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

package com.signalcollect.dcop.tests.integration

import org.scalacheck.Arbitrary

import org.scalacheck.Gen
import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import org.scalatest.prop.Checkers
import scala.math.Ordering.Boolean

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.dcop.modules.IntAlgorithm
import com.signalcollect.nodeprovisioning.slurm.LocalHost

import org.scalatest.Tag

//http://www.scalatest.org/user_guide/tagging_your_tests
object SlowTest extends Tag("com.signalcollect.dcop.tags.SlowTest")


trait TestTools {
  var runId = 0
  val minRuns = 10
  val executionModes = List(ExecutionMode.OptimizedAsynchronous, ExecutionMode.PureAsynchronous, ExecutionMode.Synchronous)

  //    lazy val smallWidth = Gen.chooseNum(1, 10)//.map(Width(_))
  //  implicit def arbSmallWidth[Int] = Arbitrary(smallWidth)
  implicit lazy val arbInt = Arbitrary[Int](Gen.chooseNum(1, 200))

  def zeroInitialized(domain: Set[Int]) = 0
  val debug = false
  val localHost = new LocalHost

  def implies(a: Boolean, b: Boolean): Boolean = !a || b

  def checkAssertions(runId: Int, terminationReason: String, isNe: String, isOptimal: String, isAbsorbing: Boolean, mustConverge: Boolean, extraInfo: String = ""): Boolean = {
    assert(implies(isOptimal == "true", isNe == "true"), s"It is optimal but not in a NE in run $runId. Termination reason $terminationReason. $extraInfo") // for: $em ${myAlgorithm.toString}, GRAPH($myGraph), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
    assert(implies(terminationReason == "Converged", isNe == "true"), s"Termination reason $terminationReason, NE $isNe in run $runId. $extraInfo") // for: $em ${myAlgorithm.toString}, GRAPH($myGraph), aggregation interval = $myAggregationInterval, fullHistory = $myFullHistory.")
    if (isAbsorbing) {
      assert(implies(isNe == "true", terminationReason == "Converged"), s"Termination reason $terminationReason, NE $isNe in run $runId. NE should be absorbing. $extraInfo")
    }
    if (mustConverge) {
      assert(terminationReason == "Converged", s"Termination reason $terminationReason. Algorithm should have converged. $extraInfo")
    }
    true
  }
}

