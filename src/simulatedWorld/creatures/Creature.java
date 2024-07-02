package simulatedWorld.creatures;

import simulatedWorld.Chunk;
import simulatedWorld.Main;
import simulatedWorld.World;
import javafx.scene.paint.Color;
import sun.awt.image.ImageWatched;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Creature extends Positioned{
    private ThreadLocalRandom rand = ThreadLocalRandom.current();
    protected double health, maxHealth, hunger, maxHunger, thirst, maxThirst, speed, sight, dmg, range;
    protected int size, idle = rand.nextInt(10), lastGoal, continueTime = -1, maxSize;
    protected double dx = -1, dy = -1, xx = -1, yy = -1, fear = 0, comfort = 1;
    protected World world;
    protected String name;
    protected int[] edibleTiles, drinkableTiles;
    protected String[] edibleCreatures, drinkableCreatures, dangerousCreatures;
    protected Color c;
    protected Positioned closestEdible, closestDrinkable,
            closestDanger, closestDangerE, closestDangerD;
    protected Creature closestBreedable;
    protected boolean gender = rand.nextBoolean(), child = false;
    private int chunkX, chunkY;

    public Creature(double x, double y, double maxHealth, double maxHunger, double maxThirst, double speed, double sight, int size, World world) {
        super(x, y);
        this.maxHealth = maxHealth;
        this.maxHunger = maxHunger;
        this.maxThirst = maxThirst;
        this.speed = speed;
        this.sight = sight;
        this.size = size;
        this.world = world;
        chunkX = (int)x / Chunk.chunkSize / World.tileWidth;
        chunkY = (int)y / Chunk.chunkSize / World.tileWidth;
    }

    public void update(){
        if(health < 0){
            die();
            return;
        }

        LinkedList<Creature> visible = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            double degree = rand.nextDouble(Math.PI / 2);
            double distance = rand.nextDouble(sight * World.tileWidth - 10 * 2);
            double checkx = x + Math.cos(degree) * distance * (rand.nextInt(2) - 0.5) * 2;
            double checky = y + Math.sin(degree) * distance * (rand.nextInt(2) - 0.5) * 2;
            visible.addAll(world.getCreaturesNear(checkx, checky, 10));
        }

        ArrayList<Double> visibleTiles = new ArrayList<>(7);
        for (int i = 0; i < 5; i++) {
            double degree = rand.nextDouble(Math.PI / 2);
            double distance = rand.nextDouble(sight * World.tileWidth);
            double checkx = x + Math.cos(degree) * distance * (rand.nextInt(2) - 0.5) * 2;
            double checky = y + Math.sin(degree) * distance * (rand.nextInt(2) - 0.5) * 2;
            visibleTiles.add(checkx);
            visibleTiles.add(checky);
        }

        LinkedList<Positioned> sensedEdible = new LinkedList<>(), sensedDrinkable = new LinkedList<>(),
                sensedDanger = new LinkedList<>();
        LinkedList<Creature> sensedBreedable = new LinkedList<>();
        for(Creature c : visible){
            for (String s : edibleCreatures) {
                if(s.equals(c.getName())){
                    sensedEdible.add(c);
                }
            }
            for(String s : drinkableCreatures){
                if(s.equals(c.getName())){
                    sensedDrinkable.add(c);
                }
            }
            for(String s : dangerousCreatures){
                if(s.equals(c.getName())){
                    sensedDanger.add(c);
                }
            }
            if(c.getName().equals(name) && gender != c.gender && !c.child){
                sensedBreedable.add(c);
            }
        }
        for (int i = 0; i < visibleTiles.size(); i += 2) {
            int t = world.getTile(visibleTiles.get(i), visibleTiles.get(i + 1));
            for (int j : edibleTiles) {
                if(j == t){
                    sensedEdible.add(new Positioned(visibleTiles.get(i), visibleTiles.get(i + 1)));
                }
            }
            for(int j : drinkableTiles){
                if(j == t){
                    sensedDrinkable.add(new Positioned(visibleTiles.get(i), visibleTiles.get(i + 1)));
                }
            }
        }

        double dist, edibDist = -1, dEdibDist = -1, drinkDist = -1, dDrinkDist = -1, dDist = -1, breedDist = -1;

        if(closestEdible == null){
                edibDist = sight * World.tileWidth * 3;
        }else {
            edibDist = Math.sqrt(Math.pow(closestEdible.x - x, 2) + Math.pow(closestEdible.y - y, 2));
        }
        if(closestDangerE == null || closestEdible == null ){
            dEdibDist = sight * World.tileWidth * 3;
        }else{
            dEdibDist = Math.sqrt(Math.pow(closestDangerE.x - closestEdible.x, 2) +
                    Math.pow(closestDangerE.y - closestEdible.y, 2));
        }
        if(closestDrinkable == null){
            drinkDist = sight * World.tileWidth * 3;
        }else {
            drinkDist = Math.sqrt(Math.pow(closestDrinkable.x - x, 2) + Math.pow(closestDrinkable.y - y, 2));
        }
        if(closestDangerD == null || closestDrinkable == null){
            dDrinkDist = sight * World.tileWidth * 3;
        }else{
            dDrinkDist = Math.sqrt(Math.pow(closestDangerD.x - closestDrinkable.x, 2) +
                    Math.pow(closestDangerD.y - closestDrinkable.y, 2));
        }
        if (closestDanger == null) {
            dDist = sight * World.tileWidth * 3;
        }else{
            dDist = Math.sqrt(Math.pow(closestDanger.x - x, 2) + Math.pow(closestDanger.y - y, 2));
        }
        if(closestBreedable == null){
            breedDist = sight * World.tileWidth * 3;
        }else{
            breedDist = Math.sqrt(Math.pow(closestBreedable.x - x, 2) + Math.pow(closestBreedable.y - y, 2));
        }

        for(Positioned c : sensedEdible){
            dist = Math.sqrt(Math.pow(c.x - x, 2) + Math.pow(c.y - y, 2));
            if (closestEdible == null || dist < edibDist) {
                closestEdible = c;
                edibDist = dist;
            }
        }
        for(Positioned c : sensedDrinkable){
            dist = Math.sqrt(Math.pow(c.x - x, 2) + Math.pow(c.y - y, 2));
            if (closestDrinkable == null || dist < drinkDist) {
                closestDrinkable = c;
                drinkDist = dist;
            }
        }
        for(Positioned c : sensedDanger){
            if(closestDanger == null){
                closestDanger = c;
                dDist = Math.sqrt(Math.pow(closestDanger.x - x, 2) + Math.pow(closestDanger.y - y, 2));
            }else {
                dist = Math.sqrt(Math.pow(c.x - x, 2) + Math.pow(c.y - y, 2));
                if (dist < dDist) {
                    closestDanger = c;
                    dDist = dist;
                }

                if(closestEdible == null){
                    dEdibDist = sight * World.tileWidth * 3;
                }else if(closestDangerE == null){
                    closestDangerE = c;
                    dEdibDist = Math.sqrt(Math.pow(closestDangerE.x - closestEdible.x, 2) +
                            Math.pow(closestDangerE.y - closestEdible.y, 2));
                }else {
                    dist = Math.sqrt(Math.pow(c.x - closestEdible.x, 2) + Math.pow(c.y - closestEdible.y, 2));
                    dEdibDist = Math.sqrt(Math.pow(closestDangerE.x - closestEdible.x, 2) +
                            Math.pow(closestDangerE.y - closestEdible.y, 2));
                    if (dist < dEdibDist) {
                        closestDangerE = c;
                        dEdibDist = dist;
                    }
                }

                if(closestDrinkable == null){
                    dDrinkDist = sight * World.tileWidth * 3;
                }else if(closestDangerD == null) {
                    closestDangerD = c;
                    dDrinkDist = Math.sqrt(Math.pow(closestDangerE.x - closestDrinkable.x, 2) +
                            Math.pow(closestDangerE.y - closestDrinkable.y, 2));
                }else{
                    dist = Math.sqrt(Math.pow(c.x - closestDrinkable.x, 2) + Math.pow(c.y - closestDrinkable.y, 2));
                    dDrinkDist = Math.sqrt(Math.pow(closestDangerE.x - closestDrinkable.x, 2) +
                            Math.pow(closestDangerE.y - closestDrinkable.y, 2));
                    if (dist < dDrinkDist) {
                        closestDangerE = c;
                        dDrinkDist = dist;
                    }
                }
            }
        }
        for(Creature c : sensedBreedable){
            dist = Math.sqrt(Math.pow(c.x - x, 2) + Math.pow(c.y - y, 2));
            if(closestBreedable == null || dist < breedDist){
                closestBreedable = c;
                breedDist = dist;
            }
        }


        if(fear < 1){
            fear = 1;
        }

        double edibleScore = maxHunger / hunger *
                        (1 - (1 / (1 + Math.pow(Math.E, -(edibDist - sight * World.tileWidth / 2)))) / 4) *
                        (1 / (1 + Math.pow(Math.E, -(dEdibDist - sight * World.tileWidth / 2)))) / Math.sqrt(comfort),
                drinkableScore = maxThirst / thirst *
                        (1 - (1 / (1 + Math.pow(Math.E, -(drinkDist - sight * World.tileWidth / 2)))) / 4) *
                        (1 / (1 + Math.pow(Math.E, -(dDrinkDist - sight * World.tileWidth / 2)))) / Math.sqrt(comfort),
                dangerScore = Math.pow(sight * World.tileWidth / dDist, 2) * fear * 1.1,
                idleScore = 2,
                breedableScore = hunger / maxHunger * thirst / maxThirst * health / maxHealth  *
                        ((3 * sight * World.tileWidth - breedDist) / (3 * sight * World.tileWidth) * 8) / comfort;

        if(lastGoal == 1 || lastGoal == 5){
            edibleScore *= 1.5;
        }else if(lastGoal == 2 || lastGoal == 6){
            drinkableScore *= 1.5;
        }else if(lastGoal == 3){
            dangerScore *= 1.5;
        }else if(lastGoal == 7 || lastGoal == 8){
            breedableScore *= 1.5;
        }

        if(world.getPlayer().spectate == this){
            Main.edible.setText("" + edibleScore);
            Main.drinkable.setText("" + drinkableScore);
            Main.danger.setText("" + dangerScore);
            Main.idle.setText("" + idleScore);
            Main.breedable.setText("" + breedableScore);
        }


        if(comfort > 1 && rand.nextInt(10000) == 0){
            --comfort;
        }
        fear -= 10;
        --continueTime;
        if(edibleScore > drinkableScore && edibleScore > dangerScore && edibleScore > idleScore && edibleScore > breedableScore){
            if(closestEdible == null){
                if(lastGoal != 1 || continueTime < 0) {
                    double degree = rand.nextDouble(Math.PI / 2);
                    double distance = sight * World.tileWidth * 100;
                    xx = x + Math.cos(degree) * distance * (rand.nextInt(2) - 0.5) * 2;
                    yy = y + Math.sin(degree) * distance * (rand.nextInt(2) - 0.5) * 2;
                    continueTime = rand.nextInt(500, 1500);
                }
                lastGoal = 1;
            }else {
                continueTime = 0;
                xx = closestEdible.x;
                yy = closestEdible.y;
                if (closestEdible instanceof Creature) {
                    if (edibDist < speed + (size + ((Creature) closestEdible).size) / 2.0 ||
                            (lastGoal == 5 && edibDist < range + (size + ((Creature) closestEdible).size) / 2.0)) {
                        xx = -1;
                        yy = -1;
                        lastGoal = 5;
                    }else{
                        lastGoal = 1;
                    }
                } else {
                    if ((int) xx / World.tileWidth == (int) x / World.tileWidth
                            && (int) yy / World.tileWidth == (int) y / World.tileWidth) {
                        xx = -1;
                        yy = -1;
                    }
                    lastGoal = 1;
                }
            }

        }else if(drinkableScore > dangerScore && drinkableScore > idleScore && drinkableScore > breedableScore){
            if(closestDrinkable == null){
                if(lastGoal != 2 || continueTime < 0) {
                    double degree = rand.nextDouble(Math.PI / 2);
                    double distance = sight * World.tileWidth * 100;
                    xx = x + Math.cos(degree) * distance * (rand.nextInt(2) - 0.5) * 2;
                    yy = y + Math.sin(degree) * distance * (rand.nextInt(2) - 0.5) * 2;
                    continueTime = rand.nextInt(500, 1500);
                }
                lastGoal = 2;
            }else {
                continueTime = 0;
                xx = closestDrinkable.x;
                yy = closestDrinkable.y;
                if (closestDrinkable instanceof Creature) {
                    if (drinkDist < speed + (size + ((Creature) closestDrinkable).size) / 2.0 ||
                            (lastGoal == 6 && drinkDist < range + (size + ((Creature) closestDrinkable).size) / 2.0)) {
                        xx = -1;
                        yy = -1;
                        lastGoal = 6;
                    }else {
                        lastGoal = 2;
                    }
                } else {
                    if ((int) xx / World.tileWidth == (int) x / World.tileWidth
                            && (int) yy / World.tileWidth == (int) y / World.tileWidth) {
                        xx = -1;
                        yy = -1;
                    }
                    lastGoal = 2;
                }
            }
        }else if(dangerScore > idleScore && dangerScore > breedableScore){
            //hide
            if(edibleScore > dangerScore * 0.9 || drinkableScore > dangerScore * 0.9){
                xx = -1;
                yy = -1;
            }else {
                // run
                if (sight * World.tileWidth - dDist > fear) {
                    fear = sight * World.tileWidth - dDist;
                }
                xx = -(closestDanger.x - x) + x;
                yy = -(closestDanger.y - y) + y;
            }
            lastGoal = 3;
        }else if( idleScore > breedableScore){
            if(continueTime < 0 || lastGoal != 4){
                if(rand.nextInt(500) == 0) {
                    double degree = rand.nextDouble(Math.PI / 2);
                    double distance = sight * World.tileWidth * 100;
                    xx = x + Math.cos(degree) * distance * (rand.nextInt(2) - 0.5) * 2;
                    yy = y + Math.sin(degree) * distance * (rand.nextInt(2) - 0.5) * 2;
                    continueTime = rand.nextInt(10, 200);
                }else {
                    xx = -1;
                    yy = -1;
                }
            }
            lastGoal = 4;
        }else{
            if (breedDist < speed + (size + closestBreedable.size) / 2.0 ||
                    (lastGoal == 8 && breedDist < range + (size + closestBreedable.size) / 2.0)) {
                xx = -1;
                yy = -1;

                if(closestBreedable.lastGoal == 8){
                    hunger -= maxHunger * 0.3;
                    thirst -= maxThirst * 0.3;
                    comfort = 10;
                    closestBreedable.hunger -= maxHunger * 0.3;
                    closestBreedable.thirst -= maxThirst * 0.3;
                    closestBreedable.comfort = 10;

                    world.addCreature(birth());
                }else{
                    lastGoal = 8;
                }
            }else {
                xx = closestBreedable.x;
                yy = closestBreedable.y;
                lastGoal = 7;
            }
        }


        if(xx != -1){
            dx = xx - x;
            dy = yy - y;
            double d = Math.sqrt(dx * dx + dy * dy);
            dx /= d;
            dy /= d;
            dx *= speed;
            dy *= speed;

            x += dx + rand.nextDouble(-0.5, 0.5) * speed * (lastGoal == 4 ? 0.5 : 1);
            y += dy + rand.nextDouble(-0.5, 0.5) * speed * (lastGoal == 4 ? 0.5 : 1);
            thirst -= 0.002;
            hunger -= 0.001;

        }else{ // not moving

            if(closestEdible != null ) {
                if (closestEdible instanceof Creature) {
                    Creature eat = (Creature) closestEdible;
                    if (edibDist < range + (eat.size + size) / 2.0) {
                        hunger += 5 * dmg;
                        eat.health -= dmg;
                        if (eat.health <= 0) {
                            hunger += 5 * eat.health;
                            eat.x = -1;// sloppy way to make sure dead things are too far
                            eat.y = -1;// away to be considered closest and will be overwritten
                            // quickly, this will be changed when creatures become dead bodies
                            // after death instead of just disappearing
                            world.dead(eat);
                            closestEdible = null;
                        }
                        if (hunger > maxHunger) {
                            hunger = maxHunger;
                        }
                    }
                } else if ((int)closestEdible.x / World.tileWidth == (int)x / World.tileWidth &&
                        (int)closestEdible.y / World.tileWidth == (int)y / World.tileWidth) {
                    hunger += 0.01;
                    if (rand.nextInt(1000) == 0) {
                        world.setTile(x, y, 0);
                        closestEdible = null;
                    }

                    if (hunger >= maxHunger) {
                        hunger = maxHunger;
                    }
                }
            }

            if(closestDrinkable != null) {
                if (closestDrinkable instanceof Creature) {
                    Creature drink = (Creature) closestDrinkable;
                    if (drinkDist < range + (drink.size + size) / 2.0) {
                        thirst += 5 * dmg;
                        drink.health -= dmg;
                        if (drink.health <= 0) {
                            thirst += 5 * drink.health;
                            drink.x = -1;// sloppy way to make sure dead things are too far
                            drink.y = -1;// away to be considered closest and will be overwritten
                            // quickly, this will be changed when creatures become dead bodies
                            // after death instead of just disappearing
                            world.dead(drink);
                            closestDrinkable = null;
                        }
                        if (thirst > maxThirst) {
                            thirst = maxThirst;
                        }
                    }
                } else if ((int)closestDrinkable.x / World.tileWidth == (int)x / World.tileWidth &&
                        (int)closestDrinkable.y / World.tileWidth == (int)y / World.tileWidth) {
                    thirst += 0.01;

                    if (thirst > maxThirst) {
                        thirst = maxThirst;
                    }
                }
            }

            if(health < maxHealth){
                health += 0.0005;
                hunger -= 0.005;
                if(health > maxHealth){
                    health = maxHealth;
                    size = maxSize;
                    child = false;
                }
            }

            if((lastGoal == 4 || lastGoal == 2 || lastGoal == 1) && rand.nextInt(25) == 0) {
                x += rand.nextDouble(-0.3, 0.3) * speed;
                y += rand.nextDouble(-0.3, 0.3) * speed;
            }

            thirst -= 0.0005;
            hunger -= 0.0005;
        }

        if(hunger < 0){
            health += hunger;
            hunger = 0;
        }
        if(thirst < 0){
            health += thirst;
            thirst = 0;
        }

        LinkedList<Creature> possibleOverlaps = world.getCreaturesNear(x, y, size);
        for (Creature c : possibleOverlaps){
            if(c != this) {
                dist = Math.sqrt(Math.pow(x - c.x, 2) + Math.pow(y - c.y, 2));
                if (dist < size / 2.0 + c.size / 2.0) {
                    double percent =  size / 2.0 / ((double)size / 2.0 + c.size / 2.0);
                    dist = (size / 2.0 + c.size / 2.0 - dist) / dist;
                    //dist = (size + c.size) / dist;
                    x -= (c.x - x) * percent * dist;
                    c.x -= (x - c.x) * (1 - percent) * dist;
                    y -= (c.y - y) * percent * dist;
                    c.y -= (y - c.y) * (1 - percent) * dist;
                }
            }
        }
    }

    public void setChunk(int x, int y){
        chunkX = x;
        chunkY = y;
    }

    public int[] getChunk(){
        return new int[] {chunkX, chunkY};
    }

    public void die() {
        world.dead(this);
    }

    protected Creature birth() {
        return new Creature(x, y, maxHealth, maxHunger, maxThirst, speed, sight, size, world);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return c;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getHunger() {
        return hunger;
    }

    public double getMaxHunger() {
        return maxHunger;
    }

    public double getThirst() {
        return thirst;
    }

    public double getMaxThirst() {
        return maxThirst;
    }
}
