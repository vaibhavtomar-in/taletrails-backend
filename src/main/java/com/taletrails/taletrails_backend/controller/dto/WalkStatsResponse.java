package com.taletrails.taletrails_backend.controller.dto;

import java.util.List;

public class WalkStatsResponse {
    private Integer totalDistance;
    private Integer completedWalks;
    private Integer incompleteWalks;
    private Integer placesVisited;
    private Integer placesNotVisited;
    private List<VisitedPlace> visitedPlaces;
    private List<NotVisitedPlace> notVisitedPlaces;

    public static class VisitedPlace {
        public Long walkId;
        public double latitude;
        public double longitude;
        public String storySegment;

        public Long getWalkId() {
            return walkId;
        }

        public void setWalkId(Long walkId) {
            this.walkId = walkId;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getStorySegment() {
            return storySegment;
        }

        public void setStorySegment(String storySegment) {
            this.storySegment = storySegment;
        }
    }

    public static class NotVisitedPlace {
        public Long walkId;
        public double latitude;
        public double longitude;

        public Long getWalkId() {
            return walkId;
        }

        public void setWalkId(Long walkId) {
            this.walkId = walkId;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public Integer getIncompleteWalks() {
        return incompleteWalks;
    }

    public void setIncompleteWalks(Integer incompleteWalks) {
        this.incompleteWalks = incompleteWalks;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Integer totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Integer getCompletedWalks() {
        return completedWalks;
    }

    public void setCompletedWalks(Integer completedWalks) {
        this.completedWalks = completedWalks;
    }

    public Integer getPlacesVisited() {
        return placesVisited;
    }

    public void setPlacesVisited(Integer placesVisited) {
        this.placesVisited = placesVisited;
    }

    public Integer getPlacesNotVisited() {
        return placesNotVisited;
    }

    public void setPlacesNotVisited(Integer placesNotVisited) {
        this.placesNotVisited = placesNotVisited;
    }

    public List<VisitedPlace> getVisitedPlaces() {
        return visitedPlaces;
    }

    public void setVisitedPlaces(List<VisitedPlace> visitedPlaces) {
        this.visitedPlaces = visitedPlaces;
    }

    public List<NotVisitedPlace> getNotVisitedPlaces() {
        return notVisitedPlaces;
    }

    public void setNotVisitedPlaces(List<NotVisitedPlace> notVisitedPlaces) {
        this.notVisitedPlaces = notVisitedPlaces;
    }
    // Getters and Setters omitted for brevity
}
