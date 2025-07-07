package com.taletrails.taletrails_backend.provider;

import com.taletrails.taletrails_backend.manager.data.WalkInfo;
import com.taletrails.taletrails_backend.manager.data.WalkSummaryInfo;

import java.util.List;

public interface WalkProvider {
    void saveWalkWithRoutes(WalkInfo walkInfo);
    List<WalkSummaryInfo> getWalksByUserId(Long userId);

}
