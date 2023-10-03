package org.example;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[] arr = new int[1000];
        fillRandomNumbers(arr);
        long startTime = System.currentTimeMillis();
        int sequentalMax = sequentalFindMax(arr);
        long endTime = System.currentTimeMillis();
        System.out.println("Sequential Max: " + sequentalMax);
        System.out.println("Sequential Time: " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        int threadsMax = threadsFindMax(arr, 10);
        endTime = System.currentTimeMillis();
        System.out.println("Multithreaded Max: " + threadsMax);
        System.out.println("Multithreaded Time: " + (endTime - startTime) + " ms");

        ForkJoinMaxFinder forkJoinMaxFinder = new ForkJoinMaxFinder();
        startTime = System.currentTimeMillis();
        int maxForkJoin = forkJoinMaxFinder.findMax(arr);
        endTime = System.currentTimeMillis();
        System.out.println("ForkJoin Max: " + maxForkJoin);
        System.out.println("ForkJoin Time: " + (endTime - startTime) + " ms");
    }

    public static void fillRandomNumbers(int[] array){
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(-10000, 10000);
        }
    }

    public static synchronized int sequentalFindMax(int[] array) {
        int max = Integer.MIN_VALUE;
        for (int num : array) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    public static int threadsFindMax(int[] array, int parts) {
        int[] preRes = new int[parts];
        for(int i = 0; i < parts; i++){
            int finalI = i;
            new Thread(() -> {
                int count = array.length / parts;
                int startIndex = finalI * count;
                int endIndex = startIndex + count;
//                System.out.println(startIndex + " -> " + endIndex);
                preRes[finalI] = sequentalFindMax(Arrays.copyOfRange(array, startIndex, endIndex));
            }).start();
        }
        while (Arrays.stream(preRes).anyMatch(value -> value == 0)){}
        return sequentalFindMax(preRes);
    }
}