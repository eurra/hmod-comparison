
package kp.v2.hmod;

import hmod.core.Statement;
import kp.v1.hmod.KnapsackProblemDomain;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import static hmod.core.FlowchartFactory.*;
import kp.core.ItemList;
import kp.v1.hmod.ItemHandler;
import kp.v1.hmod.KnapsackInitializer;
import kp.v1.hmod.KnapsackItemSelector;
import kp.v1.hmod.KnapsackOperators;
import optefx.loader.Resolvable;

public class KnapsackGreedy {
    public static final KnapsackInitializer GREEDY_INIT = Resolvable.boundTo(
        KnapsackInitializer.class,
        KnapsackGreedy.class,
        (kg) -> kg.greedyInit
    );
    
    public static final KnapsackItemSelector GREEDY_ITEM_SELECTOR = Resolvable.boundTo(
        KnapsackItemSelector.class,
        KnapsackGreedy.class,
        (kg) -> kg.greedySelector
    );    
    
    @LoadsComponent({
        KnapsackGreedy.class
    })
    public static void load(ComponentRegister cr, KnapsackProblemDomain kpd) {
        cr.provide(new KnapsackGreedy(kpd));
    }

    private Statement greedySelector;
    private Statement greedyInit;
    
    public KnapsackGreedy(KnapsackProblemDomain kpd) {
        GreedyOperators go = new GreedyOperators();
        KnapsackOperators kso = new KnapsackOperators();
        
        ItemList remainingItems = new ItemList();
        ItemHandler toAddItem = new ItemHandler();
        
        greedyInit = block(
            kso.fillListWithBenchmark(remainingItems, kpd.getBenchmark()),
            While(NOT(remainingItems::isEmpty)).Do(
                go.setItemByValue(remainingItems, toAddItem),
                kso.removeSelectedItemFromList(remainingItems, toAddItem),
                If(kso.canAddItemByWeight(kpd.getSelectedItems(), toAddItem, kpd.getBenchmark())).then(
                    kso.addSelectedItemToList(kpd.getSelectedItems(), toAddItem)
                )
                .Else(
                    kso.addSelectedItemToList(kpd.getPendingItems(), toAddItem)
                )
            )
        );
        
        greedySelector = block(
            go.setItemByValue(kpd.getPendingItems(), kpd.getSelectedItem())
        );
    }
}
