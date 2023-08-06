package edu.nn.perceptron.neuron.activation;

public interface ActivationFunction {
    double transform(double num);

    double derivative(double num);
}
