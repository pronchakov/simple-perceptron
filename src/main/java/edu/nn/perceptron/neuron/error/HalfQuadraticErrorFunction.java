package edu.nn.perceptron.neuron.error;

public class HalfQuadraticErrorFunction implements ErrorFunction {
    @Override
    public double calculateError(double value, double desiredValue, double derivativeActivationFunction) {
        return ((Math.pow(value - desiredValue, 2.0) / 2) * derivativeActivationFunction) * (value > desiredValue ? 1.0 : -1.0);
    }
}
