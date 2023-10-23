import java.util.Random;
import java.util.Scanner;


public class Main {
    private static int n; // board size
    private final int populationSize;
    private final int maxGenerations;
    private final double mutationRate;
    private int[][] population;
    private final Random random;

    public Main(int n, int populationSize, int maxGenerations, double mutationRate) {
        Main.n = n;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.mutationRate = mutationRate;
        this.population = new int[populationSize][n];
        this.random = new Random();
        initPopulation();
    }

    private void initPopulation() {
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < n; j++) {
                population[i][j] = random.nextInt(n);
            }
        }
    }

    private int fitness(int[] solution) {
        int[] rows = new int[n];
        int[] diagonal1 = new int[2 * n - 1];
        int[] diagonal2 = new int[2 * n - 1];
        int conflicts = 0;
        for (int i = 0; i < n; i++) {
            int row = solution[i];
            if (rows[row] > 0 || diagonal1[i - row + n - 1] > 0 || diagonal2[i + row] > 0) {
                conflicts++;
            } else {
                rows[row]++;
                diagonal1[i - row + n - 1]++;
                diagonal2[i + row]++;
            }
        }
        return conflicts;
    }

    private int[] selectParent() {
        // Tournament selection
        int tournamentSize = 2;
        int[] tournament = new int[tournamentSize];
        for (int i = 0; i < tournamentSize; i++) {
            tournament[i] = random.nextInt(populationSize);
        }
        int bestIndex = tournament[0];
        int bestFitness = fitness(population[bestIndex]);
        for (int i = 1; i < tournamentSize; i++) {
            int index = tournament[i];
            int fitness = fitness(population[index]);
            if (fitness < bestFitness) {
                bestIndex = index;
                bestFitness = fitness;
            }
        }
        return population[bestIndex];
    }

    private int[] crossover(int[] parent1, int[] parent2) {
        int[] child = new int[n];
// One-point crossover
        int crossoverPoint = random.nextInt(n);
        for (int i = 0; i < crossoverPoint; i++) {
            child[i] = parent1[i];
        }
        for (int i = crossoverPoint; i < n; i++) {
            child[i] = parent2[i];
        }
        return child;
    }

    private void mutate(int[] solution) {
        // Random reset mutation
        for (int i = 0; i < n; i++) {
            if (random.nextDouble() <= mutationRate) {
                solution[i] = random.nextInt(n);
            }
        }
    }

    public int[] solve() {
        int generation = 0;
        int[] bestSolution = null;
        int bestFitness = Integer.MAX_VALUE;
        while (generation < maxGenerations && bestFitness > 0) {
            int[][] newPopulation = new int[populationSize][n];
            for (int i = 0; i < populationSize; i++) {
                int[] parent1 = selectParent();
                int[] parent2 = selectParent();
                int[] child = crossover(parent1, parent2);
                mutate(child);
                newPopulation[i] = child;
                int fitness = fitness(child);
                if (fitness == 0) {
                    // Solution found
                    System.out.println("Solution found in generation " + generation);
                            System.out.println();
                    return child;
                }
                if (fitness < bestFitness) {
                    bestSolution = child;
                    bestFitness = fitness;
                }
                if (bestFitness < Integer.MAX_VALUE) {
                    System.out.println("Best fitness in generation " + generation + ": " + bestFitness);
                }
            }
            population = newPopulation;
            generation++;
        }
        System.out.println("No solution found.");
        return bestSolution;
    }



    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        while (true) {

            System.out.println("Enter the number of queens:");
            System.out.println("0 for Exit");

            n = scan.nextInt();

            if (n == 0) {
                System.exit(0);
            }
//100, 10000, 50   100
            int populationSize = 1000;
            int maxGenerations = Integer.MAX_VALUE;
            double mutationRate = 100;

            long startTime = System.currentTimeMillis();

            Main ga = new Main(n, populationSize, maxGenerations, mutationRate);

            int[] solution = ga.solve();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (solution != null) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (solution[j] == i) {
                            System.out.print("Q ");
                        } else {
                            System.out.print(". ");
                        }
                    }
                    System.out.println();
                }
                System.out.println("Time taken: " + duration + "ms");
            }
             else{
                    System.out.println("No solution found.");
                }

        }
    }
}