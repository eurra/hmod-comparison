
package kp.v1.hmod;

import hmod.core.Condition;
import hmod.core.Statement;
import kp.core.Benchmark;
import kp.core.Item;
import kp.core.ItemList;
import optefx.util.random.RandomTool;

public class KnapsackOperators {
        
    public Statement fillListWithBenchmark(ItemList il, Benchmark bm) {
        return () -> {
            Item[] items = bm.getItems();
            il.addAll(items);
        };
    }
    
    public Statement setRandomItemFromList(ItemList il, ItemHandler ih) {
        return () -> {
            Item toSet = il.getItemAt(RandomTool.getInt(il.getItemCount()));
            ih.setItem(toSet);
        };
    }
    
    public Statement removeSelectedItemFromList(ItemList il, ItemHandler ih) {
        return () -> {
            Item toRem = ih.getItem();
            il.removeItem(toRem);
        };
    }
    
    public Statement addSelectedItemToList(ItemList il, ItemHandler ih) {
        return () -> {
            Item toAdd = ih.getItem();
            il.addItem(toAdd);
        };
    }
    
    public Condition canAddItemByWeight(ItemList il, ItemHandler ih, Benchmark bm) {
        return () -> il.getTotalWeigth() + ih.getItem().getWeight() <= bm.getMaxCapacity();
    }
    
    public Statement printResult(ItemList il) {
        return () -> {
            System.out.println("Result found: " + il.getTotalValue());
        };
    }
}
