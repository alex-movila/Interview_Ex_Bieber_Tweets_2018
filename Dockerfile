FROM openjdk:latest
WORKDIR /app
COPY /target/bieber-tweets-1.0.0-SNAPSHOT.jar app.jar

CMD ["java", "-cp", "app.jar", "org.interview.oauth.twitter.BieberTweetsMain"]