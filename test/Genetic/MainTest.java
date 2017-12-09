package Genetic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MainTest {
    @Test
    public void fill(){
        List<Thing> things = new ArrayList<>();
        things.add(new Thing(1, 7));
        things.add(new Thing(2, 3));
        things.add(new Thing(20, 10));
        things.add(new Thing(15, 5));
        things.add(new Thing(5, 6));

        Main main = new Main(20, 5, 100,10, 0.1, things);
        Main.Backpack result = main.fillBackpack();
        System.out.println(result.value);
        System.out.println(result.load);
    }
}