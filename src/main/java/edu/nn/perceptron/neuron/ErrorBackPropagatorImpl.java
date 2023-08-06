package edu.nn.perceptron.neuron;

import edu.nn.perceptron.Layer;

public class ErrorBackPropagatorImpl<OUTPUT> implements ErrorBackPropagator<OUTPUT> {
    @Override
    public void propagateErrors(Layer<OUTPUT> fromLayer, OUTPUT answer) {
        Layer<?> layer = fromLayer;
//        for (var p : layer.neurons()) {
//            var desiredValue = answer.equals(p.desiredAnswer()) ? 1.0f : 0.0f;
//            p.error(desiredValue - p.value());
//        }
        while ((layer = layer.previous()) != null) {
            for (var p : layer.neurons()) {
                var sumAllWeights = p.rightConnections().stream().mapToDouble(value -> value.weight()).sum();
                p.error(p.rightConnections().stream().mapToDouble(c -> c.opposite(p).error() * (c.weight() / sumAllWeights)).sum());
            }
        }
    }
}
