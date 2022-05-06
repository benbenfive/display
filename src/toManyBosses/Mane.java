package toManyBosses;

import javax.swing.*;
import java.awt.*;

/**
 * @author Benjamin Vick
 */
public class Mane {

    public static String message = "Victory!";
    public static Player p1 = new Player(400, 500);
    static int barriersPerStage = 10, stages = 1, currentStage = 0, time = 0;
    static Barrier[][] world;
    static int[] barriersInStage = new int[stages];
    static Boss currentBoss = new Boss(1);

    public static void main(String[] args) {
        run();
    }

    public static void run(){
        //like the fx stage
        JFrame frame = new JFrame();
        //title
        frame.setTitle("To Many Bosses");
        //a new method I found making this game sets the screen to the max size
        //frame.setSize(frame.getMaximumSize()); 2022 me: what was I thinking, how
        // can this work on any screen size when I manually set all the numbers
        frame.setSize(1550, 950);// my best guess at my old screen resolution
        //put the frame in the top left corner
        frame.setLocation(-10, -10);
        //JFrames don't really turn off when you close them without this line
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //creates a new instance of the MyPanel class I made. This is where everything
        //drawing related is put
        MyPanel panel = new MyPanel();
        //next two just tie the panel to the frame
        Container pane = frame.getContentPane();
        pane.add(panel);

        //Making and initializing an array of the barrier class I made
        world = new Barrier[stages][barriersPerStage];
        //the constructor takes the start and end point coordinates
        world[0][0] = new Barrier(100, 500, 500, 700);
        world[0][1] = new Barrier(500, 400, 1100, 400);
        world[0][2] = new Barrier(1100, 700, 1500, 500);
        world[0][3] = new Barrier(100, 800, 1500, 800);


        frame.setVisible(true);


        //this is where the game itself runs
        while(currentBoss.hp > 0 && p1.hp > 0){
            //short delay so game doesn't run at fastest possible speed
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //checks to see what buttons are pressed and does the appropriate action
            controls(panel);

            //checks to see if the player is mid air and moves the player if not
            gravity(p1);

            //moves the boss according to whatever patter it has selected or randomly
            //selects a new pattern to follow
            currentBoss.move();

            //checks to see if the player is invulnerable and if so lowers that time
            if(p1.safeTime > 0){
                p1.safeTime--;
            }
            //if the player is touching the boss and not invincible make the player
            //invincible and lower the players hp
            if(currentBoss.touchingPlayer(p1) && p1.safeTime == 0){
                p1.hp--;
                p1.safeTime = 15;
            }

            //checks if the player fell out of the world
            fallDamage();

            //this updates the images on the screen
            panel.repaint();

            time++;
        }
    }




    private static void gravity(Player p){
        int vel = p.yVel;//up down velocity
        int left = p.x - p.width / 2;//sides of the player
        int right = p.x + p.width / 2;
        int possibleY = 10000;//set way to high so anything found will be lower
        int lineHitNum = -1;

        //loop through all the barriers
        for (int i = 0; i < barriersInStage[currentStage]; i++) {
            Barrier curBar = world[currentStage][i];

            //default
            p.canJump = false;
            p.lineOn = null;

            //checks every point from left to right
            for (int j = left; j <= right; j++) {
                //checks if you will go past the barrier
                if (curBar.goesPast(j, p.y, 0, vel)) {
                    //checks if it is higher up than the last possible position you could
                    // land
                    if (curBar.yAt(j) < possibleY) {
                        possibleY = curBar.yAt(j);
                        //save the number index of the line you may land on
                        lineHitNum = i;
                    }
                }
            }
        }

        if(possibleY != 10000){
            p.setY(possibleY);
            p.setYVel(1);
            p.canJump = true;
            //the line number can never change unless possibleY also changes so you do
            //not need to check if the line number is at it's default value too
            p.lineOn = world[currentStage][lineHitNum];
        }

        if(!p.canJump){
            p.move(0, vel);

            int slowing = 2;
            p.changeYVel(Math.abs(vel / slowing));
            if(vel == 0 || vel / slowing == 0){
                p.changeYVel(6);
            }
        }
    }



    private static void controls(MyPanel panel){
        if(panel.aPressed){
            sideMovement(-p1.getSpeed());
            p1.lookedRightLast = false;
        }

        if(panel.dPressed){
            sideMovement(p1.getSpeed());
            p1.lookedRightLast = true;
        }

        if(panel.spacePressed){
            //fist jump
            if(p1.canJump) {
                p1.yVel += -p1.getSpeed() * 7 / 2;
                p1.lastJump = time;
                //after your jump increase your height
            }else if(p1.lastJump + 5 > time){
                p1.yVel += -p1.getSpeed() * 6 / 5;
            }
        }

        if(panel.sPressed){
            if(p1.canJump) {
                //checks if the last press was after the last release (haven't released
                // yet) and if the last press was recent
                if(panel.lastSPresses > panel.lastSRelease &&
                        panel.lastSPresses < panel.lastSRelease + 10) {
                    //should only happen when s is pressed released and pressed again
                    p1.move(0, p1.getSpeed());
                }
            }
        }

        if(panel.jPressed){
            if(p1.canAttackAgain) {
                int damageDone;
                //checks which direction you are attacking
                if (panel.wPressed) {
                    damageDone = p1.damageDone(1);
                    MyPanel.drawAttack = 1;
                } else if (panel.sPressed) {
                    damageDone = p1.damageDone(3);
                    MyPanel.drawAttack = 3;
                } else if (p1.lookedRightLast) {
                    damageDone = p1.damageDone(2);
                    MyPanel.drawAttack = 2;
                } else {
                    damageDone = p1.damageDone(4);
                    MyPanel.drawAttack = 4;
                }

                //lower boss hp if hit
                currentBoss.hp -= damageDone;
                //makes it so you can't attack again till you release the j key
                p1.canAttackAgain = false;
            }
        }
    }



    public static void sideMovement(int speed){
        //speed / Math.abs(speed) will be + or - 1 depending on whatever sign speed has
        int direction = speed / Math.abs(speed);
        int corner = p1.x + p1.width / 2 * direction;
        boolean willHitWall = false, willPassBarrier = false;

        //find if the player is going to walk through a ramp/wall
        for (int i = 0; i < barriersInStage[currentStage]; i++) {
            Barrier curBar = world[currentStage][i];
            //are you on an upward hill?
            if(curBar.goesPast(corner, p1.y, speed, 0)){
                willPassBarrier = true;
                //is it to steep to climb?
                if(Math.abs(curBar.m) > 1){
                    willHitWall = true;
                }else{
                    //climb the slope
                    p1.setY(curBar.yAt(corner + speed));
                }
            }
        }
        //move as normal
        if(!willHitWall) {
            p1.move(speed, 0);
            //stay on the line you are currently on
            //System.out.println();
            //System.out.println("online " + (p1.lineOn != null) + p1.canJump);
            //System.out.println("pasbar " + !willPassBarrier);
            if(p1.lineOn != null && !willPassBarrier){
                //System.out.println("I'm in");
                p1.y = p1.lineOn.yAt(p1.x - p1.width / 2 * direction);
            }
        }
    }

    private static void fallDamage(){
        if(p1.outOfTheWorld()){
            p1.hp--;
            //go back to your spawn point
            p1.setY(500);
            p1.setX(400);
        }
    }
}
