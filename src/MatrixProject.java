import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixProject {

    // Rozmiar macierzy. 1500x1500 to wystarczająco dużo, żeby procesor się spocił!
    private static final int SIZE = 1500;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Generowanie macierzy o rozmiarze " + SIZE + "x" + SIZE + "...");
        double[][] matrixA = generateMatrix();
        double[][] matrixB = generateMatrix();
        double[][] resultClassical = new double[SIZE][SIZE];
        double[][] resultPool = new double[SIZE][SIZE];

        System.out.println("Rozpoczynam obliczenia...\n");

        // --- PODEJŚCIE 1: KLASYCZNE WĄTKI (Klasa Thread) ---
        System.out.println("1. Testowanie podejścia klasycznego (new Thread)...");
        long startClassical = System.currentTimeMillis();

        List<Thread> threads = new ArrayList<>();
        // Tworzymy NOWY wątek dla każdego wiersza macierzy (1500 wątków!)
        for (int i = 0; i < SIZE; i++) {
            Thread t = new Thread(new RowMultiplierTask(matrixA, matrixB, resultClassical, i));
            threads.add(t);
            t.start();
        }

        // Czekamy aż wszystkie 1500 wątków skończy pracę
        for (Thread t : threads) {
            t.join();
        }

        long endClassical = System.currentTimeMillis();
        System.out.println("Czas wykonania klasycznie: " + (endClassical - startClassical) + " ms\n");


        // --- PODEJŚCIE 2: WYSOKOPOZIOMOWE (ExecutorService) ---
        System.out.println("2. Testowanie puli wątków (ExecutorService)...");

        // Tworzymy pulę wątków wielkości liczby rdzeni Twojego procesora
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Wykryto " + cores + " rdzeni. Tworzę pulę " + cores + " wątków.");
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        long startPool = System.currentTimeMillis();

        // Zlecamy 1500 zadań, ale obsługuje je tylko stała liczba wątków z puli
        for (int i = 0; i < SIZE; i++) {
            executor.submit(new RowMultiplierTask(matrixA, matrixB, resultPool, i));
        }

        // Mówimy puli, że nie będzie więcej zadań i czekamy na zakończenie
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        long endPool = System.currentTimeMillis();
        System.out.println("Czas wykonania z pulą wątków: " + (endPool - startPool) + " ms");
    }

    // --- KLASA ZADANIA (Implementuje Runnable) ---
    static class RowMultiplierTask implements Runnable {
        private final double[][] A;
        private final double[][] B;
        private final double[][] C;
        private final int row;

        public RowMultiplierTask(double[][] A, double[][] B, double[][] C, int row) {
            this.A = A;
            this.B = B;
            this.C = C;
            this.row = row;
        }

        @Override
        public void run() {
            // Klasyczny algorytm mnożenia macierzy dla jednego konkretnego wiersza
            for (int j = 0; j < SIZE; j++) {
                double sum = 0;
                for (int k = 0; k < SIZE; k++) {
                    sum += A[row][k] * B[k][j];
                }
                C[row][j] = sum;
            }
        }
    }

    // --- METODA POMOCNICZA DO GENEROWANIA DANYCH ---
    private static double[][] generateMatrix() {
        Random random = new Random();
        double[][] matrix = new double[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrix[i][j] = random.nextDouble() * 10; // Losowe liczby od 0 do 10
            }
        }
        return matrix;
    }
}