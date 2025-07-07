package com.taletrails.taletrails_backend.manager;

import com.taletrails.taletrails_backend.manager.data.WalkInfo;
import com.taletrails.taletrails_backend.manager.data.WalkSummaryInfo;

import java.util.List;

public interface WalkManager {
    void saveWalk(WalkInfo walkInfo);
    List<WalkSummaryInfo> getWalksByUserId(Long userId);

}