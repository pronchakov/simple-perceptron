package edu.nn.iris;

import edu.nn.perceptron.neuron.bias.BiasGenerator;

public class IrisBiasGenerator implements BiasGenerator {

    private Double value = 0.2d;

    @Override
    public double generateBias() {
        value += 0.1;
        return value;
    }
}
