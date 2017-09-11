package edu.cap.model.vo;

/**
 * Created by Jaylin on 17-8-23.
 */
public class OverallMeritVO {
    /**
     * 类型
     */
    private String type;

    /**
     * 数值
     */
    private String value;

    /**
     * 序列
     */
    private String series;

    public OverallMeritVO(String type, String value, String series) {
        this.type = type;
        this.value = value;
        this.series = series;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }
}
