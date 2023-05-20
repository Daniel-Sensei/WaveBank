package com.uid.progettobanca.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class randomNumbers {

    public static List<Integer> generateRandomNumbers(int count) {
        List<Integer> randomNumbers = new ArrayList<>();
        Random random = new Random();

            for (int i = 0; i < count; i++) {
            int randomNumber = random.nextInt();
            randomNumbers.add(randomNumber);
        }

            return randomNumbers;
    }
}
