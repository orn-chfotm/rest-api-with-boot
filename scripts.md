# Scripts

Here, I memo scripts that I have used during development.

## Postgres

### Run Postgres Container

```
docker run --name postgres_rest_boot -p 5432:5432 -e POSTGRES_PASSWORD=pass -d postgres

```

This cmdlet will create Postgres instance so that you can connect to a database with:
* database: postgres_rest_boot
* username: postgres
* password: pass
* post: 5432

### Getting into the Postgres container

```
docker exec -i -t postgres_rest_boot bash
```

Then you will see the containers bash as a root user.

### Connect to a database

```
psql -d postgres -U postgres
```

### Query Databases

```
\l
```

### Query Tables

```
\dt
```

### Quit

```
\q
```

## application.properties

### Datasource

```
spring:
  datasource:
    username: postgres
    password: pass
    url: jdbc:postgresql://localhost:5432/postgres
    driver-class-name: org.postgresql.Driver
```

### Hibernate

```
spring:
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        format_sql: true

logging:
  level:
    org:
      hibernate:
        SQL: DEBUG
        type: TRACE
```

[//]: # (spring.jpa.hibernate.ddl-auto=create-drop)

[//]: # (spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true)

[//]: # (spring.jpa.properties.hibernate.format_sql=true)

[//]: # ()
[//]: # (logging.level.org.hibernate.SQL=DEBUG)

[//]: # (logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE)

### Test Database

```
spring:
  datasource:
    username: sa
    password: 
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
```