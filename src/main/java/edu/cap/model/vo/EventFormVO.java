package edu.cap.model.vo;

import java.util.StringJoiner;

/**
 * Created by Jaylin on 17-8-24.
 */
public class EventFormVO {
    private String name;
    private String city;
    private int heat;

    public EventFormVO(String name, String city, int heat) {
        this.name = name;
        this.city = city;
        this.heat = heat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("city = " + city)
                .add("heat = " + heat)
                .add("name = " + name)
                .toString();
    }
}
