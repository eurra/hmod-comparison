
package kp.core;

public class Benchmark {
    public static Benchmark[] getDefaultBenchmarks() {
        return new Benchmark[] {
            new Benchmark(165,
                new int[] { 23, 31, 29, 44, 53, 38, 63, 85, 89, 82 }, 
                new int[] { 92, 57, 49, 68, 60, 43, 67, 84, 87, 72 }
            ),
            new Benchmark(26,
                new int[] { 12, 7, 11, 8, 9 },
                new int[] { 24, 13, 23, 15, 16 }
            ),
        };
    }
    
    private int capacity;
    private int weights[];
    private int profits[];

    public Benchmark(int capacity, int[] weights, int[] profits) {
        this.capacity = capacity;
        this.weights = weights;
        this.profits = profits;
    }
    
    public Item[] getItems() {
        Item[] res = new Item[weights.length];
        
        for(int i = 0; i < weights.length; i++) {
            Item newItem = new Item(profits[i], weights[i]);
            res[i] = newItem;
        }
        
        return res;
    }
    
    public int getMaxCapacity() {
        return capacity;
    }
}
