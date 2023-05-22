package com.uid.progettobanca.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomNumbers {

    public static String generateRandomNumbers(int count) {
        List<Integer> randomNumbers = new ArrayList<>();
        Random random = new Random();

            for (int i = 0; i < count; i++) {
            int randomNumber = random.nextInt(10);
            randomNumbers.add(randomNumber);
        }
        String result = randomNumbers.stream()
                .map(Object::toString)
                .collect(Collectors.joining(""));

            return result;
    }
}
