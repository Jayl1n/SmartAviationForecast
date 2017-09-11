package edu.cap.model.po;

/**
 * Created by Jaylin on 17-8-24.
 */
public class HotCity {
    private String city;
    private long value;
    private int series;

    public HotCity(String city, long value) {
        this.city = city;
        this.value = value;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }
}
