# Categorization Service
This `categorization-service` Spring Boot microservice implements a Kafka consumer to fetch incoming transactions from a `incoming-transactions` topic.
It also implements a producer to publish that transactions to a `categorized-transactions` topic once these have been categorized.
