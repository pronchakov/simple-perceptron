package edu.nn.perceptron.neuron.activation;


public class Tan implements ActivationFunction {
    @Override
    public double transform(double num) {
        return Math.tan(num);
    }

    @Override
    public double derivative(double num) {
        return 1 / (Math.pow(Math.cos(num), 2.0));
    }
}
