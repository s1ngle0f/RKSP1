package org.example;

import java.util.Scanner;
import java.util.concurrent.*;

public class Task2 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Введите число (или 'q' для выхода): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("q")) {
                executor.shutdown();
                break;
            }

            try {
                int number = Integer.parseInt(input);
                Future<Integer> future = executor.submit(new SquareCalculator(number));

                // Обработка результата после завершения задачи
                try {
                    int result = future.get();
                    System.out.println("Результат: " + result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите целое число.");
            }
        }

        scanner.close();
    }
}
class SquareCalculator implements Callable<Integer> {
    private int number;

    public SquareCalculator(int number) {
        this.number = number;
    }

    @Override
    public Integer call() throws Exception {
        // Имитация задержки от 1 до 5 секунд
        int delay = ThreadLocalRandom.current().nextInt(1, 6);
        TimeUnit.SECONDS.sleep(delay);
        return number * number;
    }
}