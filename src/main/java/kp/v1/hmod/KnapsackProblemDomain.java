
package kp.v1.hmod;

import static hmod.core.FlowchartFactory.*;
import hmod.core.PlaceholderStatement;
import hmod.core.Statement;
import hmod.solvers.common.IterativeHeuristic;
import kp.core.Benchmark;
import kp.core.ItemList;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.loader.Resolvable;

public class KnapsackProblemDomain {  
    public static final Parameter<KnapsackInitializer> INITIALIZER = new Parameter<>();
    
    public static final KnapsackInitializer DEFAULT_INIT = Resolvable.boundTo(
        KnapsackInitializer.class,
        KnapsackProblemDomain.class,
        (kpd) -> kpd.defaultInit
    );
    
    @LoadsComponent({
        KnapsackProblemDomain.class
    })
    public static void load(ComponentRegister cr, ParameterRegister pr, IterativeHeuristic ih) {
        KnapsackOperators kso = new KnapsackOperators();
        KnapsackProblemDomain kpd = cr.provide(new KnapsackProblemDomain(kso));        
        
        KnapsackInitializer initResolver = pr.getRequiredValue(INITIALIZER);
        pr.addBoundHandler(initResolver, (s) -> kpd.initSolution.set(s));
        
        ih.init().append(
            kpd.initSolution
        );
        
        ih.iteration().append(
            kpd.iteration
        );
        
        ih.finish().append(kso.printResult(kpd.selectedItems));
    }
    
    private Statement iteration;
    private Statement defaultInit;
    private PlaceholderStatement<Statement> initSolution = new PlaceholderStatement<>();
    
    private ItemList selectedItems;
    private ItemList pendingItems;
    private Benchmark bm;
    
    private KnapsackProblemDomain(KnapsackOperators kso) {
        bm = Benchmark.getDefaultBenchmarks()[1];
        selectedItems = new ItemList();
        pendingItems = new ItemList();        
                
        ItemList remainingItems = new ItemList();
        ItemHandler toAddItem = new ItemHandler();
        
        defaultInit = block(
            kso.fillListWithBenchmark(remainingItems, bm),
            While(NOT(remainingItems::isEmpty)).Do(
                kso.setRandomItemFromList(remainingItems, toAddItem),
                kso.removeSelectedItemFromList(remainingItems, toAddItem),
                If(kso.canAddItemByWeight(selectedItems, toAddItem, bm)).then(
                    kso.addSelectedItemToList(selectedItems, toAddItem)
                )
                .Else(
                    kso.addSelectedItemToList(pendingItems, toAddItem)
                )
            )
        );
        
        ItemHandler toRemoveItem = new ItemHandler();
        
        iteration = block(
            kso.setRandomItemFromList(pendingItems, toAddItem),
            While(NOT(kso.canAddItemByWeight(selectedItems, toAddItem, bm))).Do(
                kso.setRandomItemFromList(selectedItems, toRemoveItem),
                kso.removeSelectedItemFromList(selectedItems, toRemoveItem),
                kso.addSelectedItemToList(pendingItems, toRemoveItem)
            ),
            kso.addSelectedItemToList(selectedItems, toAddItem)
        );
    }

    public ItemList getPendingItems() {
        return pendingItems;
    }

    public ItemList getSelectedItems() {
        return selectedItems;
    }

    public Benchmark getBenchmark() {
        return bm;
    }
}
