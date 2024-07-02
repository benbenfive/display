package simulatedWorld;

import simulatedWorld.creatures.Creature;
import simulatedWorld.creatures.RedSlime;
import simulatedWorld.creatures.Slime;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    private double[][] distance =
    {{14.142135623730951, 13.45362404707371, 12.806248474865697, 12.206555615733702, 11.661903789690601, 11.180339887498949, 10.770329614269007, 10.44030650891055, 10.198039027185569, 10.04987562112089, 10.0, 10.04987562112089, 10.198039027185569, 10.44030650891055, 10.770329614269007, 11.180339887498949, 11.661903789690601, 12.206555615733702, 12.806248474865697, 13.45362404707371, 14.142135623730951},
    {13.45362404707371, 12.727922061357855, 12.041594578792296, 11.40175425099138, 10.816653826391969, 10.295630140987, 9.848857801796104, 9.486832980505138, 9.219544457292887, 9.055385138137417, 9.0, 9.055385138137417, 9.219544457292887, 9.486832980505138, 9.848857801796104, 10.295630140987, 10.816653826391969, 11.40175425099138, 12.041594578792296, 12.727922061357855, 13.45362404707371},
    {12.806248474865697, 12.041594578792296, 11.313708498984761, 10.63014581273465, 10.0, 9.433981132056603, 8.94427190999916, 8.54400374531753, 8.246211251235321, 8.06225774829855, 8.0, 8.06225774829855, 8.246211251235321, 8.54400374531753, 8.94427190999916, 9.433981132056603, 10.0, 10.63014581273465, 11.313708498984761, 12.041594578792296, 12.806248474865697},
    {12.206555615733702, 11.40175425099138, 10.63014581273465, 9.899494936611665, 9.219544457292887, 8.602325267042627, 8.06225774829855, 7.615773105863909, 7.280109889280518, 7.0710678118654755, 7.0, 7.0710678118654755, 7.280109889280518, 7.615773105863909, 8.06225774829855, 8.602325267042627, 9.219544457292887, 9.899494936611665, 10.63014581273465, 11.40175425099138, 12.206555615733702},
    {11.661903789690601, 10.816653826391969, 10.0, 9.219544457292887, 8.48528137423857, 7.810249675906654, 7.211102550927978, 6.708203932499369, 6.324555320336759, 6.082762530298219, 6.0, 6.082762530298219, 6.324555320336759, 6.708203932499369, 7.211102550927978, 7.810249675906654, 8.48528137423857, 9.219544457292887, 10.0, 10.816653826391969, 11.661903789690601},
    {11.180339887498949, 10.295630140987, 9.433981132056603, 8.602325267042627, 7.810249675906654, 7.0710678118654755, 6.4031242374328485, 5.830951894845301, 5.385164807134504, 5.0990195135927845, 5.0, 5.0990195135927845, 5.385164807134504, 5.830951894845301, 6.4031242374328485, 7.0710678118654755, 7.810249675906654, 8.602325267042627, 9.433981132056603, 10.295630140987, 11.180339887498949},
    {10.770329614269007, 9.848857801796104, 8.94427190999916, 8.06225774829855, 7.211102550927978, 6.4031242374328485, 5.656854249492381, 5.0, 4.47213595499958, 4.123105625617661, 4.0, 4.123105625617661, 4.47213595499958, 5.0, 5.656854249492381, 6.4031242374328485, 7.211102550927978, 8.06225774829855, 8.94427190999916, 9.848857801796104, 10.770329614269007},
    {10.44030650891055, 9.486832980505138, 8.54400374531753, 7.615773105863909, 6.708203932499369, 5.830951894845301, 5.0, 4.242640687119285, 3.605551275463989, 3.1622776601683795, 3.0, 3.1622776601683795, 3.605551275463989, 4.242640687119285, 5.0, 5.830951894845301, 6.708203932499369, 7.615773105863909, 8.54400374531753, 9.486832980505138, 10.44030650891055},
    {10.198039027185569, 9.219544457292887, 8.246211251235321, 7.280109889280518, 6.324555320336759, 5.385164807134504, 4.47213595499958, 3.605551275463989, 2.8284271247461903, 2.23606797749979, 2.0, 2.23606797749979, 2.8284271247461903, 3.605551275463989, 4.47213595499958, 5.385164807134504, 6.324555320336759, 7.280109889280518, 8.246211251235321, 9.219544457292887, 10.198039027185569},
    {10.04987562112089, 9.055385138137417, 8.06225774829855, 7.0710678118654755, 6.082762530298219, 5.0990195135927845, 4.123105625617661, 3.1622776601683795, 2.23606797749979, 1.4142135623730951, 1.0, 1.4142135623730951, 2.23606797749979, 3.1622776601683795, 4.123105625617661, 5.0990195135927845, 6.082762530298219, 7.0710678118654755, 8.06225774829855, 9.055385138137417, 10.04987562112089},
    {10.0, 9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0},
    {10.04987562112089, 9.055385138137417, 8.06225774829855, 7.0710678118654755, 6.082762530298219, 5.0990195135927845, 4.123105625617661, 3.1622776601683795, 2.23606797749979, 1.4142135623730951, 1.0, 1.4142135623730951, 2.23606797749979, 3.1622776601683795, 4.123105625617661, 5.0990195135927845, 6.082762530298219, 7.0710678118654755, 8.06225774829855, 9.055385138137417, 10.04987562112089},
    {10.198039027185569, 9.219544457292887, 8.246211251235321, 7.280109889280518, 6.324555320336759, 5.385164807134504, 4.47213595499958, 3.605551275463989, 2.8284271247461903, 2.23606797749979, 2.0, 2.23606797749979, 2.8284271247461903, 3.605551275463989, 4.47213595499958, 5.385164807134504, 6.324555320336759, 7.280109889280518, 8.246211251235321, 9.219544457292887, 10.198039027185569},
    {10.44030650891055, 9.486832980505138, 8.54400374531753, 7.615773105863909, 6.708203932499369, 5.830951894845301, 5.0, 4.242640687119285, 3.605551275463989, 3.1622776601683795, 3.0, 3.1622776601683795, 3.605551275463989, 4.242640687119285, 5.0, 5.830951894845301, 6.708203932499369, 7.615773105863909, 8.54400374531753, 9.486832980505138, 10.44030650891055},
    {10.770329614269007, 9.848857801796104, 8.94427190999916, 8.06225774829855, 7.211102550927978, 6.4031242374328485, 5.656854249492381, 5.0, 4.47213595499958, 4.123105625617661, 4.0, 4.123105625617661, 4.47213595499958, 5.0, 5.656854249492381, 6.4031242374328485, 7.211102550927978, 8.06225774829855, 8.94427190999916, 9.848857801796104, 10.770329614269007},
    {11.180339887498949, 10.295630140987, 9.433981132056603, 8.602325267042627, 7.810249675906654, 7.0710678118654755, 6.4031242374328485, 5.830951894845301, 5.385164807134504, 5.0990195135927845, 5.0, 5.0990195135927845, 5.385164807134504, 5.830951894845301, 6.4031242374328485, 7.0710678118654755, 7.810249675906654, 8.602325267042627, 9.433981132056603, 10.295630140987, 11.180339887498949},
    {11.661903789690601, 10.816653826391969, 10.0, 9.219544457292887, 8.48528137423857, 7.810249675906654, 7.211102550927978, 6.708203932499369, 6.324555320336759, 6.082762530298219, 6.0, 6.082762530298219, 6.324555320336759, 6.708203932499369, 7.211102550927978, 7.810249675906654, 8.48528137423857, 9.219544457292887, 10.0, 10.816653826391969, 11.661903789690601},
    {12.206555615733702, 11.40175425099138, 10.63014581273465, 9.899494936611665, 9.219544457292887, 8.602325267042627, 8.06225774829855, 7.615773105863909, 7.280109889280518, 7.0710678118654755, 7.0, 7.0710678118654755, 7.280109889280518, 7.615773105863909, 8.06225774829855, 8.602325267042627, 9.219544457292887, 9.899494936611665, 10.63014581273465, 11.40175425099138, 12.206555615733702},
    {12.806248474865697, 12.041594578792296, 11.313708498984761, 10.63014581273465, 10.0, 9.433981132056603, 8.94427190999916, 8.54400374531753, 8.246211251235321, 8.06225774829855, 8.0, 8.06225774829855, 8.246211251235321, 8.54400374531753, 8.94427190999916, 9.433981132056603, 10.0, 10.63014581273465, 11.313708498984761, 12.041594578792296, 12.806248474865697},
    {13.45362404707371, 12.727922061357855, 12.041594578792296, 11.40175425099138, 10.816653826391969, 10.295630140987, 9.848857801796104, 9.486832980505138, 9.219544457292887, 9.055385138137417, 9.0, 9.055385138137417, 9.219544457292887, 9.486832980505138, 9.848857801796104, 10.295630140987, 10.816653826391969, 11.40175425099138, 12.041594578792296, 12.727922061357855, 13.45362404707371},
    {14.142135623730951, 13.45362404707371, 12.806248474865697, 12.206555615733702, 11.661903789690601, 11.180339887498949, 10.770329614269007, 10.44030650891055, 10.198039027185569, 10.04987562112089, 10.0, 10.04987562112089, 10.198039027185569, 10.44030650891055, 10.770329614269007, 11.180339887498949, 11.661903789690601, 12.206555615733702, 12.806248474865697, 13.45362404707371, 14.142135623730951}};


    public static final int worldSize = 32, tileWidth = 64;
    private PixelWriter pw;
    private Color[] colors = {new Color(0.4, 0.27, 0.15, 1),
            new Color(0.2, 0.5, 0.2, 1), new Color(0.3, 0.3, 0.3, 1),
            new Color(0.3, 0.7, 1, 1)};
    private Player p = new Player(worldSize * Chunk.chunkSize * tileWidth / 2, worldSize * Chunk.chunkSize * tileWidth / 2);
    private ThreadLocalRandom rand = ThreadLocalRandom.current();
    private static Image spriteSheet = new Image("simulatedWorld/resources/spritesheet.png");
    private static PixelReader pr = spriteSheet.getPixelReader();
    private HashMap<Integer, HashMap<Integer, Chunk>> loadedChunks = new HashMap<>();
    private long frame = 0;

    public World(PixelWriter pw) {
        this.pw = pw;

        for (int i = 0; i < worldSize; i++) {
            HashMap<Integer, Chunk> row = new HashMap<>();
            for (int j = 0; j < worldSize; j++) {
                row.put(j, new Chunk(j, i, this));
            }
            loadedChunks.put(i, row);
        }
    }

    public void update(int w, int h){
        //long total = System.nanoTime();
        for (int i = 0; i < 100 ; i++) {
            int tx = rand.nextInt(1, (worldSize * Chunk.chunkSize) - 2),
                    ty = rand.nextInt(1, worldSize * Chunk.chunkSize - 2);
            if(getTile(ty, tx) == 0){
                if(getTile(ty - 1, tx) == 1 || getTile(ty + 1, tx) == 1 || getTile(ty - 1, tx - 1) == 1 ||
                                         getTile(ty - 1, tx + 1) == 1 || getTile(ty + 1, tx - 1) == 1 ||
                                         getTile(ty + 1, tx + 1) == 1 || getTile(ty, tx - 1) == 1 || getTile(ty, tx + 1) == 1){
                    setTile(ty, tx, 1);
                }
            }
        }

        //long getNear = System.nanoTime();
        LinkedList<Creature> crts = getCreaturesNear(p.getX(), p.getY(), tileWidth * 50);
        //System.out.println(System.nanoTime() - getNear);

        //long update = System.nanoTime();
        for (Creature c : crts) {
            c.update();
        }
        //System.out.println(System.nanoTime() - update);


        p.controls();


        int zeroX = (int)p.getX() - w / 2, zeroY = (int)p.getY() - h / 2;
        for (int i = -zeroY % tileWidth; i < h; i += tileWidth) {
            for (int j = -zeroX % tileWidth; j < w; j += tileWidth) {
                //Color c = colors[tiles[(zeroY + i) / tileWidth][(zeroX + j) / tileWidth]];
                //pw.setColor(j, i, c);
                pw.setPixels(j, i, tileWidth, tileWidth, pr,
                        Math.abs(((zeroY + i) * (zeroY + i) + (zeroX + j) * (zeroX + j)) % 5 * tileWidth),
                        getTile((zeroX + j) / tileWidth, (zeroY + i) / tileWidth) * tileWidth);
            }
        }
        for(Creature c : crts){
            if((int)p.getY() - h / 2 <= c.getY() + c.getSize() / 2 && (int)p.getY() + h / 2 >= c.getY() - c.getSize() / 2){
                if((int)p.getX() - w / 2 <= c.getX() + c.getSize() / 2 && (int)p.getX() + w / 2 >= c.getX() - c.getSize() / 2){
                    for (int j = (int)c.getY() - c.getSize() / 2 - zeroY; j < c.getY() + c.getSize() / 2 - zeroY; j++) {
                        for (int i = (int)c.getX() - c.getSize() / 2 - zeroX; i < c.getX() + c.getSize() / 2 - zeroX; i++) {
                            if(i >= 0 && i < w && j >= 0 && j < h){
                                pw.setColor(i, j, c.getColor());
                            }
                        }
                    }
                }
            }
        }
        if(p.spectate != null) {
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < p.spectate.getHealth() / p.spectate.getMaxHealth() * 100; j++) {
                    pw.setColor(j, i, new Color(1, 0, 0, 0.3));
                }
            }
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < p.spectate.getHunger() / p.spectate.getMaxHunger() * 100; j++) {
                    pw.setColor(j, i + 15, new Color(0.4, 0.3, 0.15, 0.3));
                }
            }
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < p.spectate.getThirst() / p.spectate.getMaxThirst() * 100; j++) {
                    pw.setColor(j, i + 30, new Color(0, 0, 1, 0.3));
                }
            }
        }

        ++frame;
        //System.out.println(System.nanoTime() - total);
        //System.out.println();
    }




    public int[] lookForTile(double x, double y, double s, int t){
        double dist = Double.MAX_VALUE;
        int xx = -1, yy = -1;
        for (int i = 0; i <= s * s / 5; i++) {
            int tx = rand.nextInt((int)-s, (int)s + 1),
                    ty = rand.nextInt((int)-s, (int)s + 1);
            if(getTile(ty + (int)y / tileWidth, tx + (int)x / tileWidth) == t && distance[tx + 10][ty + 10] < dist){
                dist = distance[tx + 10][ty + 10];
                xx = tx + (int)x / tileWidth;
                yy = ty + (int)y / tileWidth;
            }
        }
        int[] out = {xx, yy};
        return out;
    }

    public LinkedList<Creature> getCreaturesNear(double x, double y, double w){
        // if w is ever greater than chunk size this can break and skip entire chunks
        // but you should never be getting all the creatures in an area that big as
        // it defeats the purpose of this being to limit the exponential nature of
        // checking every single other creature

        // if for some ungodly reason you want all the creatures in a chunk and destroy
        // that frame with lag go ahead and take the list of creatures in that chunk
        int     x1 = (int)(x - w) / tileWidth / Chunk.chunkSize,
                y1 = (int)(y - w) / tileWidth / Chunk.chunkSize,
                x2 = (int)(x + w) / tileWidth / Chunk.chunkSize,
                y2 = (int)(y - w) / tileWidth / Chunk.chunkSize,
                x3 = (int)(x - w) / tileWidth / Chunk.chunkSize,
                y3 = (int)(y + w) / tileWidth / Chunk.chunkSize,
                x4 = (int)(x + w) / tileWidth / Chunk.chunkSize,
                y4 = (int)(y + w) / tileWidth / Chunk.chunkSize;


        LinkedList<Creature> crts = loadedChunks.get(y1).get(x1).getCreaturesNear(x, y, w, frame);

        if(x2 != x1 || y2 != y1){
            crts.addAll(loadedChunks.get(y2).get(x2).getCreaturesNear(x, y, w, frame));
        }
        if((x3 != x2 || y3 != y2) && (x3 != x1 || y3 != y1)) {
            crts.addAll(loadedChunks.get(y3).get(x3).getCreaturesNear(x, y, w, frame));
        }
        if((x4 != x3 || y4 != y3) && (x4 != x2 || y4 != y2) && (x4 != x1 || y4 != y1)){
            crts.addAll(loadedChunks.get(y4).get(x4).getCreaturesNear(x, y, w, frame));
        }

        LinkedList<Creature> ret = new LinkedList<>();
        for(Creature c : crts){
            double dist = Math.sqrt(Math.pow(x - c.getX(),2) + Math.pow(y - c.getY(),2));
            if(dist <= w){
                ret.add(c);
            }
        }
        return ret;
    }

    public void setTile(double x, double y, int t){
        setTile((int)x / tileWidth, (int)y / tileWidth, t);
    }

    public void setTile(int x, int y, int t){
        loadedChunks.get(y / Chunk.chunkSize).get(x / Chunk.chunkSize)
                .setTile(x % Chunk.chunkSize, y % Chunk.chunkSize, t);
    }

    public int getTile(double x, double y){
        return getTile((int)x / tileWidth, (int)y / tileWidth);
    }

    public int getTile(int x, int y){
        return loadedChunks.get(y / Chunk.chunkSize).get(x / Chunk.chunkSize)
                .getTile(x % Chunk.chunkSize, y % Chunk.chunkSize);
    }

    public Player getPlayer(){
        return p;
    }

    public void dead(Creature c) {
        loadedChunks.get((int)c.getY() / tileWidth / Chunk.chunkSize)
                .get((int)c.getX() / tileWidth / Chunk.chunkSize).dead(c);
    }

    public void addCreature(Creature c){
        loadedChunks.get((int)c.getY() / tileWidth / Chunk.chunkSize)
                .get((int)c.getX() / tileWidth / Chunk.chunkSize).add(c);
    }
}
