package edu.nn.perceptron.neuron.connection.weight;

import java.util.Random;

public class RandomWeightGenerator implements WeightsGenerator {

    private final Random random = new Random();

    @Override
    public double generateWeight() {
        return random.nextDouble();
    }
}
