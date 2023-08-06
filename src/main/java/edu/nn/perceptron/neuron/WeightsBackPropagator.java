package edu.nn.perceptron.neuron;

import edu.nn.perceptron.Layer;

public interface WeightsBackPropagator<OUTPUT> {
    void propagateWeights(Layer<OUTPUT> fromLayer);
}
