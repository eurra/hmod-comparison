
package kp.v1.hmod;

import hmod.solvers.common.Heuristic;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterativeHeuristic;
import java.io.PrintWriter;
import optefx.loader.ModuleLoader;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;

public class RunnerHmod {
    
    public static ModuleLoader getLoader(int maxIterations) {
        OutputManager.getCurrent().setOutputsFromConfig(
            new OutputConfig().
                addSystemOutputId(HeuristicOutputIds.EXECUTION_INFO)
        );
        
        return new ModuleLoader().
            loadAll(KnapsackProblemDomain.class, IterativeHeuristic.class).
            setParameter(IterativeHeuristic.MAX_ITERATIONS, maxIterations).
            setParameter(IterativeHeuristic.MAX_SECONDS, 10.0).
            setParameter(KnapsackProblemDomain.INITIALIZER, KnapsackProblemDomain.ORDER_BASED_INIT).
            setParameter(KnapsackProblemDomain.ITEM_SELECTOR, KnapsackProblemDomain.RANDOM_ITEM_SELECTOR);
    }
    
    public static void run(ModuleLoader coreBuilder, PrintWriter output) {        
        Heuristic h = coreBuilder.getInstance(Heuristic.class);        
        long startTime = System.nanoTime();        
        h.run();        
        long endTime = System.nanoTime();
        double elapsed = (double) (endTime - startTime) / 1000000000;
        
         if(output != null)
            output.println(String.format("%.10f", elapsed));
    }
    
    public static void main(String[] args) {
        run(getLoader(10000), null);
    }
}
