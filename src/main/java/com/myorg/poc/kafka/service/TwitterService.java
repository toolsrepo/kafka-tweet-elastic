package com.myorg.poc.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.Collections.singletonList;

@Service
@Slf4j
public class TwitterService {

    private Twitter twitter;
    private KafkaProducerService kafkaProducerService;
    private Stream twitterStream;
    ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static final BlockingQueue<String> msgQueue = new LinkedBlockingDeque<>(100000);

    public TwitterService(Twitter twitter, KafkaProducerService kafkaProducerService) {
        this.twitter = twitter;
        this.kafkaProducerService = kafkaProducerService;
    }

    public void startConsumption(String text) {
        FilterStreamParameters filterStreamParameters =
                (FilterStreamParameters) new FilterStreamParameters().track(text);

        twitterStream = twitter.streamingOperations().filter(filterStreamParameters, singletonList(new TweeterStream()));
        executorService.submit(() -> {
            while(true) {
                try {
                    String tweet = msgQueue.take();
                    kafkaProducerService.send("tweeter-topic", tweet);
                } catch (InterruptedException e) {
                    log.error("Interrupted exception", e);
                } catch (Exception e) {
                    log.error("Exception", e);
                }
            }
        });
    }

    public void stopConsumption() {
        twitterStream.close();
        executorService.shutdown();
    }

    public List<String> getTweets() {
        return new ArrayList<>(msgQueue);
    }

    private static class TweeterStream implements StreamListener {

        @Override
        public void onTweet(Tweet tweet) {
            String message = String.format("User [%s], Tweeted : [%s]", tweet.getUser().getName(), tweet.getText());
            log.info(message);
            msgQueue.add(message);
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
