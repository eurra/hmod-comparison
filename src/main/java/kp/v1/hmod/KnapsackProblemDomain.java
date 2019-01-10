
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
    public static final Parameter<KnapsackItemSelector> ITEM_SELECTOR = new Parameter<>();
    
    public static final KnapsackInitializer ORDER_BASED_INIT = Resolvable.boundTo(
        KnapsackInitializer.class,
        KnapsackProblemDomain.class,
        (kpd) -> kpd.defaultInit
    );
    
    public static final KnapsackItemSelector RANDOM_ITEM_SELECTOR = Resolvable.boundTo(
        KnapsackItemSelector.class,
        KnapsackProblemDomain.class,
        (kpd) -> kpd.defaultItemSelector
    );
    
    @LoadsComponent({
        KnapsackProblemDomain.class
    })
    public static void load(ComponentRegister cr, ParameterRegister pr, IterativeHeuristic ih) {
        KnapsackOperators kso = new KnapsackOperators();
        KnapsackProblemDomain kpd = cr.provide(new KnapsackProblemDomain(kso));        
        
        KnapsackInitializer initResolver = pr.getRequiredValue(INITIALIZER);
        pr.addBoundHandler(initResolver, (s) -> kpd.initSolution.set(s));
        
        KnapsackItemSelector selectorResolver = pr.getRequiredValue(ITEM_SELECTOR);
        pr.addBoundHandler(selectorResolver, (s) -> kpd.itemSelector.set(s));
        
        ih.init().append(block(
            kpd.initSolution,
            kso.printResult(kpd.selectedItems)
        ));
        
        ih.iteration().append(block(
            kpd.iteration/*,
            kso.printResult(kpd.selectedItems)*/
        )); 
        
        ih.finish().append(kso.printResult(kpd.selectedItems));
    }
    
    private Statement defaultItemSelector;
    private Statement defaultInit;
    private Statement iteration;
    private PlaceholderStatement<Statement> initSolution = new PlaceholderStatement<>();
    private PlaceholderStatement<Statement> itemSelector = new PlaceholderStatement<>();
    
    private ItemList selectedItems;
    private ItemList pendingItems;
    private ItemHandler selectedItem;
    private Benchmark bm;
    
    private KnapsackProblemDomain(KnapsackOperators kso) {
        bm = Benchmark.getDefaultBenchmarks()[1];
        selectedItems = new ItemList();
        pendingItems = new ItemList();
        selectedItem = new ItemHandler();
                
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
        
        defaultItemSelector = block(
            kso.setRandomItemFromList(pendingItems, selectedItem)
        );
        
        ItemHandler toRemoveItem = new ItemHandler();
        
        iteration = block(
            itemSelector,
            While(NOT(kso.canAddItemByWeight(selectedItems, selectedItem, bm))).Do(
                kso.setRandomItemFromList(selectedItems, toRemoveItem),
                kso.removeSelectedItemFromList(selectedItems, toRemoveItem),
                //kso.printItem(toRemoveItem, "removed"),
                kso.addSelectedItemToList(pendingItems, toRemoveItem)
            ),
            kso.addSelectedItemToList(selectedItems, selectedItem),
            kso.removeSelectedItemFromList(pendingItems, selectedItem)//,
            //kso.printItem(selectedItem, "added")
        );
    }

    public ItemList getPendingItems() {
        return pendingItems;
    }

    public ItemList getSelectedItems() {
        return selectedItems;
    }

    public ItemHandler getSelectedItem() {
        return selectedItem;
    }

    public Benchmark getBenchmark() {
        return bm;
    }
}
