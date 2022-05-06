package toManyBosses;

import javax.swing.*;
import java.awt.*;

/**
 * @author Benjamin Vick
 */
public class Player {
    int x = 0, y = 0, speed = 20, yVel = 0,
            hp = 10,
            safeTime = 0, lastJump = -1000;
    public static int width = 64, height = 128;
    int attackRange = width * 5 / 2, attackHeight = height * 3 / 4;
    boolean canJump = false, lookedRightLast = true, canAttackAgain = true;
    Image imager, imagel, attackl, attackr, attacku, attackd;
    Barrier lineOn = null;
    HitBox hitBox;

    int playerNum = 1;
    public Player(int startX, int startY){
        x = startX;
        y = startY;
        hitBox = new HitBox(width, height, width / 2, height, 1);
        imager = new ImageIcon(
                "src\\toManyBosses\\resources\\p1r.png").getImage();
        imagel = new ImageIcon(
                "src\\toManyBosses\\resources\\p1l.png").getImage();
        attackr = new ImageIcon(
                "src\\toManyBosses\\resources\\attackr.png").getImage();
        attackl = new ImageIcon(
                "src\\toManyBosses\\resources\\attackl.png").getImage();
        attacku = new ImageIcon(
                "src\\toManyBosses\\resources\\attacku.png").getImage();
        attackd = new ImageIcon(
                "src\\toManyBosses\\resources\\attackd.png").getImage();
        playerNum++;
    }



    public void move(int changeX, int changeY){
        x += changeX;
        y += changeY;
    }



    //attack area points
    int left = 0;
    int right = 0;
    int top = 0;
    int bot = 0;

    public int damageDone(int direction){
        int midy = y - height / 2;

        //sets the attack points based on the direction of the attack
        switch (direction){
            case 1 ://up
                left = x - attackHeight;
                right = x + attackHeight;
                top = midy - attackRange;
                bot = midy - height / 2;
                break;
            case 2 ://right
                left = x + width / 2;
                right = x + attackRange;
                top = midy - attackHeight;
                bot = midy + attackHeight;
                break;
            case 3 ://down
                left = x - attackHeight;
                right = x + attackHeight;
                top = y;
                bot = midy + attackRange;
                break;
            case 4 ://left
                left = x - attackRange;
                right = x - width / 2;
                top = midy - attackHeight;
                bot = midy + attackHeight;
                break;
        }

        //check if the attack box overlaps with the boss crit box
        if(HitBox.touching(Mane.currentBoss.critBoxes, left, top, right, bot)){
            return 2;
            //check hit boxes
        }else if(HitBox.touching(Mane.currentBoss.hitBoxes, left, top, right, bot)){
            return 1;
        }
        return 0;
    }

    public boolean outOfTheWorld(){
        return y > 1000;
    }

    public void setX(int ix){
        x = ix;
    }

    public void changeYVel(int changeCy){
        yVel += changeCy;
    }

    public void setYVel(int changeCy){
        yVel = changeCy;
    }

    public void setY(int newy){
        y = newy;
    }

    public boolean onGround(){
        return y >= 800;
    }

    public int getSpeed(){
        return speed;
    }
}
