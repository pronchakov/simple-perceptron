package edu.nn.perceptron.neuron;

import edu.nn.perceptron.Layer;

public interface ErrorBackPropagator<OUTPUT> {
    void propagateErrors(Layer<OUTPUT> fromLayer, OUTPUT answer);
}
