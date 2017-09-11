package edu.cap.model.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

/**
 * Created by Jaylin on 17-8-30.
 */
public class CityCountVO {

    @JSONField(name = "name")
    private String city;

    private float[] value;

    public CityCountVO(String city, float[] value) {
        this.city = city;
        this.value = value;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float[] getValue() {
        return value;
    }

    public void setValue(float[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CityCountVO{" +
                "city='" + city + '\'' +
                ", value=" + Arrays.toString(value) +
                '}';
    }
}
