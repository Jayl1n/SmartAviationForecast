package edu.cap.model.po;

import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 事件视图PO
 * @author Jaylin
 */
public class EventView {

    /**
     * 事件指纹
     */
    private String eventHash;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件地点
     */
    private String eventArea;

    /**
     * 事件类型
     */
    private Integer eventType;

    /**
     * 事件热度
     */
    private Integer eventHeat;

    /**
     * 历史悠久程度
     */
    private Integer eventHistory;

    /**
     * 开始日期
     */
    private Date eventBeginDate;

    /**
     * 结束日期
     */
    private Date eventFinishDate;

    /**
     * 事件类型原数据
     */
    private String eventTypeRaw;

    /**
     * 事件热度原数据
     */
    private String eventHeatRaw;

    /**
     * 事件历史次数原数据
     */
    private String eventHistoryRaw;

    /**
     * 事件一年内频率
     */
    private Integer eventFrequencyYear;

    /**
     * 事件一年内频率原数据
     */
    private String eventFrequencyYearRaw;

    /**
     * 主办方类型
     */
    private Integer eventOrganizerType;

    /**
     * 主办方等级
     */
    private Integer eventOrganizerLevel;

    /**
     * 影响年龄段
     */
    private Integer eventInfluenceAge;

    /**
     * 事件最大影响范围
     */
    private Integer eventMaxInfluence;

    /**
     * 有无固定参与人群
     */
    private Integer eventHasFixedPopulation;

    /**
     * 影响人群（商务|大众）
     */
    private Integer eventInfluenceType;

    /**
     * 事件主办方原数据
     */
    private String eventOrganizer;

    public String getEventHash() {
        return eventHash;
    }

    public void setEventHash(String eventHash) {
        this.eventHash = eventHash == null ? null : eventHash.trim();
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName == null ? null : eventName.trim();
    }

    public String getEventArea() {
        return eventArea;
    }

    public void setEventArea(String eventArea) {
        this.eventArea = eventArea == null ? null : eventArea.trim();
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
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

    public Date getEventBeginDate() {
        return eventBeginDate;
    }

    public void setEventBeginDate(Date eventBeginDate) {
        this.eventBeginDate = eventBeginDate;
    }

    public Date getEventFinishDate() {
        return eventFinishDate;
    }

    public void setEventFinishDate(Date eventFinishDate) {
        this.eventFinishDate = eventFinishDate;
    }

    public String getEventTypeRaw() {
        return eventTypeRaw;
    }

    public void setEventTypeRaw(String eventTypeRaw) {
        this.eventTypeRaw = eventTypeRaw == null ? null : eventTypeRaw.trim();
    }

    public String getEventHeatRaw() {
        return eventHeatRaw;
    }

    public void setEventHeatRaw(String eventHeatRaw) {
        this.eventHeatRaw = eventHeatRaw == null ? null : eventHeatRaw.trim();
    }

    public String getEventHistoryRaw() {
        return eventHistoryRaw;
    }

    public void setEventHistoryRaw(String eventHistoryRaw) {
        this.eventHistoryRaw = eventHistoryRaw == null ? null : eventHistoryRaw.trim();
    }

    public Integer getEventFrequencyYear() {
        return eventFrequencyYear;
    }

    public void setEventFrequencyYear(Integer eventFrequencyYear) {
        this.eventFrequencyYear = eventFrequencyYear;
    }

    public String getEventFrequencyYearRaw() {
        return eventFrequencyYearRaw;
    }

    public void setEventFrequencyYearRaw(String eventFrequencyYearRaw) {
        this.eventFrequencyYearRaw = eventFrequencyYearRaw == null ? null : eventFrequencyYearRaw.trim();
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

    public Integer getEventInfluenceType() {
        return eventInfluenceType;
    }

    public void setEventInfluenceType(Integer eventInfluenceType) {
        this.eventInfluenceType = eventInfluenceType;
    }

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer == null ? null : eventOrganizer.trim();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventView that = (EventView) o;

        return Objects.equals(this.eventArea, that.eventArea) &&
                Objects.equals(this.eventBeginDate, that.eventBeginDate) &&
                Objects.equals(this.eventFinishDate, that.eventFinishDate) &&
                Objects.equals(this.eventFrequencyYear, that.eventFrequencyYear) &&
                Objects.equals(this.eventFrequencyYearRaw, that.eventFrequencyYearRaw) &&
                Objects.equals(this.eventHasFixedPopulation, that.eventHasFixedPopulation) &&
                Objects.equals(this.eventHash, that.eventHash) &&
                Objects.equals(this.eventHeat, that.eventHeat) &&
                Objects.equals(this.eventHeatRaw, that.eventHeatRaw) &&
                Objects.equals(this.eventHistory, that.eventHistory) &&
                Objects.equals(this.eventHistoryRaw, that.eventHistoryRaw) &&
                Objects.equals(this.eventInfluenceAge, that.eventInfluenceAge) &&
                Objects.equals(this.eventInfluenceType, that.eventInfluenceType) &&
                Objects.equals(this.eventMaxInfluence, that.eventMaxInfluence) &&
                Objects.equals(this.eventName, that.eventName) &&
                Objects.equals(this.eventOrganizer, that.eventOrganizer) &&
                Objects.equals(this.eventOrganizerLevel, that.eventOrganizerLevel) &&
                Objects.equals(this.eventOrganizerType, that.eventOrganizerType) &&
                Objects.equals(this.eventType, that.eventType) &&
                Objects.equals(this.eventTypeRaw, that.eventTypeRaw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventArea, eventBeginDate, eventFinishDate, eventFrequencyYear, eventFrequencyYearRaw,
                            eventHasFixedPopulation,
                            eventHash, eventHeat, eventHeatRaw, eventHistory, eventHistoryRaw,
                            eventInfluenceAge, eventInfluenceType, eventMaxInfluence, eventName, eventOrganizer,
                            eventOrganizerLevel, eventOrganizerType, eventType, eventTypeRaw
        );
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("eventArea = " + eventArea)
                .add("eventBeginDate = " + eventBeginDate)
                .add("eventFinishDate = " + eventFinishDate)
                .add("eventFrequencyYear = " + eventFrequencyYear)
                .add("eventFrequencyYearRaw = " + eventFrequencyYearRaw)
                .add("eventHasFixedPopulation = " + eventHasFixedPopulation)
                .add("eventHash = " + eventHash)
                .add("eventHeat = " + eventHeat)
                .add("eventHeatRaw = " + eventHeatRaw)
                .add("eventHistory = " + eventHistory)
                .add("eventHistoryRaw = " + eventHistoryRaw)
                .add("eventInfluenceAge = " + eventInfluenceAge)
                .add("eventInfluenceType = " + eventInfluenceType)
                .add("eventMaxInfluence = " + eventMaxInfluence)
                .add("eventName = " + eventName)
                .add("eventOrganizer = " + eventOrganizer)
                .add("eventOrganizerLevel = " + eventOrganizerLevel)
                .add("eventOrganizerType = " + eventOrganizerType)
                .add("eventType = " + eventType)
                .add("eventTypeRaw = " + eventTypeRaw)
                .toString();
    }
}