package edu.nn;

import edu.nn.iris.Iris;
import edu.nn.iris.IrisInputPreparator;
import edu.nn.perceptron.Perceptron;
import edu.nn.perceptron.neuron.activation.ReLu;
import edu.nn.perceptron.neuron.bias.RandomBiasGenerator;
import edu.nn.perceptron.neuron.connection.weight.RandomWeightGenerator;
import edu.nn.perceptron.neuron.error.HalfQuadraticErrorFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class Main {

    private static final double ratio = 0.8;

    public static void main(String[] args) throws IOException {
        var allData = IOUtils.readLines(new FileReader("data/iris.csv"));
        Collections.shuffle(allData);
        log.info("All data count: {}", allData.size());

        final var trainData = getTrainData(allData);
        final var checkData = getCheckData(allData);

        log.info("Train data: {}, Check data: {}", trainData.size(), checkData.size());

        final var nn = Perceptron.<Iris, String>builder()
                .inputSize(4)
                .middleLayersSizes(List.of(3))
                .outputValues(List.of("Setosa", "Versicolor", "Virginica"))
                .inputPreparator(new IrisInputPreparator())
                .weightsGenerator(new RandomWeightGenerator())
                .biasGenerator(new RandomBiasGenerator())
                .activationFunction(new ReLu())
                .errorFunction(new HalfQuadraticErrorFunction())
                .learningRate(Perceptron.DEFAULT_LEARNING_RATE)
                .build();

        checkNetwork(checkData, nn);
        trainNetwoek(trainData, nn);
        checkNetwork(checkData, nn);

        log.info("End");
    }

    private static void checkNetwork(List<Iris> checkData, Perceptron<Iris, String> nn) {
        log.info("Checking network");

        final var successCount = checkData.stream()
                .map(iris -> {
                    final var predict = nn.predict(iris);
                    return iris.type().equals(predict);
                })
                .filter(Main::onlySuccess)
                .count();

        log.info("Network was checked. Results: Success: {}, Failure: {}", successCount, checkData.size() - successCount);
    }

    private static boolean onlySuccess(boolean b) {
        return b;
    }

    private static void trainNetwoek(List<Iris> trainData, Perceptron<Iris, String> nn) {
        log.info("Training network");
        for (int i = 0; i < 300; i++) {
            trainData.forEach(iris -> nn.train(iris, iris.type()));
        }
        log.info("Network was trained");
    }

    private static List<Iris> getTrainData(List<String> allData) {
        final int trainDataSize = (int) (allData.size() * ratio);
        final var trainDataStr = allData.subList(0, trainDataSize);
        return parseData(trainDataStr);
    }

    private static List<Iris> getCheckData(List<String> allData) {
        final var trainDataSize = (int) (allData.size() * ratio);
        final var checkDataStr = allData.subList(trainDataSize, allData.size());
        return parseData(checkDataStr);
    }

    private static ArrayList<Iris> parseData(List<String> checkDataStr) {
        final var result = new ArrayList<Iris>();
        for (var check : checkDataStr) {
            final var scanner = new Scanner(check);
            scanner.useDelimiter(",");
            result.add(
                    Iris.builder()
                            .sepalLength(scanner.nextDouble())
                            .sepalWidth(scanner.nextDouble())
                            .petalLength(scanner.nextDouble())
                            .petalWidth(scanner.nextDouble())
                            .type(scanner.next())
                            .build()
            );
        }
        return result;
    }
}
