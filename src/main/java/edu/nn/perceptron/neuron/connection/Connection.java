package edu.nn.perceptron.neuron.connection;

import edu.nn.perceptron.neuron.Neuron;

public interface Connection {
    double weight();

    void weight(double weight);

    Neuron<?> opposite(Neuron<?> p);
}
