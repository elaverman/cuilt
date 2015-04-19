
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
