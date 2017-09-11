package edu.cap.model.vo;

import java.util.StringJoiner;

/**
 * Created by Jaylin on 17-8-24.
 */
public class SummaryVO {
    private String value;

    public SummaryVO(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("value = " + value)
                .toString();
    }
}
