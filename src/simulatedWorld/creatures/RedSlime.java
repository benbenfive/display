package simulatedWorld.creatures;

import javafx.scene.paint.Color;
import simulatedWorld.World;

import java.util.concurrent.ThreadLocalRandom;

public class RedSlime extends Creature{
    public static int count;
    public RedSlime(double x, double y, World world) {
        super(x, y, 10, 30, 50, 1.2,4, 24, world);
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        name = "red slime";
        edibleTiles = new int[] {};
        drinkableTiles = new int[] {3};
        edibleCreatures = new String[] {"green slime"};
        drinkableCreatures = new String[] {"green slime"};
        dangerousCreatures = new String[] {};
        maxSize = size;
        this.x = x;
        this.y = y;
        dmg = 0.02;
        range = 20;
        health = maxHealth;
        hunger = maxHunger * rand.nextDouble(0.2, 1);
        thirst = maxThirst * rand.nextDouble(0.2, 1);
        c = new Color(0.8, 0.1, 0.1, 0.5);
        ++count;
    }

    @Override
    protected Creature birth() {
        Creature c = new RedSlime((x + closestBreedable.x) / 2, (y + closestBreedable.y) / 2, world);
        c.size = maxSize / 2;
        c.maxSize = maxSize;
        c.health = maxHealth / 10;
        c.hunger = maxHunger / 2;
        c.thirst = maxThirst / 2;
        c.child = true;
        return c;
    }
}
