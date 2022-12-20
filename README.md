# Search Service

**Authors**: Nadav Babai | [3pc GmbH](https://www.3pc.de)

Service to import assets into elasticsearch. The service create indexes and provide search functionality based on the created indexes.

### Build, Run Test

- Build Service: ```gradlew clean build```
- Run Unit Tests: ```gradlew clean test```
- Run Integration Tests: ```gradlew clean integrationTest```
- Run Service ```gradlew bootRun``` (Docker-Compose Services vorher starten)

### Tech-Stack
- [Java 17](https://openjdk.java.net/projects/jdk/17/)
- [GraphQL](https://graphql.org/)
- [Elasticsearch](https://www.elastic.co/)
- [Protobuf](https://developers.google.com/protocol-buffers)
- Weitere Bibliotheken: Siehe ```build.gradle```

### Loading related profile

For xcurator, rub with following profile:
```-Dspring.profiles.active=xcurator```


### More Info | Documentation | Papers
- [Elasticsearch Similarity](https://www.elastic.co/guide/en/elasticsearch/reference/current/similarity.html)
- [Elasticsearch Queries with Spring Data](https://www.baeldung.com/spring-data-elasticsearch-queries)
- [Alternatives to mapping types](https://www.elastic.co/guide/en/elasticsearch/reference/current/removal-of-types.html)
- [Managing Relations Inside Elasticsearch](https://www.elastic.co/blog/managing-relations-inside-elasticsearch)
- [Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html)
