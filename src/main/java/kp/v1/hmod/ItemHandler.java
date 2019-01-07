
package kp.v1.hmod;

import kp.core.Item;

public class ItemHandler {
    private Item item;

    public ItemHandler() {
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    
    public Item getItem() {
        return item;
    }
}
