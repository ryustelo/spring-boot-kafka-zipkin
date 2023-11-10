# Ingestion Service
This `ingestion-service` Spring Boot microservice implements a Kafka producer to publish incoming transactions to a `incoming-transactions` topic.
It receives that transactions from and endpoint published under `/ingestion-service/transactions`.
A Swagger OpenAPI definition will be published and accessible in [Transactions OpenAPI definition](http://localhost:8080/swagger-ui.html) after running the service in your local computer.

A Docker Compose file named `compose.yml` has been included to help setting up all the infrastructure needed to run the service.
It includes a Kafka cluster with two brokers and a script to create two topics:
- `incoming-transactions`
- `categorized-transactions`

Assuming your computer has already installed Docker and Docker Compose the command `docker compose up -d` should set up all the Kafka environment.

This service will usually run together with the [categorization-service](https://github.com/ryustelo/categorization-service), that will take that incoming transactions from Kafka, categorize them following some specific rules and publish again to the `categorized-transactions` topic.
The Docker Compose file to set up Kafka is also included in that service for easiness, but it's just needed to be executed once. 

The `compose.yml` file also includes a Zipkin server that both services are configured to send its traces all the way from the originating `ingestion-service` request down to the `categorization-service` Kafka publishing.
That Zipkin server is configured to run in http://localhost:9411/ by default.

![traces](docs/trace.png)

### Dependencies
As said, to properly run this `ingestion-service` the running computer will need a proper installation of Docker and Docker Compose.  

### Reference Documentation
For further reference, please consider the following sections:

* [Docker Compose Support](https://docs.spring.io/spring-boot/docs/3.1.4/reference/htmlsingle/index.html#features.docker-compose)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/docs/3.1.4/reference/htmlsingle/index.html#messaging.kafka)
* [Getting Started with Distributed Tracing](https://docs.spring.io/spring-boot/docs/3.1.4/reference/html/actuator.html#actuator.micrometer-tracing.getting-started)
