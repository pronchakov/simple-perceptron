package edu.nn.iris;

import lombok.Builder;

@Builder
public record Iris(
        double sepalLength,
        double sepalWidth,
        double petalLength,
        double petalWidth,
        String type
) {
}
