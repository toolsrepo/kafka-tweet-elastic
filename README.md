# kafka-tweet-elastic

# Run twitter-producer app
```shell script
  cd twitter-producer
  java -Dtwitter-app-id=${app-id} -Dtwitter-api-secret=${api-secret} -Dtwitter-access-token=${access-token} \
        -Dtwitter-access-secret=${access-secret} -jar target/<app.jar>  
```

# Run elastic-consumer app
```shell script
  cd elastic-consumer
  java -jar target/<app.jar>  
```