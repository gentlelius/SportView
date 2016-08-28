package ls.main.bean;

import java.io.Serializable;

/**
 * Created by admin on 2016/8/27.
 */
public class TargetDay implements Serializable{
    private int duration;
    private double miles;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }
}
