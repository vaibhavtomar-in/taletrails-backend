package com.taletrails.taletrails_backend.controller;


import com.taletrails.taletrails_backend.controller.dto.AddWalkRequest;
import com.taletrails.taletrails_backend.controller.dto.SuccessResponse;
import com.taletrails.taletrails_backend.controller.dto.WalkSummary;
import com.taletrails.taletrails_backend.manager.WalkManager;
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
    public List<WalkSummary> getWalksForUser(@RequestParam Long userId) {
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
