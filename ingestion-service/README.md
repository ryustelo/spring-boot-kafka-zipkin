# Ingestion Service
This `ingestion-service` Spring Boot microservice implements a Kafka producer to publish incoming transactions to a `incoming-transactions` topic.
It receives that transactions from and endpoint published under `/ingestion-service/transactions`.
A Swagger OpenAPI definition will be published and accessible in http://localhost:8080/swagger-ui.html after running the service.
That Swagger UI can be used to send requests to the service, or just running a curl command similar to

```bash
$ curl 'http://localhost:8080/ingestion-service/transactions' \
  -H 'Content-Type: application/json' \
  -d '{"transactionId": 168}'
```