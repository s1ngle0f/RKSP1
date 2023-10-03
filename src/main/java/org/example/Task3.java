package org.example;
import java.util.Random;
import java.util.concurrent.*;

class File {
    private String fileType;
    private int fileSize;

    public File(String fileType, int fileSize) {
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public int getFileSize() {
        return fileSize;
    }
}

class FileGenerator implements Runnable {
    private BlockingQueue<File> queue;
    private Random random = new Random();

    public FileGenerator(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String[] fileTypes = {"XML", "JSON", "XLS"};
                String fileType = fileTypes[random.nextInt(fileTypes.length)];
                int fileSize = random.nextInt(91) + 10; // Размер файла от 10 до 100
                File file = new File(fileType, fileSize);
                queue.put(file);
                Thread.sleep(random.nextInt(901) + 100); // Задержка от 100 до 1000 мс
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class FileProcessor implements Runnable {
    private BlockingQueue<File> queue;
    private String fileType;

    public FileProcessor(BlockingQueue<File> queue, String fileType) {
        this.queue = queue;
        this.fileType = fileType;
    }

    @Override
    public void run() {
        while (true) {
            try {
                File file = queue.take();
                if (file.getFileType().equals(fileType)) {
                    int processingTime = file.getFileSize() * 7;
                    Thread.sleep(processingTime);
                    System.out.println("File of type " + fileType + " processed. Size: " + file.getFileSize());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Task3 {
    public static void main(String[] args) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(5);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Запускаем генератор файлов
        executor.submit(new FileGenerator(queue));

        // Запускаем обработчики файлов разных типов
        executor.submit(new FileProcessor(queue, "XML"));
        executor.submit(new FileProcessor(queue, "JSON"));
        executor.submit(new FileProcessor(queue, "XLS"));
    }
}
