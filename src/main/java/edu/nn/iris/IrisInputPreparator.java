package edu.nn.iris;

import edu.nn.perceptron.neuron.input.InputPreparator;

import java.util.List;

public class IrisInputPreparator implements InputPreparator<Iris> {

    @Override
    public List<Double> transform(Iris iris) {
        return List.of(
                iris.sepalLength() / 10,
                iris.sepalWidth() / 10,
                iris.petalLength() / 10,
                iris.petalWidth() / 10
        );
    }
}
