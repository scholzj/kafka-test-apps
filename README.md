# Kafka test applications

This repository contains simple clients which can be used for testing of Apache Kafka deployments. They are also 
dockerized and can be used in Kubernetes / OpenShift. See the [`kafka-consumer.yaml`](kafka-consumer.yaml) and 
[`kafka-producer.yaml`](kafka-producer.yaml) files as examples.

The [`deployment.yaml`](deployment.yaml) and [`job.yaml`](job.yaml) files contain a full deployment of Producer, Consumer and Topic definition for [Strimzi Topic Operator](http://strimzi.io).
