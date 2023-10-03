package org.example;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinMaxFinder extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 1000;
    private int[] array;
    private int start;
    private int end;

    public ForkJoinMaxFinder() {}

    public ForkJoinMaxFinder(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= THRESHOLD) {
            int max = Integer.MIN_VALUE;
            for (int i = start; i < end; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (array[i] > max) {
                    max = array[i];
                }
            }
            return max;
        } else {
            int mid = (start + end) / 2;
            ForkJoinMaxFinder leftTask = new ForkJoinMaxFinder(array, start, mid);
            ForkJoinMaxFinder rightTask = new ForkJoinMaxFinder(array, mid, end);
            leftTask.fork();
            int rightResult = rightTask.compute();
            int leftResult = leftTask.join();
            return Math.max(leftResult, rightResult);
        }
    }

    public static int findMax(int[] array) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ForkJoinMaxFinder(array, 0, array.length));
    }
}
