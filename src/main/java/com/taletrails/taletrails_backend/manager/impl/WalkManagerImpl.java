package com.taletrails.taletrails_backend.manager.impl;

import com.taletrails.taletrails_backend.entities.Route;
import com.taletrails.taletrails_backend.entities.Walk;
import com.taletrails.taletrails_backend.exception.LogitracError;
import com.taletrails.taletrails_backend.exception.LogitrackException;
import com.taletrails.taletrails_backend.manager.WalkManager;
import com.taletrails.taletrails_backend.manager.data.WalkDetailInfo;
import com.taletrails.taletrails_backend.manager.data.WalkInfo;
import com.taletrails.taletrails_backend.manager.data.WalkSummaryInfo;
import com.taletrails.taletrails_backend.provider.UserProvider;
import com.taletrails.taletrails_backend.provider.WalkProvider;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalkManagerImpl implements WalkManager {

    @Autowired
    WalkProvider walkProvider;
    @Autowired
    UserProvider userProvider;

    @Override
    public void saveWalk(WalkInfo walkInfo) {
        if (walkInfo.getUserId() == null) {
            throw new LogitrackException(LogitracError.ACCOUNT_DOES_NOT_EXIST);
        }

        // Validate user exists
        if (userProvider.getUserById(walkInfo.getUserId()).isEmpty()) {
            throw new LogitrackException(LogitracError.ACCOUNT_DOES_NOT_EXIST);
        }

        // Validate genre
        if (walkInfo.getGenre() == null || walkInfo.getGenre().isBlank()) {
            throw new LogitrackException(LogitracError.INVALID_REQUEST_DATA);
        }

        // Validate stop distance
        if (walkInfo.getStopDist() <= 0) {
            throw new LogitrackException(LogitracError.INVALID_REQUEST_DATA);
        }

        // Validate number of stops
        if (walkInfo.getNoOfStops() <= 0) {
            throw new LogitrackException(LogitracError.INVALID_REQUEST_DATA);
        }

        // Validate route list
        if (walkInfo.getRoute() == null || walkInfo.getRoute().isEmpty()) {
            throw new LogitrackException(LogitracError.INVALID_REQUEST_DATA);
        }

        // Validate each route point
        for (WalkInfo.Route route : walkInfo.getRoute()) {
            if (route.getOrder() < 0 || route.getLatitude() == 0 || route.getLongitude() == 0) {
                throw new LogitrackException(LogitracError.INVALID_REQUEST_DATA);
            }
        }
        walkProvider.saveWalkWithRoutes(walkInfo);
        walkProvider.generateAndAttachStory(walkInfo);
    }

    @Override
    public List<WalkSummaryInfo> getWalksByUserId(Long userId) {
        if (userProvider.getUserById(userId).isEmpty()) {
            throw new LogitrackException(LogitracError.ACCOUNT_DOES_NOT_EXIST);
        }
        return walkProvider.getWalksByUserId(userId);
    }
    @Override
    public WalkDetailInfo getWalkDetails(Long walkId) {
        Optional<WalkDetailInfo> info = walkProvider.getWalkDetailsById(walkId);
        if (info.isEmpty()) {
            throw new LogitrackException(LogitracError.INVALID_REQUEST_DATA);
        }
        return info.get();
    }
    @Override
    public boolean attemptCheckIn(double userLat, double userLng, Long routeId) {
        if (routeId == null) {
            throw new LogitrackException(LogitracError.INVALID_REQUEST_DATA);
        }

        Route route = walkProvider.getRouteById(routeId)
                .orElseThrow(() -> new LogitrackException(LogitracError.INVALID_REQUEST_DATA));

        double checkpointLat = route.getLatitude();
        double checkpointLng = route.getLongitude();

        Coordinate userCoord = new Coordinate(userLng, userLat);
        Coordinate checkpointCoord = new Coordinate(checkpointLng, checkpointLat);

        GeometryFactory geometryFactory = new GeometryFactory();
        Point checkpointPoint = geometryFactory.createPoint(checkpointCoord);

        double radiusInMeters = 200;
        double radiusInDegrees = metersToDegrees(radiusInMeters, checkpointLat);

        Geometry circle = checkpointPoint.buffer(radiusInDegrees);
        Point userPoint = geometryFactory.createPoint(userCoord);

        boolean isInside = circle.contains(userPoint);

        if (isInside && route.getLockStatus() == 0) {
            walkProvider.unlockRoute(routeId); // delegate to provider
        }



        return isInside;
    }

    private double metersToDegrees(double meters, double latitude) {
        return meters / 111_000.0;
    }



}
