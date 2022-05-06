package toManyBosses;

/**
 * @author Benjamin Vick
 */
public class Barrier {
    int x1 = 0, y1 = 0;
    int x2 = 0, y2 = 0;
    double m = 0;
    int b = 0;

    //this is the constructor it is ran when an instance of the class is made
    //simply sets all the values to whatever is input
    public Barrier(int ix1, int iy1, int ix2, int iy2){
        x1 = ix1;
        y1 = iy1;
        x2 = ix2;
        y2 = iy2;
        //slope
        m = (double)(y1 - y2) / (x1 - x2);
        //offset
        b = (int)(y1 - x1 * m);
        //increases a variable that sets a max for a loop so that the loop won't go
        //into any empty parts of the array
        Mane.barriersInStage[Mane.currentStage]++;
    }

    //I'm sure you know what this formula is
    public int yAt(int x){
        return (int)(m * x + b);
    }

    //this checks if you are about to fall through the line
    //c stands for change
    public boolean goesPast(int x, int y, int cx, int cy){
        //special case for straight up and down barriers
        if(Math.abs(m) > 1000000){
            //check if you will pass the barrier
            if(y1 <= y + cy && y + cy <= y2) {
                return (x1 < x && x1 >= x + cx || x1 > x && x1 <= x + cx);
            }
        //checks if you are above the barrier
        }else if(x1 <= x + cx && x + cx <= x2) {
            //checks if you are above the line before and below the line after
            return yAt(x) >= y && y + cy > yAt(x + cx);
        }
        return false;
    }
}
