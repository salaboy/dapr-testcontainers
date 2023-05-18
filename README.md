# Dapr :: Testcontainers integration and Demo

This repository contains some examples showing the Dapr project being used with Test Containers. 

There are different examples covering different Dapr Components, but in theory, you should be able to start all the backend applications locally by running `mvn spring-boot:test-run` on the `write-app`, `read-app` and `subscriber-app`. 

For these applications to share the infrastructure components you should configure the TestContainers `reuse` mode in the file `~/.testcontainers.properties` add `testcontainers.reuse.enable = true`


For more information, I will be adding the recording for my presentation at Spring I/O 2023 - Barcelona, Spain soon. 


# Extras

Install Redis in Kubernetes using Helm 

```
helm install redis bitnami/redis --set architecture=standalone
```

Install PostgreSQL in Kubernetes using Helm
```
helm install postgresql bitnami/postgresql --set auth.enablePostgresUser=true --version 11.6.6
```

Build `do-some-sql` example:
```
docker build --platform linux/amd64  -t salaboy/do-some-sql:testcontainers .
```