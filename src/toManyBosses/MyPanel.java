package toManyBosses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Benjamin Vick
 */
public class MyPanel extends JPanel {
    //this is the constructor it is ran when an instance of the class is made
    public MyPanel(){
        setBackground(new Color(0, 183, 255));
        //this makes it so that the panel knows what keys are pressed
        addKeyListener(keyListener);
    }

    //set a boolean for every button to be used containing weather it is currently pressed
    //without this round about way of button pressing there is a delay in changing
    //directions and trying to hold down a new button
    public boolean wPressed = false, aPressed = false, sPressed = false, dPressed = false,
            spacePressed = false, jPressed = false;
    public static int drawAttack = 0, lastSPresses= -100, lastSRelease = -100;

    //this is where everything key pressing related is
    KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            //finds out what key was just pushed down
            String c = "" + e.getKeyChar();

            //checks which key it was and sets that boolean true
            if(c.equalsIgnoreCase("w")){
                wPressed = true;
            }else if(c.equalsIgnoreCase("a")){
                aPressed = true;
            }else if(c.equalsIgnoreCase("s")){
                sPressed = true;
                lastSPresses = Mane.time;
            }else if(c.equalsIgnoreCase("d")){
                dPressed = true;
            }else if(c.equalsIgnoreCase(" ")){
                spacePressed = true;
            }else if(c.equalsIgnoreCase("j")){
                jPressed = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //finds out what key was just released
            String c = "" + e.getKeyChar();

            //checks which key it was and sets that boolean false
            if(c.equalsIgnoreCase("w")){
                wPressed = false;
            }else if(c.equalsIgnoreCase("a")){
                aPressed = false;
            }else if(c.equalsIgnoreCase("s")){
                sPressed = false;
                lastSRelease = Mane.time;
            }else if(c.equalsIgnoreCase("d")){
                dPressed = false;
            }else if(c.equalsIgnoreCase(" ")){
                spacePressed = false;
            }else if(c.equalsIgnoreCase("j")){
                jPressed = false;
                //this boolean is used so that the player can only attack once per
                //press of the key and must release it to attack again
                //otherwise the player can hold the button and attack 20 times a
                //second and kill the boss in 8 seconds
                Mane.p1.canAttackAgain = true;
            }
        }
    };

    //this is what gets run when the repaint method is called
    public void paintComponent(Graphics g) {
        //honestly don't know what this does it's just here
        super.paintComponent(g);
        //helper methods that make this so much easier to read
        if((Mane.currentBoss.hp > 0)) {
            drawBarriers(g);
            drawPlayer(g);
            drawBosses(g);
            drawAttack(g);
        }else{
            drawVictory(g);
        }
    }

    private void drawVictory(Graphics g){
        //sets the color for anything drawn after this
        g.setColor(Color.black);
        g.drawString(Mane.message, 600, 400);
    }

    private void drawBarriers(Graphics g){
        g.setColor(Color.black);
        for (int i = 0; i < Mane.barriersInStage[Mane.currentStage]; i++) {
            Barrier curBar = Mane.world[Mane.currentStage][i];
            //draws a 1 pixel wide line from the two points
            g.drawLine(curBar.x1, curBar.y1, curBar.x2, curBar.y2);
        }
    }

    private void drawPlayer(Graphics g){
        int w = Player.width,
            h = Player.height,
            x = Mane.p1.x - w / 2,
            y = Mane.p1.y - h;
        //draws a rectangle from the top left point outward
        if(Mane.p1.lookedRightLast) {
            g.drawImage(Mane.p1.imager, x, y, w, h, null);
        }else{
            g.drawImage(Mane.p1.imagel, x, y, w, h, null);
        }

        //draws health
        g.setColor(new Color(94, 101, 118));
        for (int i = 0; i < Mane.p1.hp; i++) {
            //drawn same as the rectangle top left corner is the first two variables
            //and then how far down and to the right you want to stretch it
            g.fillOval(20 * i, 20, 20, 20);
        }
    }

    private void drawBosses(Graphics g){
        int w = Mane.currentBoss.width,
                h = Mane.currentBoss.height,
                x = Mane.currentBoss.x - w / 2,
                y = Mane.currentBoss.y - h;
        //this draws the boss picture on the screen same way a rectangle is drawn
        //starting at the top left corner
        g.drawImage(Mane.currentBoss.image, x, y, w, h, null);

        //boss health bar
        g.setColor(Color.red);
        g.fillRect(0, 0, Mane.currentBoss.hp * 1550 / Mane.currentBoss.startHp
                , 20);
    }

    private void drawAttack(Graphics g){
        //draws a red rectangle in whatever direction the player is looking
        Image attackImage = Mane.p1.attacku;
        switch (drawAttack){
            case 1 ://up
                attackImage = Mane.p1.attacku;
                break;
            case 2 ://right
                attackImage = Mane.p1.attackr;
                break;
            case 3 ://down
                attackImage = Mane.p1.attackd;
                break;
            case 4 ://left
                attackImage = Mane.p1.attackl;
                break;
        }
        if(drawAttack != 0) {
            g.drawImage(attackImage, Mane.p1.left, Mane.p1.top,
                    Mane.p1.right - Mane.p1.left,
                    Mane.p1.bot - Mane.p1.top, null);
        }
        drawAttack = 0;
    }

    //this somehow makes it so the panel is focused on at the start idk
    public void addNotify(){
        super.addNotify();
        requestFocus();
    }
}
