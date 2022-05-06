package toManyBosses;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Benjamin Vick
 */
public class Boss {
    int hp = 0, startHp = 0, num = 0, x = 0, y = 0, width = 0, height = 0, pattern = 0,
            p = 0, totalPatterns = 3, changeX = 0, changeY = 0, speed = 1;
    Image image;
    ArrayList<HitBox> hitBoxes = new ArrayList<>();
    ArrayList<HitBox> critBoxes = new ArrayList<>();

    public Boss(int bossNum){
        num = bossNum;
        switch (bossNum){
            case 1:
                setFirstBoss();
                break;
        }
    }

    private void setFirstBoss(){
        startHp = 200;
        hp = startHp;
        x = 800;
        y = 600;
        width = 512;
        height = 256;
        image = new ImageIcon(
                "src\\toManyBosses\\resources\\firstBoss0.png").getImage();
        //mouth
        speed = 1;
        hitBoxes.add(new HitBox(width, height * 19 / 32, width / 2,
                height * 19 / 32, 0));
        //eyes
        critBoxes.add(new HitBox(width * 7 / 16, height * 13 / 32,
                width * 7 / 32, height, 0));
    }


    public boolean touchingPlayer(Player p){
        return HitBox.touching(hitBoxes, Mane.p1.hitBox) ||
                HitBox.touching(critBoxes, Mane.p1.hitBox);
    }


    public void move(){
        if(pattern == 0){
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            pattern = rand.nextInt(1, totalPatterns + 1);
            if(hp < 70){
                speed = 4;
            }else if(hp < 140){
                speed = 2;
            }

        }
        if(pattern == 1){
            figure8();
        }else if(pattern == 2){
            goToPlayer();
        }else if(pattern == 3){
            aroundTheEdge();
        }

        lookAtPlayer();
    }

    private void figure8(){
        if(p < 50 / speed){
            x += 8 * speed;
            y = (int) Math.round(Math.pow((x - 1000) / 20.0, 2) * 2 + 400);
        }else if(p < 100 / speed){
            x -= 8 * speed;
            y = (int) Math.round(-Math.pow((x - 1000) / 20.0, 2) * 2 + 800);
        }else if(p < 150 / speed){
            x -= 8 * speed;
            y = (int) Math.round(Math.pow((x - 600) / 20.0, 2) * 2 + 400);
        }else if(p < 200 / speed){
            x += 8 * speed;
            y = (int) Math.round(-Math.pow((x - 600) / 20.0, 2) * 2 + 800);
        }else{
            pattern = 0;
            p = 0;
            return;
        }
        p++;
    }

    final double chargeLength = 20;
    final int waitTime = 40;
    private void goToPlayer(){
        if(p == 0){
            changeX = (int) Math.round((Mane.p1.x - x) / chargeLength);
            changeY = (int) Math.round((Mane.p1.y - y) / chargeLength);
        }
        if(p < waitTime){
            //sit still
        }else if(p < waitTime + chargeLength){
            x += changeX;
            y += changeY;
        }else if(p < waitTime + chargeLength * 2){
            x -= changeX;
            y -= changeY;
        }else{
            pattern = 0;
            p = 0;
            return;
        }
        p++;
    }

    final int edgeMoveTime = 40;
    private void aroundTheEdge(){
        int xdist = (int) Math.round(600.0 / edgeMoveTime);
        int ydist = (int) Math.round(400.0 / edgeMoveTime);
        if(p < edgeMoveTime){
            x += xdist;
        }else if(p < edgeMoveTime * 2){
            y += ydist;
        }else if(p < edgeMoveTime * 4){
            x -= xdist;
        }else if(p < edgeMoveTime * 5){
            y -= ydist;
        }else if(p < edgeMoveTime * 6){
            x += xdist;
        }else{
            pattern = 0;
            p = 0;
            return;
        }
        p++;
    }

    //Finds which direction the player is from the boss and turns the eys that way.
    public void lookAtPlayer(){
        int px = Mane.p1.x - x;
        //subtract height so the boss is looking at eyes not feet
        int py = Mane.p1.y - Player.height - y + height;

        //there are 4 lines dividing the directions the boss can look
        //the lower the y the higher the payer
        boolean aboveL1 = py < -2 * px;
        boolean aboveL2 = py < px / -2;
        boolean aboveL3 = py < px / 2;
        boolean aboveL4 = py < 2 * px;

        int n = 0;
        if(aboveL1 && !aboveL2){
            n = 0;
        }else if(!aboveL1 && !aboveL4){
            n = 1;
        }else if(aboveL4 && !aboveL3){
            n = 2;
        }else if(aboveL3 && !aboveL2){
            n = 3;
        }else if(aboveL2 && !aboveL1){
            n = 4;
        }else if(aboveL1 && aboveL4){
            n = 5;
        }else if(aboveL3 && !aboveL4){
            n = 6;
        }else if(aboveL2 && !aboveL3){
            n = 7;
        }

        image = new ImageIcon(
                "src\\toManyBosses\\resources\\firstBoss" + n + ".png").getImage();
    }
}
