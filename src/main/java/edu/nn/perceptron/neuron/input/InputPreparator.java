package edu.nn.perceptron.neuron.input;

import java.util.List;

public interface InputPreparator<T> {
    List<Double> transform(T input);
}
