
package kp.v1.simple;

import java.util.concurrent.ThreadLocalRandom;
import kp.core.Benchmark;
import kp.core.ItemList;
import kp.core.Item;

public class Runner {
    private static double MAX_TIME = 10.0;
    private static int MAX_ITERATIONS = 10000;
    
    private static void initSolution(Item[] items, double maxCap, ItemList selected, ItemList pending) {
        ItemList remaining = new ItemList();
        remaining.addAll(items);

        while(!remaining.isEmpty()) {
            Item currItem = items[ThreadLocalRandom.current().nextInt(items.length)];
            remaining.removeItem(currItem);

            if(selected.getTotalWeigth() + currItem.getWeight() <= maxCap)
                selected.addItem(currItem);
            else
                pending.addItem(currItem);
        }
    }
    
    private static void runIteration(double maxCap, ItemList selected, ItemList pending) {
        Item toAdd = pending.getItemAt(ThreadLocalRandom.current().nextInt(pending.getItemCount()));

        while(selected.getTotalWeigth() + toAdd.getWeight() > maxCap) {
            Item toRemove = selected.getItemAt(ThreadLocalRandom.current().nextInt(selected.getItemCount()));
            selected.removeItem(toRemove);
            pending.addItem(toRemove);
        }

        selected.addItem(toAdd);
    }
    
    private static void runBenchMark(Benchmark b) {
        double maxCap = b.getMaxCapacity();
        Item[] items = b.getItems();

        double startTime = System.currentTimeMillis();
        double currentTime;
        System.out.println("Running benchmark with " + items.length + " items and knapsack capacity of " + maxCap);
        int iterations = 0;
        ItemList selected = new ItemList();
        ItemList pending = new ItemList();

        initSolution(items, maxCap, selected, pending);
        

        // Main iteration
        do {
            runIteration(maxCap, selected, pending);

            currentTime = (System.currentTimeMillis() - startTime) / 1000.0;
            iterations++;
        }
        while(iterations < MAX_ITERATIONS && currentTime < MAX_TIME);

        System.out.println("Benchmark finished at iteration " + iterations + " with time " + currentTime);
        System.out.println("Total profit: " + selected.getTotalValue());
        System.out.println();        
    }
    
    public static void main(String[] args) {        
        Benchmark[] bms = Benchmark.getDefaultBenchmarks();
        runBenchMark(bms[1]);
    }
}
