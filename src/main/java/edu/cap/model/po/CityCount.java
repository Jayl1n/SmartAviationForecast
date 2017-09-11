package edu.cap.model.po;

/**
 * Created by Jaylin on 17-8-30.
 */
public class CityCount {

    private String city;

    private long count;

    public CityCount(String city, long count) {
        this.city = city;
        this.count = count;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CityCount{" +
                "city='" + city + '\'' +
                ", count=" + count +
                '}';
    }
}
