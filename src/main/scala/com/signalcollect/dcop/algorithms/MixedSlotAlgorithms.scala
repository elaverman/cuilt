
/*
 *  @author Philip Stutz
 *  @author Mihaela Verman
 *  
 *  Copyright 2016 University of Zurich
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
import com.signalcollect.dcop.custom.SlotUtility

class SlotMemoryLessTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotMemoryLessTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class SlotAverageExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotAverageExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class SlotWeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "SlotWeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class SlotAverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotAverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class SlotDiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "SlotDiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class SlotMemoryLessTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotMemoryLessTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class SlotAverageExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotAverageExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class SlotWeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "SlotWeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class SlotAverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotAverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class SlotDiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "SlotDiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class SlotMemoryLessTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotMemoryLessTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" 
            }
class SlotAverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotAverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" 
            }
class SlotWeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "SlotWeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class SlotAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" 
            }
class SlotDiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "SlotDiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class SlotMemoryLessTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleNumberOfCollectsState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with NumberOfCollectsTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotMemoryLessTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class SlotAverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotAverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class SlotWeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(park: Double, parrho: Double, parnegDeltaMax: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def k = park 
               def rho = parrho 
               def negDeltaMax = parnegDeltaMax 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotWeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"k" + park + "" +"rho" + parrho + "" +"negDeltaMax" + parnegDeltaMax + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class SlotAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class SlotDiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(park: Double, parrho: Double, parnegDeltaMax: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def k = park 
               def rho = parrho 
               def negDeltaMax = parnegDeltaMax 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotDiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"k" + park + "" +"rho" + parrho + "" +"negDeltaMax" + parnegDeltaMax + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class SlotMemoryLessTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotMemoryLessTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class SlotAverageExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotAverageExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class SlotWeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "SlotWeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class SlotAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "SlotAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class SlotDiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with SlotUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "SlotDiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class SlotWeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "SlotWeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class SlotDiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "SlotDiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class SlotWeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "SlotWeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class SlotDiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "SlotDiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class SlotMemoryLessTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double) extends IntAlgorithm
            with SimpleState
            with SlotUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def algorithmName = "SlotMemoryLessTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" 
            }
class SlotAverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def algorithmName = "SlotAverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" 
            }
class SlotWeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def rho = parrho 
               def algorithmName = "SlotWeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" +"rho" + parrho + "" 
            }
class SlotAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def algorithmName = "SlotAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" 
            }
class SlotDiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def rho = parrho 
               def algorithmName = "SlotDiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" +"rho" + parrho + "" 
            }
class SlotMemoryLessTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double) extends IntAlgorithm
            with SimpleNumberOfCollectsState
            with SlotUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with NumberOfCollectsTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def algorithmName = "SlotMemoryLessTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" 
            }
class SlotAverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def algorithmName = "SlotAverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" 
            }
class SlotWeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def rho = parrho 
               def algorithmName = "SlotWeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"rho" + parrho + "" 
            }
class SlotAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def algorithmName = "SlotAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" 
            }
class SlotDiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def rho = parrho 
               def algorithmName = "SlotDiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"rho" + parrho + "" 
            }
class SlotWeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "SlotWeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class SlotDiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with SlotUtility
            with FloodAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "SlotDiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
