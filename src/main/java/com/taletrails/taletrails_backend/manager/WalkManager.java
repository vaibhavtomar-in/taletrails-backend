package com.taletrails.taletrails_backend.manager;

import com.taletrails.taletrails_backend.controller.dto.WalkStatsResponse;
import com.taletrails.taletrails_backend.manager.data.WalkDetailInfo;
import com.taletrails.taletrails_backend.manager.data.WalkInfo;
import com.taletrails.taletrails_backend.manager.data.WalkSummaryInfo;

import java.util.List;

public interface WalkManager {
    void saveWalk(WalkInfo walkInfo);
    List<WalkSummaryInfo> getWalksByUserId(Long userId);
    WalkDetailInfo getWalkDetails(Long walkId);
    boolean attemptCheckIn(double userLat, double userLng, Long routeId);
    WalkStatsResponse getWalkStatsByUser(Long userId);



}