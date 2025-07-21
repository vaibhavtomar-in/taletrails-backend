package com.taletrails.taletrails_backend.controller;


import com.taletrails.taletrails_backend.controller.dto.AddWalkRequest;
import com.taletrails.taletrails_backend.controller.dto.SuccessResponse;
import com.taletrails.taletrails_backend.controller.dto.WalkDetailsResponse;
import com.taletrails.taletrails_backend.controller.dto.WalkSummary;
import com.taletrails.taletrails_backend.manager.WalkManager;
import com.taletrails.taletrails_backend.manager.data.WalkDetailInfo;
import com.taletrails.taletrails_backend.manager.data.WalkInfo;
import com.taletrails.taletrails_backend.manager.data.WalkSummaryInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/walks")
public class WalkController {
    @Autowired
    WalkManager walkManager;

    @PostMapping("/add-walk")
    public SuccessResponse addWalk(HttpServletRequest request, @RequestBody AddWalkRequest walkRequest) {
        walkManager.saveWalk(transform(walkRequest));
        return new SuccessResponse("Walk and route saved successfully");
    }

    @GetMapping("/user-walks")
    public List<WalkSummary> getWalksForUser(HttpServletRequest request, @RequestParam Long userId) {
        List<WalkSummaryInfo> walkInfos = walkManager.getWalksByUserId(userId);
        return walkInfos.stream().map(info -> {
            WalkSummary summary = new WalkSummary();
            summary.setWalkId(info.getWalkId());
            summary.setGenre(info.getGenre());
            summary.setNoOfStops(info.getNoOfStops());
            summary.setStopDist(info.getStopDist());
            return summary;
        }).collect(Collectors.toList());
    }
    @GetMapping("/details")
    public WalkDetailsResponse getWalkDetails(HttpServletRequest request, @RequestParam Long walkId) {
        WalkDetailInfo info = walkManager.getWalkDetails(walkId);
        return transform(info);
    }

    @GetMapping("/checkin")
    public SuccessResponse checkInAtRoute(
            HttpServletRequest request,
            @RequestParam double userLat,
            @RequestParam double userLng,
            @RequestParam Long routeId) {

        boolean unlocked = walkManager.attemptCheckIn(userLat, userLng, routeId);
        if (unlocked) {
            return new SuccessResponse("You are within range! Check-in successful.");
        } else {
            return new SuccessResponse("You are not close enough to this stop.");
        }
    }

    private WalkDetailsResponse transform(WalkDetailInfo info) {
        WalkDetailsResponse response = new WalkDetailsResponse();
        response.setWalkId(info.getWalkId());
        response.setGenre(info.getGenre());
        response.setNoOfStops(info.getNoOfStops());
        response.setStopDistance(info.getStopDistance());
        response.setPlacesUnlocked(info.getPlacesUnlocked());
        response.setPlacesLocked(info.getPlacesLocked());
        response.setStatus(info.getStatus());

        List<WalkDetailsResponse.RouteInfo> routeInfos = info.getRoutes().stream().map(r -> {
            WalkDetailsResponse.RouteInfo ri = new WalkDetailsResponse.RouteInfo();
            ri.setRouteId(r.getRouteId());
            ri.setOrder(r.getOrder());
            ri.setLatitude(r.getLatitude());
            ri.setLongitude(r.getLongitude());
            ri.setLockStatus(r.getLockStatus());
            if (r.getLockStatus() == 1) {
                ri.setStorySegment(r.getStorySegment()); // ✅ Only if unlocked
            }
//            else {
//                ri.setStorySegment(null); // Optional: to hide locked content
//            }
            return ri;
        }).toList();

        response.setRoutes(routeInfos);
        return response;
    }


    WalkInfo transform(AddWalkRequest walkRequest){
        WalkInfo walkInfo = new WalkInfo();
        walkInfo.setUserId(walkRequest.getUserId());
        walkInfo.setGenre(walkRequest.getGenre());
        walkInfo.setStopDist(walkRequest.getStopDist());
        walkInfo.setNoOfStops(walkRequest.getNoOfStops());

        walkInfo.setRoute(
                walkRequest.getRoute().stream().map(r -> {
                    WalkInfo.Route ri = new WalkInfo.Route();
                    ri.setOrder(r.getOrder());
                    ri.setLatitude(r.getLatitude());
                    ri.setLongitude(r.getLongitude());
                    return ri;
                }).collect(Collectors.toList())
        );
        return walkInfo;
    }
}
