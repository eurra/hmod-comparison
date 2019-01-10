
package kp.v1.simple;

import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;
import kp.core.Benchmark;
import kp.core.ItemList;
import kp.core.Item;

public class RunnerSimple {    
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
    
    private static void runBenchMark(Benchmark b, int maxIterations, PrintWriter output) {
        double maxCap = b.getMaxCapacity();
        Item[] items = b.getItems();

        long startTime = System.nanoTime();
        System.out.println("Running benchmark with " + items.length + " items and knapsack capacity of " + maxCap);
        int iterations = 0;
        ItemList selected = new ItemList();
        ItemList pending = new ItemList();

        initSolution(items, maxCap, selected, pending);        

        // Main iteration
        do {
            runIteration(maxCap, selected, pending);
            iterations++;
        }
        while(iterations < maxIterations);
        
        long endTime = System.nanoTime();
        double elapsed = (double) (endTime - startTime) / 1000000000;

        System.out.println("Benchmark finished at iteration " + iterations + " with time " + elapsed);
        System.out.println("Total profit: " + selected.getTotalValue());
        
        if(output != null)
            output.println(String.format("%.10f", elapsed));
        
        System.out.println();        
    }
    
    public static void run(int maxIterations, PrintWriter output) {
        Benchmark[] bms = Benchmark.getDefaultBenchmarks();
        runBenchMark(bms[1], maxIterations, output);
    }
    
    public static void main(String[] args) {        
        run(10000, null);
    }
}
