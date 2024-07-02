package simulatedWorld;

import simulatedWorld.creatures.Creature;

import java.util.LinkedList;

public class CreatureGroup {
    private double w, h, ox, oy;
    private LinkedList<Creature> creatures = new LinkedList<>();
    private CreatureGroup[] groups = new CreatureGroup[4];
    public static final int maxSize = 5;

    public CreatureGroup(LinkedList<Creature> creatures, double w, double h, double ox, double oy) {
        double t = System.nanoTime();
        this.w = w;
        this.h = h;
        this.ox = ox;
        this.oy = oy;
        this.creatures = creatures;
        if(creatures.size() > maxSize){
            breakdown();
        }
    }

    public CreatureGroup(double w, double h, double ox, double oy) {
        this.w = w;
        this.h = h;
        this.ox = ox;
        this.oy = oy;
    }

    private void breakdown() {
        groups[0] = new CreatureGroup(w / 2, h / 2, ox, oy);
        groups[1] = new CreatureGroup(w / 2, h / 2, ox + w / 2, oy);
        groups[2] = new CreatureGroup(w / 2, h / 2, ox, oy + h / 2);
        groups[3] = new CreatureGroup(w / 2, h / 2, ox + w / 2, oy + h / 2);

        for (Creature c : creatures) {
            place(c.getX(), c.getY()).add(c);
        }

        creatures = null;
    }

    private CreatureGroup place(double x, double y) {
        if(x < w / 2 + ox){
            if(y < h / 2 + oy){
                return groups[0];
            }else{
                return groups[2];
            }
        }else{
            if(y < h / 2 + oy){
                return groups[1];
            }else{
                return groups[3];
            }
        }
    }

    public void add(Creature c){
        if(groups[0] != null){
            place(c.getX(), c.getY()).add(c);
            return;
        }

        creatures.add(c);
        if(creatures.size() > maxSize){
            breakdown();
        }
    }

    public LinkedList<Creature> get(double x, double y, double w){
        if(groups[0] != null){
            LinkedList<Creature> crts = new LinkedList<>();
            CreatureGroup group = place(x, y);
            crts.addAll(group.get(x, y, w));

            // this is doing a square on square collision of the group to a circle which will make it
            // so that some groups do not actually overlap at all with the circle but they will be added
            // however even if a square slightly overlaps the circle all creatures in that square will
            // be added to the list so there will be extra creatures no matter what and a few more will
            // not hurt
            CreatureGroup other = place(x - w, y - w);
            if(other != group){
                crts.addAll(other.get(x, y, w));
            }

            other = place(x + w , y - w);
            if(other != group){
                crts.addAll(other.get(x, y, w));
            }

            other = place(x - w, y + w);
            if(other != group){
                crts.addAll(other.get(x, y, w));
            }

            other = place(x + w, y + w);
            if(other != group){
                crts.addAll(other.get(x, y, w));
            }

            return crts;
        }

        return creatures;
    }
}
