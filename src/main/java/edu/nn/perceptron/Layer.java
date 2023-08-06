package edu.nn.perceptron;

import edu.nn.perceptron.neuron.Neuron;

import java.util.List;

public interface Layer<T> {
    Layer<?> previous();

    void previous(Layer<?> layer);

    Layer<?> next();

    void next(Layer<?> layer);

    int size();

    List<Neuron<T>> neurons();
}
