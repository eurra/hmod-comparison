
package kp.core;

public class Item {
    private double value;
    private double weight;

    public Item(double value, double weight) {
        this.value = value;
        this.weight = weight;
    }

    public double getValue() {
        return value;
    }

    public double getWeight() {
        return weight;
    }
}
