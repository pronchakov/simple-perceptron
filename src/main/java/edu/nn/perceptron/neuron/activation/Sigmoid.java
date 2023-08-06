package edu.nn.perceptron.neuron.activation;

public class Sigmoid implements ActivationFunction {
    @Override
    public double transform(double num) {
        return (1 / (1 + Math.pow(Math.E, -num)));
    }

    @Override
    public double derivative(double num) {
        final var value = transform(num);
        return value * (1 - value);
    }
}
