package edu.cap.model.po;

import java.util.StringJoiner;

/**
 * Created by Jaylin on 17-8-24.
 */
public class EventCount {
    private long type;


    private String city;

    private long value;

    public EventCount(long type, long value) {
        this.type = type;
        this.value = value;
    }

    public EventCount(String city, long value) {
        this.city = city;
        this.value = value;
    }

    public EventCount(String city, int type, long value) {
        this.type = type;
        this.value = value;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("type = " + type)
                .add("value = " + value)
                .toString();
    }
}
