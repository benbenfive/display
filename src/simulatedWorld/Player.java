package simulatedWorld;

import simulatedWorld.creatures.Creature;

public class Player {
    private double x, y, speed = 5;
    private boolean u, d, l, r;
    public Creature spectate;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void controls(){
        if(spectate != null){
            x = spectate.getX();
            y = spectate.getY();
        }else {
            if (u && !d) {
                if (r) {
                    x += 1.41 * speed;
                    y -= 1.41 * speed;
                } else if (l) {
                    x -= 1.41 * speed;
                    y -= 1.41 * speed;
                } else {
                    y -= 2 * speed;
                }
            } else if (d && !u) {
                if (r) {
                    x += 1.41 * speed;
                    y += 1.41 * speed;
                } else if (l) {
                    x -= 1.41 * speed;
                    y += 1.41 * speed;
                } else {
                    y += 2 * speed;
                }
            } else if (r && !l) {
                x += 2 * speed;
            } else if (l && !r) {
                x -= 2 * speed;
            }
        }
    }

    public void setU(boolean u) {
        this.u = u;
    }

    public void setD(boolean d) {
        this.d = d;
    }

    public void setL(boolean l) {
        this.l = l;
    }

    public void setR(boolean r) {
        this.r = r;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
