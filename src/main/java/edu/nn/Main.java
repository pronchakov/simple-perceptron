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
        List<String> allData = IOUtils.readLines(new FileReader("data/iris.csv"));
        Collections.shuffle(allData);
        log.info("All data count: {}", allData.size());

        var trainData = getTrainData(allData);
        var checkData = getCheckData(allData);

        log.info("Train data: {}, Check data: {}", trainData.size(), checkData.size());

        Perceptron<Iris, String> nn = Perceptron.<Iris, String>builder()
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

        trainNetwoek(trainData, nn);
        checkNetwork(checkData, nn);

        log.info("End");
    }

    private static void checkNetwork(List<Iris> checkData, Perceptron<Iris, String> nn) {
        log.info("Checking network");
        List<String> results = new ArrayList<>();
        for (Iris iris : checkData) {
            final String predict = nn.predict(iris);

            if (iris.type().equals(predict)) {
                results.add("Success");
            } else {
                results.add("Failure");
            }
        }
        log.info(
                "Network was checked. Results:Success: {}, Failure: {}",
                results.stream()
                        .filter(s -> s.equals("Success"))
                        .count(),
                results.stream()
                        .filter(s -> s.equals("Failure"))
                        .count()
        );
    }

    private static void trainNetwoek(List<Iris> trainData, Perceptron<Iris, String> nn) {
        log.info("Training network");
        for (int i = 0; i < 300; i++) {
            for (Iris iris : trainData) {
                nn.train(iris, iris.type());
            }
        }
        log.info("Network was trained");
    }

    private static List<Iris> getTrainData(List<String> allData) {
        int trainDataSize = (int) (allData.size() * ratio);
        final List<String> trainDataStr = allData.subList(0, trainDataSize);
        return parseData(trainDataStr);
    }

    private static List<Iris> getCheckData(List<String> allData) {
        int trainDataSize = (int) (allData.size() * ratio);
        final List<String> checkDataStr = allData.subList(trainDataSize, allData.size());
        return parseData(checkDataStr);
    }

    private static ArrayList<Iris> parseData(List<String> checkDataStr) {
        final var result = new ArrayList<Iris>();
        for (String check : checkDataStr) {
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
