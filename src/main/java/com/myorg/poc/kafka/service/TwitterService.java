package com.myorg.poc.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.Collections.singletonList;

@Service
@Slf4j
public class TwitterService {

    private Twitter twitter;
    private Stream twitterStream;

    private static final BlockingQueue<String> msgQueue = new LinkedBlockingDeque<>(100000);

    public TwitterService(Twitter twitter) {
        this.twitter = twitter;
    }

    public void startConsumption(String text) {
        FilterStreamParameters filterStreamParameters =
                (FilterStreamParameters) new FilterStreamParameters().track(text);

        twitterStream = twitter.streamingOperations().filter(filterStreamParameters, singletonList(new TweeterStream()));
    }

    public void stopConsumption() {
        twitterStream.close();
    }

    public List<String> getTweets() {
        return new ArrayList<>(msgQueue);
    }

    private static class TweeterStream implements StreamListener {

        @Override
        public void onTweet(Tweet tweet) {
            msgQueue.add(String.format("User '{}', Tweeted : {}", tweet.getUser().getName(), tweet.getText()));
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
