package com.taletrails.taletrails_backend.manager.impl;

import com.taletrails.taletrails_backend.exception.LogitracError;
import com.taletrails.taletrails_backend.exception.LogitrackException;
import com.taletrails.taletrails_backend.manager.WalkManager;
import com.taletrails.taletrails_backend.manager.data.WalkInfo;
import com.taletrails.taletrails_backend.manager.data.WalkSummaryInfo;
import com.taletrails.taletrails_backend.provider.UserProvider;
import com.taletrails.taletrails_backend.provider.WalkProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    }

    @Override
    public List<WalkSummaryInfo> getWalksByUserId(Long userId) {
        if (userProvider.getUserById(userId).isEmpty()) {
            throw new LogitrackException(LogitracError.ACCOUNT_DOES_NOT_EXIST);
        }
        return walkProvider.getWalksByUserId(userId);
    }

}
