package edu.cap.model.po;


import java.util.Date;

/**
 * 事件分析PO
 *
 * @author Jaylin
 */
public class Event {
    /**
     * 事件编号
     */
    private Integer id;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件类型
     */
    private Integer eventType;

    /**
     * 事件地点
     */
    private String eventArea;

    /**
     * 主办方类型
     */
    private Integer eventOrganizerType;

    /**
     * 主办方级别
     */
    private Integer eventOrganizerLevel;

    /**
     * 事件影响年龄层
     */
    private Integer eventInfluenceAge;

    /**
     * 事件最大影响范围
     */
    private Integer eventMaxInfluence;

    /**
     * 是否有固定参与人群
     */
    private Integer eventHasFixedPopulation;

    /**
     * 事件热度
     */
    private Integer eventHeat;

    /**
     * 事件历史悠久程度
     */
    private Integer eventHistory;

    /**
     * 事件一年内频率
     */
    private Integer eventFrequencyYear;

    /**
     * 事件指纹
     */
    private String eventHash;

    /**
     * 事件入库日期
     */
    private Date eventCreateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName == null ? null : eventName.trim();
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getEventArea() {
        return eventArea;
    }

    public void setEventArea(String eventArea) {
        this.eventArea = eventArea == null ? null : eventArea.trim();
    }

    public Integer getEventOrganizerType() {
        return eventOrganizerType;
    }

    public void setEventOrganizerType(Integer eventOrganizerType) {
        this.eventOrganizerType = eventOrganizerType;
    }

    public Integer getEventOrganizerLevel() {
        return eventOrganizerLevel;
    }

    public void setEventOrganizerLevel(Integer eventOrganizerLevel) {
        this.eventOrganizerLevel = eventOrganizerLevel;
    }

    public Integer getEventInfluenceAge() {
        return eventInfluenceAge;
    }

    public void setEventInfluenceAge(Integer eventInfluenceAge) {
        this.eventInfluenceAge = eventInfluenceAge;
    }

    public Integer getEventMaxInfluence() {
        return eventMaxInfluence;
    }

    public void setEventMaxInfluence(Integer eventMaxInfluence) {
        this.eventMaxInfluence = eventMaxInfluence;
    }

    public Integer getEventHasFixedPopulation() {
        return eventHasFixedPopulation;
    }

    public void setEventHasFixedPopulation(Integer eventHasFixedPopulation) {
        this.eventHasFixedPopulation = eventHasFixedPopulation;
    }

    public Integer getEventHeat() {
        return eventHeat;
    }

    public void setEventHeat(Integer eventHeat) {
        this.eventHeat = eventHeat;
    }

    public Integer getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(Integer eventHistory) {
        this.eventHistory = eventHistory;
    }

    public Integer getEventFrequencyYear() {
        return eventFrequencyYear;
    }

    public void setEventFrequencyYear(Integer eventFrequencyYear) {
        this.eventFrequencyYear = eventFrequencyYear;
    }

    public String getEventHash() {
        return eventHash;
    }

    public void setEventHash(String eventHash) {
        this.eventHash = eventHash == null ? null : eventHash.trim();
    }

    public Date getEventCreateDate() {
        return eventCreateDate;
    }

    public void setEventCreateDate(Date eventCreateDate) {
        this.eventCreateDate = eventCreateDate;
    }


}