package com.taletrails.taletrails_backend.controller.dto;

import java.util.List;

public class AddWalkRequest {
    private Long userId;
    private String genre;
    private int stopDist;
    private int noOfStops;
    private List<Route> route;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getStopDist() {
        return stopDist;
    }

    public void setStopDist(int stopDist) {
        this.stopDist = stopDist;
    }

    public int getNoOfStops() {
        return noOfStops;
    }

    public void setNoOfStops(int noOfStops) {
        this.noOfStops = noOfStops;
    }

    public List<Route> getRoute() {
        return route;
    }

    public void setRoute(List<Route> route) {
        this.route = route;
    }

    public static class Route {
        private int order;
        private double latitude;
        private double longitude;

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
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
}
