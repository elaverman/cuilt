
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

class MemoryLessTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def algorithmName = "MemoryLessTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class AverageExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def algorithmName = "AverageExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class WeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "WeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class AverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def algorithmName = "AverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class DiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "DiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class MemoryLessTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def algorithmName = "MemoryLessTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class AverageExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def algorithmName = "AverageExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class WeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "WeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class AverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def algorithmName = "AverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class DiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "DiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class MemoryLessTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def algorithmName = "MemoryLessTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" 
            }
class AverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def algorithmName = "AverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" 
            }
class WeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "WeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class AverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def algorithmName = "AverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" 
            }
class DiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "DiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class MemoryLessTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleNumberOfCollectsState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with NumberOfCollectsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "MemoryLessTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class AverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "AverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class WeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(park: Double, parrho: Double, parnegDeltaMax: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def k = park 
               def rho = parrho 
               def negDeltaMax = parnegDeltaMax 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "WeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"k" + park + "" +"rho" + parrho + "" +"negDeltaMax" + parnegDeltaMax + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class AverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "AverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class DiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(park: Double, parrho: Double, parnegDeltaMax: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def k = park 
               def rho = parrho 
               def negDeltaMax = parnegDeltaMax 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "DiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"k" + park + "" +"rho" + parrho + "" +"negDeltaMax" + parnegDeltaMax + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class MemoryLessTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def algorithmName = "MemoryLessTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class AverageExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def algorithmName = "AverageExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class WeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "WeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class AverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def algorithmName = "AverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class DiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with VertexColoringUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "DiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class WeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def rho = parrho 
               def algorithmName = "WeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class DiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def rho = parrho 
               def algorithmName = "DiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class WeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def rho = parrho 
               def algorithmName = "WeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class DiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def rho = parrho 
               def algorithmName = "DiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class MemoryLessTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double) extends IntAlgorithm
            with SimpleState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def epsilon = parepsilon 
               def algorithmName = "MemoryLessTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" 
            }
class AverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def epsilon = parepsilon 
               def algorithmName = "AverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" 
            }
class WeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def epsilon = parepsilon 
               def rho = parrho 
               def algorithmName = "WeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" +"rho" + parrho + "" 
            }
class AverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def epsilon = parepsilon 
               def algorithmName = "AverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" 
            }
class DiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def epsilon = parepsilon 
               def rho = parrho 
               def algorithmName = "DiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" +"rho" + parrho + "" 
            }
class MemoryLessTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double) extends IntAlgorithm
            with SimpleNumberOfCollectsState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with NumberOfCollectsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def algorithmName = "MemoryLessTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" 
            }
class AverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def algorithmName = "AverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" 
            }
class WeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def rho = parrho 
               def algorithmName = "WeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"rho" + parrho + "" 
            }
class AverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def algorithmName = "AverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" 
            }
class DiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parrho: Double) extends IntAlgorithm
            with SimpleMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def rho = parrho 
               def algorithmName = "DiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"rho" + parrho + "" 
            }
class WeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def rho = parrho 
               def algorithmName = "WeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class DiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with ExtendedMemoryState
            with VertexColoringUtility
            with FloodAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge
            with Execution {
               def rho = parrho 
               def algorithmName = "DiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
