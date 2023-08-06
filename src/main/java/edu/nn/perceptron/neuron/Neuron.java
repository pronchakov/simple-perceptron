package edu.nn.perceptron.neuron;

import edu.nn.perceptron.neuron.connection.Connection;

import java.util.ArrayList;
import java.util.List;

public class Neuron<T> {

    private final List<Connection> leftConnections = new ArrayList<>();
    private final List<Connection> rightConnections = new ArrayList<>();
    private double value;
    private double error;
    private T answer;
    private double bias;

    public List<Connection> leftConnections() {
        return leftConnections;
    }

    public List<Connection> rightConnections() {
        return rightConnections;
    }

    public double value() {
        return value;
    }

    public void value(double value) {
        this.value = value;
    }

    public double error() {
        return error;
    }

    public void error(double error) {
        this.error = error;
    }

    public T answer() {
        return answer;
    }

    public void answer(T answer) {
        this.answer = answer;
    }

    public double bias() {
        return bias;
    }

    public void bias(double bias) {
        this.bias = bias;
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        sb.append("n{");
        sb.append("v=");
        sb.append(value);
        if (answer != null) {
            sb.append(", a=");
            sb.append(answer);
        }
        sb.append(", e=");
        sb.append(error);
        sb.append(", b=");
        sb.append(bias);
        sb.append('}');

        return sb.toString();
    }
}
