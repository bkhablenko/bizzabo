bizzabo
=======

## How to run

```bash
./gradlew clean build -x test && docker compose up --build
```

API documentation will be available at http://localhost:8090.

## Sample API requests

Users may authenticate with _any_ username as long as they provide an empty password.

**NOTE:** For demonstration purposes only.

### Get the TV schedule

```bash
curl -i http://localhost:8080/api/v1/user/schedule/shows \
  -H 'Authorization: Basic Sm9obi5TbWl0aDo='
```

### Add a show to the TV schedule

```bash
curl -i http://localhost:8080/api/v1/user/schedule/shows \
  -H 'Authorization: Basic Sm9obi5TbWl0aDo=' \
  -H 'Content-Type: application/json' \
  --data '{"showId":82}'
```

### Remove a show from the TV schedule

```bash
curl -i http://localhost:8080/api/v1/user/schedule/shows/82 \
  -H 'Authorization: Basic Sm9obi5TbWl0aDo=' \
  -X DELETE
```

### Get the next unwatched episode for each show on the TV schedule

```bash
curl -i http://localhost:8080/api/v1/user/schedule/watch-next \
  -H 'Authorization: Basic Sm9obi5TbWl0aDo='
```

### Mark an episode as watched

```bash
curl -i http://localhost:8080/api/v1/user/watched-episodes \
  -H 'Authorization: Basic Sm9obi5TbWl0aDo=' \
  -H 'Content-Type: application/json' \
  --data '{"episodeId":4952}'
```

### Remove a watched episode

```bash
curl -i http://localhost:8080/api/v1/user/watched-episodes/4952 \
  -H 'Authorization: Basic Sm9obi5TbWl0aDo=' \
  -X DELETE
```

## Implementation details

- We use [OpenFeign](https://github.com/OpenFeign/feign) to make API calls to https://api.tvmaze.com.

- TVmaze responses are cached for up to 5 minutes.

- App data is persisted in PostgreSQL. Based on our use case, however, a key-value storage like Redis would also
  suffice.

## Additional features

- Ready to build on CircleCI (see [.circleci/config.yml](.circleci/config.yml))

### Observability

The following features are crucial for production readiness:

- **Logs** are written as JSON and ready for collection
- Prometheus **metrics** are exposed at http://localhost:8080/metrics
- OpenTelemetry **traces** are pushed directly to Jaeger, see http://localhost:16686

## TODO

- [ ] Authorize endpoint access with OAuth 2
- [ ] Include trace ID as a response header
- [ ] Build the app and run tests with Docker Compose
- [ ] Implement an end-to-end test suite

## License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) file for details.
