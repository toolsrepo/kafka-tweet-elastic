package com.github.nathd.poc.kafka.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nathd.poc.kafka.service.TwitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TweetKafkaApi {

    private final TwitterService twitterService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/tweets/start")
    public ResponseEntity startConsumingTweets(@RequestParam("tag") String trackText) {
        twitterService.startConsumption(trackText);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/tweets/stop")
    public ResponseEntity stopConsumingTweets() {
        twitterService.stopConsumption();
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/tweets")
    public ResponseEntity<List<String>> getConsumedTweets() {
        List<String> tweets = twitterService.getTweets().stream()
                .map(tweet -> new StringBuilder()
                        .append("[")
                        .append(tweet.getId()).append(",")
                        .append(tweet.getFromUser()).append(",")
                        .append(tweet.getText())
                        .append("]")
                        .toString())
        .collect(Collectors.toList());
        return ResponseEntity.ok(tweets);
    }

}
