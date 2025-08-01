package com.taletrails.taletrails_backend.repositories;

import com.taletrails.taletrails_backend.entities.Route;
import com.taletrails.taletrails_backend.entities.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByWalk(Walk walk);
    List<Route> findByWalk_User_IdAndLockStatus(Long userId, int lockStatus);
    List<Route> findByWalk_User_IdAndLockStatusNot(Long userId, int lockStatus);


}
