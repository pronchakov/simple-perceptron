package edu.nn.perceptron;

import edu.nn.perceptron.neuron.Neuron;
import edu.nn.perceptron.neuron.bias.BiasGenerator;

import java.util.ArrayList;
import java.util.List;

public class LayerImpl<T> implements edu.nn.perceptron.Layer<T> {

    private final List<Neuron<T>> neurons;
    private Layer<?> previous;
    private Layer<?> next;

    public LayerImpl(int size, BiasGenerator biasGenerator) {
        neurons = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            final var neuron = new Neuron<T>();
            neuron.bias(biasGenerator.generateBias());
            neurons.add(neuron);
        }
    }

    public LayerImpl(List<T> outputValues, BiasGenerator biasGenerator) {
        neurons = new ArrayList<>(outputValues.size());
        for (int i = 0; i < outputValues.size(); i++) {
            final var neuron = new Neuron<T>();
            neuron.bias(biasGenerator.generateBias());
            neuron.answer(outputValues.get(i));
            neurons.add(neuron);
        }
    }

    @Override
    public Layer<?> previous() {
        return previous;
    }

    @Override
    public void previous(Layer<?> previous) {
        this.previous = previous;
    }

    @Override
    public Layer<?> next() {
        return next;
    }

    @Override
    public void next(Layer<?> next) {
        this.next = next;
    }

    @Override
    public int size() {
        return neurons.size();
    }

    @Override
    public List<Neuron<T>> neurons() {
        return neurons;
    }
}
