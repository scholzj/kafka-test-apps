FROM enmasseproject/java-base:8-3

ARG version=latest
ENV VERSION ${version}
ADD target/kafka-test-consumer.jar /

CMD ["/bin/launch_java.sh", "/kafka-test-consumer.jar"]