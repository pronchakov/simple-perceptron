package edu.nn.perceptron.neuron.connection;

import edu.nn.perceptron.neuron.Neuron;

public class ConnectionImpl implements Connection {

    private final Neuron first;
    private final Neuron second;
    private double weight;

    public ConnectionImpl(Neuron first, Neuron second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public double weight() {
        return weight;
    }

    @Override
    public void weight(double weight) {
        this.weight = weight;
    }

    @Override
    public Neuron opposite(Neuron p) {
        if (p == null) throw new IllegalArgumentException("Cannot locate opposite neuron to null");
        if (p == first) {
            return second;
        } else if (p == second) {
            return first;
        } else {
            throw new IllegalArgumentException("Given neuron is not part of this connection");
        }
    }

    @Override
    public String toString() {
        return "c{" +
                first +
                " --- " +
                "w=" + weight +
                " --- " +
                second +
                '}';
    }
}