package com.taletrails.taletrails_backend.controller;


import com.taletrails.taletrails_backend.controller.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.locationtech.jts.geom.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class locationController {

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326); // WGS84

    @GetMapping("/hello")
    public SuccessResponse helloWorld(HttpServletRequest request){
        return new SuccessResponse("hello");
    }
    @GetMapping("/check")
    public ResponseEntity<SuccessResponse> isWithinRange(
            @RequestParam double userLat,
            @RequestParam double userLng,
            @RequestParam double checkpointLat,
            @RequestParam double checkpointLng) {

        // Convert to JTS Coordinates (X = Longitude, Y = Latitude)
        Coordinate userCoord = new Coordinate(userLng, userLat);
        Coordinate checkpointCoord = new Coordinate(checkpointLng, checkpointLat);

        Point checkpointPoint = geometryFactory.createPoint(checkpointCoord);

        // Buffer (circle) with 150m radius in degrees — convert meters to degrees (approx)
        double radiusInMeters = 150;
        double radiusInDegrees = metersToDegrees(radiusInMeters, checkpointLat);

        Geometry circle = checkpointPoint.buffer(radiusInDegrees);

        Point userPoint = geometryFactory.createPoint(userCoord);

        boolean isInside = circle.contains(userPoint);

        return ResponseEntity.ok(new SuccessResponse(String.valueOf(isInside)));
    }

    // Approximate conversion for small distances
    private double metersToDegrees(double meters, double latitude) {
        // 1 degree of latitude ~= 111,000 meters
        return meters / 111_000.0;
    }
}
