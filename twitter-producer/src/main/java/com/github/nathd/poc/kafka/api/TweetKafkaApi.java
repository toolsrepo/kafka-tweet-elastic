package com.github.nathd.poc.kafka.api;

import com.github.nathd.poc.kafka.service.TwitterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TweetKafkaApi {

    private TwitterService twitterService;

    public TweetKafkaApi(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

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
        List<String> tweets = twitterService.getTweets();
        return ResponseEntity.ok(tweets);
    }

}
