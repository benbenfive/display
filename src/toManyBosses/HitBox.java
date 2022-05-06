package toManyBosses;

import java.util.ArrayList;

/**
 * @author Benjamin Vick
 */
public class HitBox {
    int left, top, right, bottom;
    int width, height, offsetX, offsetY;
    int whoAmI;// 0 == boss 1-4 == players 1-4

    public HitBox(int inWidth, int inHeight, int inXOffset, int inYOffset, int whosBox){
        width = inWidth;
        height = inHeight;
        offsetX = inXOffset;
        offsetY = inYOffset;
        whoAmI = whosBox;
    }

    private void updatePosition(){
        int x = 0, y = 0;
        switch(whoAmI){
            case 0 :
                x = Mane.currentBoss.x;
                y = Mane.currentBoss.y;
                break;
            case 1 :
                x = Mane.p1.x;
                y = Mane.p1.y;
                break;
        }

        left = x - offsetX;
        top = y - offsetY;
        right = left + width;
        bottom = top + height;
    }

    public static boolean touching(ArrayList<HitBox> boxes,
                                   int left2, int top2, int right2, int bottom2){
        for(HitBox box1 : boxes){
            box1.updatePosition();

            int left1 = box1.left;
            int top1 = box1.top;
            int right1 = box1.right;
            int bottom1 = box1.bottom;

            if(bottom1 > top2  && top1 < bottom2){
                if(right1 > left2 && left1 < right2){
                    return true;
                }
            }
        }
        return false;
    }


    // this is a simplified version of what we just wrote together
    public static boolean touching(int left1, int top1, int right1, int bottom1,
                                   int left2, int top2, int right2, int bottom2){
        if(bottom1 > top2  && top1 < bottom2){
            if(right1 > left2 && left1 < right2){
                return true;
            }
        }
        return false;
    }

    public static boolean touching(ArrayList<HitBox> boxes1, ArrayList<HitBox> boxes2){
        boolean firstTime = true;
        for(HitBox box1 : boxes1){
            for(HitBox box2 : boxes2){
                box1.updatePosition();

                //so we don't waste time updating the 2nd array multiple times
                if(firstTime) {
                    box2.updatePosition();
                }

                int left1 = box1.left;
                int top1 = box1.top;
                int right1 = box1.right;
                int bottom1 = box1.bottom;

                int left2 = box2.left;
                int top2 = box2.top;
                int right2 = box2.right;
                int bottom2 = box2.bottom;

                if(bottom1 > top2  && top1 < bottom2){
                    if(right1 > left2 && left1 < right2){
                        return true;
                    }
                }
            }
            firstTime = false;
        }
        return false;
    }

    public static boolean touching(ArrayList<HitBox> boxes1, HitBox box2){
        for(HitBox box1 : boxes1){
            box1.updatePosition();
            box2.updatePosition();

            int left1 = box1.left;
            int top1 = box1.top;
            int right1 = box1.right;
            int bottom1 = box1.bottom;

            int left2 = box2.left;
            int top2 = box2.top;
            int right2 = box2.right;
            int bottom2 = box2.bottom;

            if(bottom1 > top2  && top1 < bottom2){
                if(right1 > left2 && left1 < right2){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean touching(HitBox box){
        updatePosition();
        box.updatePosition();

        int left2 = box.left;
        int top2 = box.top;
        int right2 = box.right;
        int bottom2 = box.bottom;
        if(bottom > top2  && top < bottom2){
            if(right > left2 && left < right2){
                return true;
            }
        }
        return false;
    }
}
