
package kp.v2.hmod;

import hmod.core.Statement;
import kp.core.Item;
import kp.core.ItemList;
import kp.v1.hmod.ItemHandler;

public class GreedyOperators {
    public Statement setItemByValue(ItemList il, ItemHandler ih) {
        return () -> {
            Item best = null;
            
            for(int i = 0; i < il.getItemCount(); i++){
                Item curr = il.getItemAt(i);
                
                if(best == null || curr.getValue() > best.getValue())
                    best = curr;
            }
            
            ih.setItem(best);
        };
    }
}
