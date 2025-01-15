package dev.bebomny.beaver.beaverutils.helpers;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class DoubleRollingAverage implements RollingAverage {
    private final Queue<Double> samples;
    private final int sampleSize;
    private double total = 0.0D;

    public DoubleRollingAverage(int sampleSize) {
        this.sampleSize = sampleSize;
        this.samples = new ArrayDeque<>(this.sampleSize + 1);
    }

    public void add(double value) {
        this.total += value;
        this.samples.add(value);
        if (this.samples.size() > this.sampleSize) {
            this.total -= this.samples.remove();
        }
    }

    @Override
    public double average() {
        if (this.samples.isEmpty()) {
            return 0.0D;
        }
        return this.total / this.samples.size();
    }

    @Override
    public double min() {
        double min = 0.0D;
        for (Double sample : this.samples) {
            min = Math.min(min, sample);
        }
        return min;
    }

    @Override
    public double max() {
        double max = 0.0D;
        for (Double sample : this.samples) {
            max = Math.max(max, sample);
        }
        return max;
    }

}
