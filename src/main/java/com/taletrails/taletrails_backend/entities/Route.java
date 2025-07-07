package com.taletrails.taletrails_backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int routeOrder;

    private double latitude;

    private double longitude;

    private int lockStatus = 0;

    private String storySegment = "As you arrive at this stop, a gentle breeze whispers through the trees, hinting at the adventure ahead...";

    @ManyToOne
    @JoinColumn(name = "walk_id", referencedColumnName = "id", nullable = false)
    private Walk walk;

    public Long getId() {
        return id;
    }

    public int getRouteOrder() {
        return routeOrder;
    }

    public void setRouteOrder(int routeOrder) {
        this.routeOrder = routeOrder;
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

    public int getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getStorySegment() {
        return storySegment;
    }

    public void setStorySegment(String storySegment) {
        this.storySegment = storySegment;
    }

    public Walk getWalk() {
        return walk;
    }

    public void setWalk(Walk walk) {
        this.walk = walk;
    }
}
