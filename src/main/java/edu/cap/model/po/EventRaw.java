package edu.cap.model.po;

import java.util.Date;

public class EventRaw {
    private Integer id;

    private String eventHash;

    private String eventName;

    private String eventArea;

    private String eventType;

    private Date eventBeginDate;

    private Date eventFinishDate;

    private String eventHeat;

    private String eventHistory;

    private String eventContent;

    private String eventSrcUrl;

    private String eventFrequencyYear;

    private String eventOrganizer;

    private Date eventCreateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType == null ? null : eventType.trim();
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

    public String getEventHeat() {
        return eventHeat;
    }

    public void setEventHeat(String eventHeat) {
        this.eventHeat = eventHeat == null ? null : eventHeat.trim();
    }

    public String getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(String eventHistory) {
        this.eventHistory = eventHistory == null ? null : eventHistory.trim();
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent == null ? null : eventContent.trim();
    }

    public String getEventSrcUrl() {
        return eventSrcUrl;
    }

    public void setEventSrcUrl(String eventSrcUrl) {
        this.eventSrcUrl = eventSrcUrl == null ? null : eventSrcUrl.trim();
    }

    public String getEventFrequencyYear() {
        return eventFrequencyYear;
    }

    public void setEventFrequencyYear(String eventFrequencyYear) {
        this.eventFrequencyYear = eventFrequencyYear == null ? null : eventFrequencyYear.trim();
    }

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer == null ? null : eventOrganizer.trim();
    }

    public Date getEventCreateDate() {
        return eventCreateDate;
    }

    public void setEventCreateDate(Date eventCreateDate) {
        this.eventCreateDate = eventCreateDate;
    }
}