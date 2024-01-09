FROM openjdk:8
COPY target/broker-0.0.1-jar-with-dependencies.jar /broker-0.0.1.jar
CMD java -cp /broker-0.0.1.jar broker.sender.BrokerMain  $container_name