package edu.cap.model.po;

import java.util.StringJoiner;

/**
 * Created by Jaylin on 17-8-23.
 */
public class OverallMerit {
    private float avgOrganizerLevel;
    private float avgMaxInfluence;
    private float avgHeat;
    private float avgHistory;
    private float avgFrequencyYear;

    public OverallMerit() {
        this.avgOrganizerLevel = 0f;
        this.avgMaxInfluence = 0f;
        this.avgHeat = 0f;
        this.avgHistory = 0f;
        this.avgFrequencyYear = 0f;
    }

    public float getAvgOrganizerLevel() {
        return avgOrganizerLevel;
    }

    public void setAvgOrganizerLevel(float avgOrganizerLevel) {
        this.avgOrganizerLevel = avgOrganizerLevel;
    }

    public float getAvgMaxInfluence() {
        return avgMaxInfluence;
    }

    public void setAvgMaxInfluence(float avgMaxInfluence) {
        this.avgMaxInfluence = avgMaxInfluence;
    }

    public float getAvgHeat() {
        return avgHeat;
    }

    public void setAvgHeat(float avgHeat) {
        this.avgHeat = avgHeat;
    }

    public float getAvgHistory() {
        return avgHistory;
    }

    public void setAvgHistory(float avgHistory) {
        this.avgHistory = avgHistory;
    }

    public float getAvgFrequencyYear() {
        return avgFrequencyYear;
    }

    public void setAvgFrequencyYear(float avgFrequencyYear) {
        this.avgFrequencyYear = avgFrequencyYear;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("avgFrequencyYear = " + avgFrequencyYear)
                .add("avgHeat = " + avgHeat)
                .add("avgHistory = " + avgHistory)
                .add("avgMaxInfluence = " + avgMaxInfluence)
                .add("avgOrganizerLevel = " + avgOrganizerLevel)
                .toString();
    }
}

