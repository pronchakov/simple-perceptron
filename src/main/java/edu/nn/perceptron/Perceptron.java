package edu.nn.perceptron;

import edu.nn.perceptron.neuron.Neuron;
import edu.nn.perceptron.neuron.NeuronValueComparator;
import edu.nn.perceptron.neuron.activation.ActivationFunction;
import edu.nn.perceptron.neuron.bias.BiasGenerator;
import edu.nn.perceptron.neuron.connection.ConnectionImpl;
import edu.nn.perceptron.neuron.connection.weight.WeightsGenerator;
import edu.nn.perceptron.neuron.error.ErrorFunction;
import edu.nn.perceptron.neuron.input.InputPreparator;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Perceptron<INPUT, OUTPUT> {

    public static final double DEFAULT_LEARNING_RATE = 0.05;

    private final InputPreparator inputPreparator;
    private final WeightsGenerator weightsGenerator;
    private final BiasGenerator biasGenerator;
    private final ActivationFunction activationFunction;
    private final ErrorFunction errorFunction;
    private final double learningRate;
    private final NeuronValueComparator neuronValueComparator = new NeuronValueComparator();
    private Layer<INPUT> input;
    private List<Layer<Neuron>> middleLayerList;
    private Layer<OUTPUT> output;

    @Builder
    private Perceptron(
            Integer inputSize,
            List<Integer> middleLayersSizes,
            List<OUTPUT> outputValues,
            InputPreparator<INPUT> inputPreparator,
            WeightsGenerator weightsGenerator,
            BiasGenerator biasGenerator,
            ActivationFunction activationFunction,
            ErrorFunction errorFunction,
            double learningRate
    ) {
        log.info("Creating neural network");

        this.inputPreparator = inputPreparator;
        this.activationFunction = activationFunction;
        this.errorFunction = errorFunction;
        this.weightsGenerator = weightsGenerator;
        this.biasGenerator = biasGenerator;
        this.learningRate = learningRate;

        log.info("Creating layers and neurons");
        initInputLayer(inputSize);
        initMiddleLayers(middleLayersSizes);
        initOutputLayer(outputValues);
    }

    private void initInputLayer(Integer size) {
        log.info("Creating input layer ({} neurons)", size);
        var currentLayer = new LayerImpl(size, biasGenerator);
        input = currentLayer;
    }

    private void initMiddleLayers(List<Integer> neuronCountsForLayers) {
        middleLayerList = new ArrayList<>(neuronCountsForLayers.size());
        for (int i = 0; i < neuronCountsForLayers.size(); i++) {
            final var layerSize = neuronCountsForLayers.get(i);
            log.info("Creating middle layer number {} ({} neurons)", i + 1, layerSize);
            var lastLayer = i == 0 ? input : middleLayerList.get(middleLayerList.size() - 1);
            var newLayer = new LayerImpl(layerSize, biasGenerator);
            createConnectionsBetweenLayers(lastLayer, newLayer);
            middleLayerList.add(newLayer);
        }
    }

    private void initOutputLayer(List<OUTPUT> outputValues) {
        log.info("Creating output layer ({} neurons)", outputValues.size());
        output = new LayerImpl(outputValues, biasGenerator);
        final var layer = middleLayerList.isEmpty() ? input : middleLayerList.get(middleLayerList.size() - 1);
        createConnectionsBetweenLayers(layer, output);
    }

    private void createConnectionsBetweenLayers(Layer<?> first, Layer<?> second) {
        for (Neuron firstLayerNeuron : first.neurons()) {
            for (Neuron secondLayerNeuron : second.neurons()) {
                var connection = new ConnectionImpl(firstLayerNeuron, secondLayerNeuron);
                connection.weight(weightsGenerator.generateWeight());
                firstLayerNeuron.rightConnections().add(connection);
                secondLayerNeuron.leftConnections().add(connection);
            }
        }
        first.next(second);
        second.previous(first);
    }

    public void train(INPUT data, OUTPUT answer) {
        var preparedInputs = inputPreparator.transform(data);
        feedForward(preparedInputs);
        updateError(answer);
        updateWeights();
        updateBias();
    }

    private void updateBias() {
        Layer<?> currentLayer = output;
        do {
            currentLayer.neurons().forEach(neuron -> neuron.leftConnections().forEach(connection -> connection.weight(connection.weight() - (connection.opposite(neuron).value() * neuron.error() * learningRate))));
            currentLayer = currentLayer.previous();
        } while (currentLayer.previous() != null);
    }

    private void updateWeights() {
        Layer<?> currentLayer = output;
        do {
            currentLayer.neurons().forEach(neuron -> neuron.bias(neuron.bias() - (neuron.error() * learningRate)));
            currentLayer = currentLayer.previous();
        } while (currentLayer.previous() != null);
    }

    private void updateError(OUTPUT answer) {
        output.neurons().forEach(outputNeuron -> {
            final var error = errorFunction.calculateError(
                    outputNeuron.value(),
                    outputNeuron.answer().equals(answer) ? 1 : 0,
                    activationFunction.derivative(outputNeuron.value())
            );
            outputNeuron.error(error);
        });

        for (int i = middleLayerList.size() - 1; i >= 0; i--) {
            final var layer = middleLayerList.get(i);
            layer.neurons().forEach(neuron -> {
                final var errorsSum = neuron.rightConnections().stream()
                        .map(c -> c.weight() * c.opposite(neuron).error())
                        .mapToDouble(value -> value)
                        .sum();

                neuron.error(errorsSum * activationFunction.derivative(neuron.value()));
            });
        }
    }

    public OUTPUT predict(INPUT data) {
        var preparedInputs = inputPreparator.transform(data);
        feedForward(preparedInputs);
        var outputNeuron = output.neurons().stream()
                .max(neuronValueComparator)
                .get();

        return outputNeuron.answer();
    }

    private void feedForward(List<Double> data) {
        if (data.size() != input.neurons().size()) {
            throw new IllegalArgumentException("Input data size is not equal to first neuron layer size");
        }

        for (int i = 0; i < data.size(); i++) {
            input.neurons().get(i).value(data.get(i));
        }

        var current = input.next();
        do {
            for (Neuron<?> n : current.neurons()) {
                var weightedSum = (n.leftConnections()).stream()
                        .mapToDouble(c -> c.weight() * c.opposite(n).value())
                        .sum();
                weightedSum += n.bias();
                var newValue = activationFunction.transform(weightedSum);
                n.value(newValue);
            }
        } while ((current = current.next()) != null);
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        sb.append("\n");
        sb.append("===\n");
        input.neurons().stream()
                .map(inputNeuron -> inputNeuron.rightConnections())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()).stream().forEach(connection -> {
                    sb.append(connection);
                    sb.append("\n");
                });

        sb.append("~~~\n");

        output.neurons().stream()
                .map(inputNeuron -> inputNeuron.leftConnections())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()).stream().forEach(connection -> {
                    sb.append(connection);
                    sb.append("\n");
                });
        sb.append("===\n");
        sb.append("\n");
        return sb.toString();
    }
}
