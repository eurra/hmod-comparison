
package kp.core;

import java.util.ArrayList;

public class ItemList {
    private ArrayList<Item> items;
    private double totalWeigth;
    private double totalValue;

    public ItemList() {
        this.items = new ArrayList<>();
    }
    
    public int getItemCount() {
        return items.size();
    }
    
    public boolean isEmpty() {
        return getItemCount() == 0;
    }
    
    public double getTotalWeigth() {
        return totalWeigth;
    }
    
    public double getTotalValue() {
        return totalValue;
    }
    
    public void addItem(Item i) {
        items.add(i);
        totalWeigth += i.getWeight();
        totalValue += i.getValue();
    }
    
    public void removeItem(Item i) {
        items.remove(i);
        totalWeigth -= i.getWeight();
        totalValue -= i.getValue();
    }
    
    public void addAll(Item[] items) {
        for(int i = 0; i < items.length; i++)
            addItem(items[i]);
    }
    
    public boolean hasItem(Item i) {
        return items.contains(i);
    }
    
    public Item getItemAt(int i) {
        return items.get(i);
    }
}
