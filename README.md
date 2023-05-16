# Dapr :: Testcontainers integration and Demo

Running `docker-compose up` will start two Java Spring Boot Applications that uses the Dapr StateStore component to write and read data from it. 

The docker-compose.yml file contains the configuration for both apps and their sidecars as well as the Dapr "Control Plane" (Redis and the Dapr Placement service) 




# Extras

Install Redis in Kubernetes using Helm 

```
helm install redis bitnami/redis --set architecture=standalone
```

Install PostgreSQL in Kubernetes using Helm
```
helm install postgresql bitnami/postgresql --set auth.enablePostgresUser=true
```

Build `do-some-sql` example:
```
docker build --platform linux/amd64  -t salaboy/do-some-sql:testcontainers .
```