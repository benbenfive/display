package simulatedWorld;

import simulatedWorld.creatures.Creature;
import simulatedWorld.creatures.RedSlime;
import simulatedWorld.creatures.Slime;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import static simulatedWorld.World.tileWidth;

public class Chunk {
    private ThreadLocalRandom rand = ThreadLocalRandom.current();
    public static final int chunkSize = 200;
    private int[][] tiles = new int[chunkSize][chunkSize];
    private CreatureGroup cg;
    private LinkedList<Creature> creatures = new LinkedList<>();
    private World world;
    private int x, y;
    private long lfcgu = -1; // last frame creature group updated

    public Chunk(int x, int y, World world){
        this.x = x;
        this.y = y;
        this.world = world;

        for (int i = 0; i < chunkSize; i++) {
            for (int j = 0; j < chunkSize; j++) {
                if(rand.nextInt(2) == 0 || rand.nextInt(2) == 0 || rand.nextInt(2) == 0){
                    tiles[i][j] = 1;
                }else if(rand.nextInt(2) == 0){
                    tiles[i][j] = 3;
                }else if(rand.nextInt(3) == 0){
                    tiles[i][j] = 2;
                }
            }
        }

        if(x != 0 && x != World.worldSize && y != 0 && y != World.worldSize) {
            for (int i = 0; i < 5000; i++) {
                if (rand.nextInt(10) == 0) {
                    creatures.add(new RedSlime(rand.nextDouble(x * chunkSize * tileWidth,
                            (x + 1) * chunkSize * tileWidth),
                            rand.nextDouble(y * chunkSize * tileWidth,
                                    (y + 1) * chunkSize * tileWidth), world));
                } else {
                    creatures.add(new Slime(rand.nextDouble(x * chunkSize * tileWidth,
                            (x + 1) * chunkSize * tileWidth),
                            rand.nextDouble(y * chunkSize * tileWidth,
                                    (y + 1) * chunkSize * tileWidth), world));
                }
            }
        }
    }

    public LinkedList<Creature> getCreaturesNear(double x, double y, double w, long f) {
        if(f != lfcgu){
            lfcgu = f;
            cg = new CreatureGroup(chunkSize * tileWidth, chunkSize * tileWidth,
                    this.x * chunkSize * tileWidth, this.y * chunkSize * tileWidth);
            LinkedList<Creature> remove = new LinkedList<>();
            for(Creature c : creatures){
                int[] cords = c.getChunk();
                if(cords[0] != (int)c.getX() / chunkSize / tileWidth ||
                        cords[1] != (int)c.getY() / chunkSize / tileWidth){
                    remove.add(c);
                    world.addCreature(c);
                    c.setChunk((int)c.getX() / chunkSize / tileWidth,
                            (int)c.getY() / chunkSize / tileWidth);
                }else{
                    cg.add(c);
                }
            }
            creatures.removeAll(remove);
        }
        return cg.get(x, y, w);
    }

    public int getTile(int x, int y) {
        return tiles[y][x];
    }

    public void setTile(int x, int y, int t) {
        tiles[y][x] = t;
    }

    public void dead(Creature c) {
        creatures.remove(c);
    }

    public void add(Creature c) {
        creatures.add(c);
    }
}
