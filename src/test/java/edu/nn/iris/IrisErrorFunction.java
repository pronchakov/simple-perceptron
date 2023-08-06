package edu.nn.iris;

import edu.nn.perceptron.neuron.error.ErrorFunction;

public class IrisErrorFunction implements ErrorFunction {
    @Override
    public double calculateError(double value, double desiredValue, double derivativeActivationFunction) {
        return (value - desiredValue) * derivativeActivationFunction;
    }
}
