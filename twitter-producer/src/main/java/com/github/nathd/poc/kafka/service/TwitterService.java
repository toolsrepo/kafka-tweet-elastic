package com.github.nathd.poc.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwitterService {

    private final Twitter twitter;
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    private Stream twitterStream;
    ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static final BlockingQueue<Tweet> msgQueue = new LinkedBlockingDeque<>(100000);

    public void startConsumption(String text) {
        FilterStreamParameters filterStreamParameters =
                (FilterStreamParameters) new FilterStreamParameters().track(text);

        twitterStream = twitter.streamingOperations().filter(filterStreamParameters, singletonList(new TweeterStream()));
        executorService.submit(() -> {
            while(true) {
                try {
                    Tweet tweet = msgQueue.take();
                    kafkaProducerService.send("twitter", this.objectMapper.writeValueAsString(tweet));
                } catch (InterruptedException e) {
                    log.error("Interrupted exception", e);
                } catch (Exception e) {
                    log.error("Exception", e);
                }
            }
        });
    }

    public void stopConsumption() {
        if(twitterStream != null) twitterStream.close();
        executorService.shutdown();
    }

    public List<Tweet> getTweets() {
        return new ArrayList<>(msgQueue);
    }

    private static class TweeterStream implements StreamListener {

        @Override
        public void onTweet(Tweet tweet) {
            String message = String.format("User [%s], Tweeted : [%s]", tweet.getUser().getName(), tweet.getText());
            log.debug(message);
            msgQueue.add(tweet);
        }

        @Override
        public void onDelete(StreamDeleteEvent streamDeleteEvent) {

        }

        @Override
        public void onLimit(int i) {

        }

        @Override
        public void onWarning(StreamWarningEvent streamWarningEvent) {

        }
    }
}
