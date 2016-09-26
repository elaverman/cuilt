
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
import com.signalcollect.dcop.custom._

class EventMemoryLessTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "EventMemoryLessTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class EventAverageExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "EventAverageExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class EventWeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "EventWeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class EventAverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "EventAverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class EventDiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "EventDiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class EventMemoryLessTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "EventMemoryLessTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class EventAverageExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "EventAverageExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class EventWeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "EventWeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class EventAverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "EventAverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class EventDiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "EventDiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class EventMemoryLessTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def algorithmName = "EventMemoryLessTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" 
            }
class EventAverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def algorithmName = "EventAverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" 
            }
class EventWeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "EventWeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class EventAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def algorithmName = "EventAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" 
            }
class EventDiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule(parepsilon: Double, parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "EventDiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_ParallelRandomAdjustmentSchedule"+"epsilon" + parepsilon + "" +"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class EventMemoryLessTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleNumberOfCollectsState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with NumberOfCollectsTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "EventMemoryLessTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class EventAverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "EventAverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class EventWeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(park: Double, parrho: Double, parnegDeltaMax: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
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
               def algorithmName = "EventWeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"k" + park + "" +"rho" + parrho + "" +"negDeltaMax" + parnegDeltaMax + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class EventAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def changeProbability = parchangeProbability 
               def algorithmName = "EventAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class EventDiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule(park: Double, parrho: Double, parnegDeltaMax: Double, parconst: Double, parchangeProbability: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
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
               def algorithmName = "EventDiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_ParallelRandomAdjustmentSchedule"+"k" + park + "" +"rho" + parrho + "" +"negDeltaMax" + parnegDeltaMax + "" +"const" + parconst + "" +"changeProbability" + parchangeProbability + "" 
            }
class EventMemoryLessTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with EventExtendedMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "EventMemoryLessTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class EventAverageExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with EventExtendedMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "EventAverageExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class EventWeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with EventExtendedMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "EventWeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class EventAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double) extends IntAlgorithm
            with EventExtendedMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def algorithmName = "EventAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" 
            }
class EventDiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule(parchangeProbability: Double, parrho: Double) extends IntAlgorithm
            with EventExtendedMemoryState
            with EventUtility
            with ParallelRandomAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def changeProbability = parchangeProbability 
               def rho = parrho 
               def algorithmName = "EventDiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_ParallelRandomAdjustmentSchedule"+"changeProbability" + parchangeProbability + "" +"rho" + parrho + "" 
            }
class EventWeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "EventWeightedExpectedUtilityTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class EventDiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with ArgmaxADecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "EventDiscountedAverageRegretsTargetFunction_ArgmaxADecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class EventWeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "EventWeightedExpectedUtilityTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class EventDiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with ArgmaxBDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "EventDiscountedAverageRegretsTargetFunction_ArgmaxBDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class EventMemoryLessTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double) extends IntAlgorithm
            with EventSimpleState
            with EventUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with MemoryLessTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def algorithmName = "EventMemoryLessTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" 
            }
class EventAverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def algorithmName = "EventAverageExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" 
            }
class EventWeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double, parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def rho = parrho 
               def algorithmName = "EventWeightedExpectedUtilityTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" +"rho" + parrho + "" 
            }
class EventAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def algorithmName = "EventAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" 
            }
class EventDiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule(parepsilon: Double, parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with EpsilonGreedyDecisionRule
            with NashEquilibriumConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def epsilon = parepsilon 
               def rho = parrho 
               def algorithmName = "EventDiscountedAverageRegretsTargetFunction_EpsilonGreedyDecisionRule_FloodAdjustmentSchedule"+"epsilon" + parepsilon + "" +"rho" + parrho + "" 
            }
class EventMemoryLessTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double) extends IntAlgorithm
            with EventSimpleNumberOfCollectsState
            with EventUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with NumberOfCollectsTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def algorithmName = "EventMemoryLessTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" 
            }
class EventAverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def algorithmName = "EventAverageExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" 
            }
class EventWeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def rho = parrho 
               def algorithmName = "EventWeightedExpectedUtilityTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"rho" + parrho + "" 
            }
class EventAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with AverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def algorithmName = "EventAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" 
            }
class EventDiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule(parnegDeltaMax: Double, park: Double, parconst: Double, parrho: Double) extends IntAlgorithm
            with EventSimpleMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with SimulatedAnnealingDecisionRule
            with SimulatedAnnealingConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def negDeltaMax = parnegDeltaMax 
               def k = park 
               def const = parconst 
               def rho = parrho 
               def algorithmName = "EventDiscountedAverageRegretsTargetFunction_SimulatedAnnealingDecisionRule_FloodAdjustmentSchedule"+"negDeltaMax" + parnegDeltaMax + "" +"k" + park + "" +"const" + parconst + "" +"rho" + parrho + "" 
            }
class EventWeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with EventExtendedMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with WeightedExpectedUtilityTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "EventWeightedExpectedUtilityTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
class EventDiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule(parrho: Double) extends IntAlgorithm
            with EventExtendedMemoryState
            with EventUtility
            with FloodAdjustmentSchedule
            with LinearProbabilisticDecisionRule
            with DistributionConvergence
            with DiscountedAverageRegretsTargetFunction
            with SignalCollectAlgorithmBridge {
               def rho = parrho 
               def algorithmName = "EventDiscountedAverageRegretsTargetFunction_LinearProbabilisticDecisionRule_FloodAdjustmentSchedule"+"rho" + parrho + "" 
            }
