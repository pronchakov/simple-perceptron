package edu.nn.perceptron.neuron.activation;

public class ReLu implements ActivationFunction {
    @Override
    public double transform(double num) {
        return Math.max(0.0, num);
    }

    @Override
    public double derivative(double num) {
        return num <= 0.0 ? 0 : 1;
    }
}
