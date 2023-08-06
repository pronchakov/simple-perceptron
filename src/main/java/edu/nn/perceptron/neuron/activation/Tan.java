package edu.nn.perceptron.neuron.activation;


public class Tan implements ActivationFunction {
    @Override
    public double transform(double num) {
        return Math.tan(num);
    }

    @Override
    public double derivative(double num) {
        throw new RuntimeException("Not implemented");
    }
}
