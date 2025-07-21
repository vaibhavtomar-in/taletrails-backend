package com.taletrails.taletrails_backend.manager.data;

import java.util.List;

public class WalkDetailInfo {
    private Long walkId;
    private String genre;
    private int noOfStops;
    private int stopDistance;
    private int placesUnlocked;
    private int placesLocked;
    private List<Route> routes;
    private String status;


    public static class Route {
        private Long routeId;
        private int order;
        private double latitude;
        private double longitude;
        private int lockStatus;
        private String storySegment;

        // Getters and setters
        public Long getRouteId() { return routeId; }
        public void setRouteId(Long routeId) { this.routeId = routeId; }

        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }

        public double getLatitude() { return latitude; }
        public void setLatitude(double latitude) { this.latitude = latitude; }

        public double getLongitude() { return longitude; }
        public void setLongitude(double longitude) { this.longitude = longitude; }

        public int getLockStatus() { return lockStatus; }
        public void setLockStatus(int lockStatus) { this.lockStatus = lockStatus; }
        public String getStorySegment() {
            return storySegment;
        }

        public void setStorySegment(String storySegment) {
            this.storySegment = storySegment;
        }
    }

    // Getters and setters


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getWalkId() { return walkId; }
    public void setWalkId(Long walkId) { this.walkId = walkId; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getNoOfStops() { return noOfStops; }
    public void setNoOfStops(int noOfStops) { this.noOfStops = noOfStops; }

    public int getStopDistance() { return stopDistance; }
    public void setStopDistance(int stopDistance) { this.stopDistance = stopDistance; }

    public int getPlacesUnlocked() { return placesUnlocked; }
    public void setPlacesUnlocked(int placesUnlocked) { this.placesUnlocked = placesUnlocked; }

    public int getPlacesLocked() { return placesLocked; }
    public void setPlacesLocked(int placesLocked) { this.placesLocked = placesLocked; }

    public List<Route> getRoutes() { return routes; }
    public void setRoutes(List<Route> routes) { this.routes = routes; }
}
