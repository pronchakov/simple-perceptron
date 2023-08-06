package edu.nn.iris;

import edu.nn.perceptron.neuron.connection.weight.WeightsGenerator;

public class IrisWeightGenerator implements WeightsGenerator {

    private Double value = 0.2d;

    @Override
    public double generateWeight() {
        value += 0.05;
        return value;
    }
}
