package ls.main.bean;

import java.io.Serializable;

/**
 * Created by admin on 2016/8/28.
 */
public class BMIBean implements Serializable {

    private int id;
    private String weight;
    private String height;
    private String bmi;
    private String time;

    public BMIBean(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
