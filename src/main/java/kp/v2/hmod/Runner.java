
package kp.v2.hmod;

import kp.v1.hmod.*;
import hmod.solvers.common.Heuristic;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterativeHeuristic;
import optefx.loader.ModuleLoader;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;

public class Runner {
    public static void main(String[] args) {
        Heuristic h = new ModuleLoader().
            loadAll(
                KnapsackProblemDomain.class, 
                KnapsackGreedy.class, 
                IterativeHeuristic.class
            ).
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 10000).
            setParameter(IterativeHeuristic.MAX_SECONDS, 10.0).
            setParameter(KnapsackProblemDomain.INITIALIZER, KnapsackProblemDomain.ORDER_BASED_INIT).
            setParameter(KnapsackProblemDomain.ITEM_SELECTOR, KnapsackGreedy.GREEDY_ITEM_SELECTOR).
            getInstance(Heuristic.class);
        
        OutputManager.getCurrent().setOutputsFromConfig(
            new OutputConfig().
                addSystemOutputId(HeuristicOutputIds.EXECUTION_INFO)
        );
        
        h.run();
    }
}
