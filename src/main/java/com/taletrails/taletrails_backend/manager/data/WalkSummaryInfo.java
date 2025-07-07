package com.taletrails.taletrails_backend.manager.data;

public class WalkSummaryInfo {
    private Long walkId;
    private String genre;
    private int noOfStops;
    private int stopDist;

    public Long getWalkId() {
        return walkId;
    }

    public void setWalkId(Long walkId) {
        this.walkId = walkId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getNoOfStops() {
        return noOfStops;
    }

    public void setNoOfStops(int noOfStops) {
        this.noOfStops = noOfStops;
    }

    public int getStopDist() {
        return stopDist;
    }

    public void setStopDist(int stopDist) {
        this.stopDist = stopDist;
    }
}
