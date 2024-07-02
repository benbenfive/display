package simulatedWorld.creatures;

import javafx.scene.paint.Color;
import simulatedWorld.World;

import java.util.concurrent.ThreadLocalRandom;

public class Slime extends Creature {
    public static int count;

    public Slime(double x, double y, World world) {
        super(x, y, 5, 10, 20, 1,5, 16, world);
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        name = "green slime";
        edibleTiles = new int[] {1};
        drinkableTiles = new int[] {3};
        edibleCreatures = new String[] {};
        drinkableCreatures = new String[] {};
        dangerousCreatures = new String[] {"red slime"};
        maxSize = size;
        this.x = x;
        this.y = y;
        dmg = 0.001;
        range = 10;
        health = maxHealth;
        hunger = maxHunger * rand.nextDouble(0.2, 1);
        thirst = maxThirst * rand.nextDouble(0.2, 1);
        c = new Color(0.05, 0.5, 0.3, 0.5);
        ++count;
    }

    @Override
    protected Creature birth() {
        Creature c = new Slime((x + closestBreedable.x) / 2, (y + closestBreedable.y) / 2, world);
        c.size = maxSize / 2;
        c.maxSize = maxSize;
        c.health = maxHealth / 10;
        c.hunger = maxHunger / 2;
        c.thirst = maxThirst / 2;
        c.child = true;
        return c;
    }

    @Override
    public void die() {
        --count;
        world.dead(this);
    }
}
