package com.taletrails.taletrails_backend.provider;

import com.taletrails.taletrails_backend.manager.data.WalkDetailInfo;
import com.taletrails.taletrails_backend.manager.data.WalkInfo;
import com.taletrails.taletrails_backend.manager.data.WalkSummaryInfo;

import java.util.List;
import java.util.Optional;

public interface WalkProvider {
    void saveWalkWithRoutes(WalkInfo walkInfo);
    List<WalkSummaryInfo> getWalksByUserId(Long userId);
    Optional<WalkDetailInfo> getWalkDetailsById(Long walkId);


}
