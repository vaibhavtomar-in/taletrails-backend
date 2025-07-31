package com.taletrails.taletrails_backend.provider.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taletrails.taletrails_backend.entities.Route;
import com.taletrails.taletrails_backend.entities.User;
import com.taletrails.taletrails_backend.entities.UserQuizAnswer;
import com.taletrails.taletrails_backend.entities.Walk;
import com.taletrails.taletrails_backend.exception.LogitracError;
import com.taletrails.taletrails_backend.exception.LogitrackException;
import com.taletrails.taletrails_backend.manager.data.WalkDetailInfo;
import com.taletrails.taletrails_backend.manager.data.WalkInfo;
import com.taletrails.taletrails_backend.manager.data.WalkSummaryInfo;
import com.taletrails.taletrails_backend.provider.WalkProvider;
import com.taletrails.taletrails_backend.repositories.RouteRepository;
import com.taletrails.taletrails_backend.repositories.UserQuizAnswerRepository;
import com.taletrails.taletrails_backend.repositories.UserRepository;
import com.taletrails.taletrails_backend.repositories.WalkRepository;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.locationtech.jts.geom.*;

@Service
public class MysqlWalkProvider implements WalkProvider {
    private static final Logger logger = LoggerFactory.getLogger(MysqlWalkProvider.class);

    @Autowired
    WalkRepository walkRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RouteRepository routeRepository;
    @Autowired
    UserQuizAnswerRepository answerRepo;


    private final String HF_API_URL = "https://router.huggingface.co/novita/v3/openai/chat/completions";
    @Value("${HF_API_KEY}")
    private String HF_API_KEY;

    @Override
    public void saveWalkWithRoutes(WalkInfo walkInfo) {
        Optional<User> userOpt = userRepository.findById(walkInfo.getUserId());
//        if (userOpt.isEmpty()) return;

        Walk walk = new Walk();
        walk.setUser(userOpt.get());
        walk.setGenre(walkInfo.getGenre());
        walk.setStopDist(walkInfo.getStopDist());
        walk.setNoOfStops(walkInfo.getNoOfStops());
        walk.setTotalDistance(1);
//        walk.setTeaser("This is teaser for the stop");
        walk = walkRepository.save(walk);
        GeometryFactory geometryFactory = new GeometryFactory();
        Route previousRoute = null;
        int distanceMeters = 0;
        for (WalkInfo.Route routeData : walkInfo.getRoute()) {
            Route route = new Route();
            route.setWalk(walk);
            route.setLatitude(routeData.getLatitude());
            route.setLongitude(routeData.getLongitude());
            route.setRouteOrder(routeData.getOrder());
            route.setLockStatus(0);
            logger.info("route loc {} : lat {} ,long {}", routeData.getOrder(),routeData.getLatitude(),routeData.getLongitude());

            // Calculate distance if previous route exists
            if (previousRoute != null) {
                Coordinate coord1 = new Coordinate(previousRoute.getLongitude(), previousRoute.getLatitude());
                Coordinate coord2 = new Coordinate(route.getLongitude(), route.getLatitude());

                Point point1 = geometryFactory.createPoint(coord1);
                Point point2 = geometryFactory.createPoint(coord2);

                distanceMeters += (int) (point1.distance(point2) * 111_000); // approx conversion: 1 deg ≈ 111 km

            }

            previousRoute = route;
            routeRepository.save(route);
        }

        Walk walk1 = walkRepository.findById(walk.getId()).get();

        walk1.setTotalDistance(distanceMeters);
        walkRepository.save(walk1);
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
            info.setStopDist(walk.getTotalDistance());
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
        info.setStatus(walk.getStatus());
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

    @Override
    public Optional<Route> getRouteById(Long routeId) {
        return routeRepository.findById(routeId);
    }

    @Override
    public void unlockRoute(Long routeId) {
        routeRepository.findById(routeId).ifPresent(route -> {
            route.setLockStatus(1);
            routeRepository.save(route);
        });
        WalkDetailInfo walk = getWalkDetailsById(routeRepository.findById(routeId).get().getWalk().getId()).get();
        boolean allLocked = walk.getRoutes().stream().allMatch(route -> route.getLockStatus() == 1);
        if(allLocked){
            Walk walkEntity = walkRepository.findById(walk.getWalkId()).get();
            walkEntity.setStatus("COMPLETED");
            walkRepository.save(walkEntity);
        }

    }

    @Async
    public void generateAndAttachStory(WalkInfo walkInfo) {
        List<UserQuizAnswer> answers = answerRepo.findByUser_Id(walkInfo.getUserId());

        String answerPrompt = answers.stream()
                .map(ans -> ans.getQuestionId() + ". " + ans.getQuestion() + ": " + ans.getSelectedOption())
                .collect(Collectors.joining("|"));

        String fullPrompt = String.format("Generate an interactive story in the genre %s based on the user's personality profile. Here are the question and answers:|%s List out steps and rehearseSplit the story into A teaser and %d parts where end each part with a cliff hanger to keep reader engaged and return ONLY a valid xml . output in this format ONLY: <teaser></teaser><part1>xyz</part1><part2>xyz</part2>...<partn>xyz</partn>"
                , walkInfo.getGenre(), answerPrompt, walkInfo.getNoOfStops());
        logger.info("Prompt sent to HuggingFace:\n{}", fullPrompt);
        // Send API Request
        String xmlResponse = callHuggingFaceAPI(fullPrompt);
        if (xmlResponse == null) {
            logger.error("Hugging Face response is null");
            return;
        }

        logger.info("Received XML response:\n{}", xmlResponse);

        // Extract parts using regex
        String teaser = extractTag(xmlResponse, "teaser");
        logger.info("Extracted Teaser:\n{}", teaser);
        Map<Integer, String> parts = new HashMap<>();
        for (int i = 1; i <= walkInfo.getNoOfStops(); i++) {
            String part = extractTag(xmlResponse, "part" + i);
            logger.info("Extracted Part {}:\n{}", i, part);
            if (part != null) parts.put(i, part);
        }

        // Update Walk and Routes
        Optional<Walk> walkOpt = walkRepository.findByUser_IdOrderByIdDesc(walkInfo.getUserId()).stream().findFirst();
        if (walkOpt.isEmpty()) return;
        Walk walk = walkOpt.get();
        walk.setTeaser(teaser);
        walkRepository.save(walk);

        List<Route> routes = routeRepository.findByWalk(walk);
        for (Route route : routes) {
            route.setStorySegment(parts.getOrDefault(route.getRouteOrder() , "To be revealed..."));

            routeRepository.save(route);
        }
//        List<Route> routes = routeRepository.findByWalk(walk);
//        for (Route route : routes) {
//            int partIndex = route.getRouteOrder() + 1;
//            String segment = parts.getOrDefault(partIndex, "To be revealed...");
//            logger.info("Setting story segment for route {}: {}", route.getId(), segment);
//            route.setStorySegment(segment);
//            routeRepository.save(route);
//        }
    }

    private String callHuggingFaceAPI(String prompt) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String requestBody = """
                {
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ],
                  "model": "mistralai/mistral-7b-instruct",
                  "stream": false
                }
            """.formatted(prompt.replace("\"", "\\\""));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(HF_API_URL))
                    .header("Authorization", "Bearer " + HF_API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return extractContentFromJson(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String extractContentFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            return null;
        }
    }

    private String extractTag(String xml, String tag) {
        Pattern pattern = Pattern.compile("<%s>(.*?)</%s>".formatted(tag, tag), Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xml);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

}
