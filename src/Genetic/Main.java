package Genetic;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private Random random = new Random();
    private List<Thing> things;
    private int maxWeight;
    private Backpack result;
    private List<Backpack> backpacks;
    private int generationCount; // Количество поколений
    private int individualCount; // Количество геномов в поколении
    private int selectionBest; // Сколько нужно отобрать экзепляров после каждой мутации
    private double mutationPercent; // Вероятность мутации

    public Main(int maxWeight, int generationCount, int individualCount, int selectionBest, double mutationPercent, List<Thing> things) {
        this.maxWeight = maxWeight;
        this.generationCount = generationCount;
        this.individualCount = individualCount;
        this.selectionBest = selectionBest;
        this.mutationPercent = mutationPercent;
        this.things = things;
    }

    public class Backpack {
        boolean[] backpack;
        int load; // Вес этого варианта
        int value; // Ценность варианта

        Backpack() {
            load = 0;
            value = 0;
            backpack = new boolean[things.size()];
            for (int i = 0; i < things.size(); i++) {
                backpack[i] = random.nextBoolean();
                if (backpack[i]) {
                    load += things.get(i).getWeight();
                    value += things.get(i).getCost();
                }
            }
            if (load > maxWeight) value = -1;
        }

        Backpack(boolean[] backpack, int load, int value) {
            this.backpack = backpack;
            this.load = load;
            this.value = value;
        }

        Backpack mutate() {
            boolean[] mutant = new boolean[things.size()];
            load = 0;
            value = 0;
            for (int i = 0; i < things.size(); i++) {
                mutant[i] = random.nextBoolean() ? backpack[i] : random.nextBoolean();
                if (mutant[i]) {
                    load += things.get(i).getWeight();
                    value += things.get(i).getCost();
                }
            }
            if (load > maxWeight) value = -1;
            return new Backpack(mutant, load, value);
        }

        private Backpack crossing(Backpack other) {
            boolean[] child = new boolean[things.size()];
            load = 0;
            value = 0;
            for (int i = 0; i < things.size(); i++) {
                if (backpack[i] == other.backpack[i]) {
                    child[i] = backpack[i];
                    if (child[i]) {
                        load += things.get(i).getWeight();
                        value += things.get(i).getCost();
                    }
                } else {
                    child[i] = random.nextBoolean();
                    if (child[i]) {
                        load += things.get(i).getWeight();
                        value += things.get(i).getCost();
                    }
                }
            }
            if (load > maxWeight) value = -1;
            return new Backpack(child, load, value);
        }
    }

    Backpack fillBackpack() {
        backpacks = new ArrayList<>();
        for (int i = 0; i < individualCount; i++) backpacks.add(new Backpack());
        result = new Backpack();
        for (int i = 0; i < generationCount; i++) {
            List<Backpack> crossing = generateCrossing();
            for (Backpack backpack : crossing) {
                if (random.nextDouble() < mutationPercent) backpack = backpack.mutate();
            }
        }
        return result;
    }

    private List<Backpack> generateCrossing() {
        List<Backpack> best = new ArrayList<>();
        backpacks.sort((Backpack p1, Backpack p2) -> Integer.compare(p2.value, p1.value));
        backpacks = backpacks.subList(0, selectionBest);
        if (backpacks.get(0).value > result.value) result = backpacks.get(0);

        for (int i = 0; i < individualCount; i++) {
            Backpack parent1 = backpacks.get(random.nextInt(selectionBest));
            Backpack parent2 = backpacks.get(random.nextInt(selectionBest));
            best.add(parent1.crossing(parent2));
        }
        return best;
    }
}

