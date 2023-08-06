package edu.nn.perceptron.neuron;

import java.util.Comparator;

public class NeuronValueComparator implements Comparator<Neuron> {
    @Override
    public int compare(Neuron n1, Neuron n2) {
        if (n1.value() < n2.value())
            return -1;
        else if (n1.value() > n2.value())
            return 1;
        else
            return 0;
    }
}
