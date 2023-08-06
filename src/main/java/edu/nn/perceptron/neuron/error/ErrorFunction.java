package edu.nn.perceptron.neuron.error;

public interface ErrorFunction {

    double calculateError(double value, double desiredValue, double derivativeActivationFunction);

}
