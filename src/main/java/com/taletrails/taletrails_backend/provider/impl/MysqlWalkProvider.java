package com.taletrails.taletrails_backend.provider.impl;

import com.taletrails.taletrails_backend.entities.Route;
import com.taletrails.taletrails_backend.entities.User;
import com.taletrails.taletrails_backend.entities.Walk;
import com.taletrails.taletrails_backend.exception.LogitracError;
import com.taletrails.taletrails_backend.exception.LogitrackException;
import com.taletrails.taletrails_backend.manager.data.WalkDetailInfo;
import com.taletrails.taletrails_backend.manager.data.WalkInfo;
import com.taletrails.taletrails_backend.manager.data.WalkSummaryInfo;
import com.taletrails.taletrails_backend.provider.WalkProvider;
import com.taletrails.taletrails_backend.repositories.RouteRepository;
import com.taletrails.taletrails_backend.repositories.UserRepository;
import com.taletrails.taletrails_backend.repositories.WalkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MysqlWalkProvider implements WalkProvider {

    @Autowired
    WalkRepository walkRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RouteRepository routeRepository;

    @Override
    public void saveWalkWithRoutes(WalkInfo walkInfo) {
        Optional<User> userOpt = userRepository.findById(walkInfo.getUserId());
//        if (userOpt.isEmpty()) return;

        Walk walk = new Walk();
        walk.setUser(userOpt.get());
        walk.setGenre(walkInfo.getGenre());
        walk.setStopDist(walkInfo.getStopDist());
        walk.setNoOfStops(walkInfo.getNoOfStops());

        walk = walkRepository.save(walk);

        for (WalkInfo.Route routeData : walkInfo.getRoute()) {
            Route route = new Route();
            route.setWalk(walk);
            route.setLatitude(routeData.getLatitude());
            route.setLongitude(routeData.getLongitude());
            route.setRouteOrder(routeData.getOrder());
            route.setLockStatus(0);
            route.setStorySegment("A new mystery unfolds at this stop. You feel a shift in the atmosphere...");

            routeRepository.save(route);
        }
    }
    @Override
    public List<WalkSummaryInfo> getWalksByUserId(Long userId) {
        User user = userRepository.findById(userId).get();

        List<Walk> walks = walkRepository.findByUser(user);
        List<WalkSummaryInfo> summaries = new ArrayList<>();

        for (Walk walk : walks) {
            WalkSummaryInfo info = new WalkSummaryInfo();
            info.setWalkId(walk.getId());
            info.setGenre(walk.getGenre());
            info.setNoOfStops(walk.getNoOfStops());
            info.setStopDist(walk.getStopDist());
            summaries.add(info);
        }

        return summaries;
    }
    @Override
    public Optional<WalkDetailInfo> getWalkDetailsById(Long walkId) {
        Optional<Walk> walkOpt = walkRepository.findById(walkId);
        if (walkOpt.isEmpty()) return Optional.empty();

        Walk walk = walkOpt.get();
        List<Route> routeList = routeRepository.findByWalk(walk);

        WalkDetailInfo info = new WalkDetailInfo();
        info.setWalkId(walk.getId());
        info.setGenre(walk.getGenre());
        info.setNoOfStops(walk.getNoOfStops());
        info.setStopDistance(walk.getStopDist());

        int unlocked = (int) routeList.stream().filter(r -> r.getLockStatus() == 1).count();
        info.setPlacesUnlocked(unlocked);
        info.setPlacesLocked(walk.getNoOfStops() - unlocked);

        List<WalkDetailInfo.Route> routeInfos = routeList.stream().map(r -> {
            WalkDetailInfo.Route route = new WalkDetailInfo.Route();
            route.setRouteId(r.getId());
            route.setOrder(r.getRouteOrder());
            route.setLatitude(r.getLatitude());
            route.setLongitude(r.getLongitude());
            route.setLockStatus(r.getLockStatus());
            if (r.getLockStatus() == 1) {
                route.setStorySegment(r.getStorySegment()); // ✅ Only if unlocked
            }
//            else {
//                route.setStorySegment(null); // Optional: to hide locked content
//            }
            return route;
        }).toList();

        info.setRoutes(routeInfos);
        return Optional.of(info);
    }


}
