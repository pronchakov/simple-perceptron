package edu.nn.perceptron.neuron.bias;

import java.util.Random;

public class RandomBiasGenerator implements BiasGenerator {

    private final Random random = new Random();

    @Override
    public double generateBias() {
        return random.nextDouble();
    }
}
