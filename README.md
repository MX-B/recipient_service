Moneyex Payments - Recipient
=====================

This project does the recipient part of the Moneyex payment gateway.

## docker-compose.yml

```
version: '2.1'

services:

  recipients:
    hostname: recipients
    image: 
    ulimits:
      memlock:
        soft: -1
        hard: -1
      nofile: 65536
    ports:
      - "8080:8080"
    environment:
      - LOG_LEVEL=INFO
      - PORT=8080
      - JDBC_CONNECTION_STRING=
      - PAGARME_APIKEY=
      - DB_USER=
      - DB_PASSWORD=
      - AUTH_CLIENT_SECRET=
      - AUTH_CLIENT=
      - AUTH_REALM=
      - AUTH_SERVER_URL= 
```

## Environment Configuration

| ENV Name                                  | Description                                           | Required | Default Value                                                                                               |
|-------------------------------------------|-------------------------------------------------------|----------|-------------------------------------------------------------------------------------------------------------|
| `JDBC_CONNECTION_STRING`                  | Used to create connection with the database           | **Yes**  | `jdbc:mysql://localhost:3306/Moneyex_ic_usage?useLegacyDatetimeCode=false&useTimezone=true&serverTimezone=UTC` |
| `DB_USER`                                 | MySQL Database User                                   | No       | `<blank>`                                                                                                   |
| `DB_PASSWORD`                             | MySQL Database Password                               | No       | `<blank>`                                                                                                   |
| `PORT`                                    | TCP Port where the server will run                    | No       | `8080`                                                                                                      |
| `LOG_LEVEL`                               | Log level                                             | No       | `INFO`                                                                                                      |
| `AUTH_CLIENT_SECRET`                      | Secret to authenticate with Keycloak                  | **Yes**  |                                                                                                             |
| `AUTH_CLIENT`                             | ClientID to authenticate with Keycloak                | No       | `Moneyex-ic-usage`                                                                                             |
| `AUTH_REALM`                              | Realm to authenticate with Keycloak                   | No       | `innovation-cloud`                                                                                          |
| `AUTH_SERVER_URL`                         | Keycloak Auth URL                                     | No       | `https://is.dev.Moneyex.io/auth`                                                                               |
| `PAGARME_APIKEY`                          | Pagar.me API Key                                      | **Yes**  | `<blank>`                                                                                                   |

## External services

1. Uses `MySQL` to store metrics data
2. Uses `Keycloak` to authenticate between services and also to authenticate endpoints
3. Uses `Pagar.me` to manage Recipients

## Keycloak

- Client ID: `Moneyex-payments-recipients`
- Service Account Enabled: **Yes**

### Roles

| Name                | Description                | Composite | Composite Roles |
|---------------------|----------------------------|-----------|-----------------|
| `user`              | Role to read only          | No        | -               |
| `admin`             | Admin role with all roles  | **Yes**   | `user`          |

### Service Account Roles

| Client ID          | Required Roles  | Description                                     |
|--------------------|-----------------|-------------------------------------------------|
| `NONE`             | `NONE`          | `NONE`                                          |

## Development

### Install

This project doesn't require installation

### Run

Run as java application in Eclipse or IntelliJ or Use docker-compose as mentioned above

### Run Tests

First configure some env variables

| ENV Name         | Description                | Required | Default Value   |
|------------------|----------------------------|----------|-----------------|
| `DB_URL`         | Address to MySQL database  | No       | `localhost`     |
| `DB_PORT`        | Port to MySQL database     | No       | `3306`          |
| `DB_NAME`        | Database schema            | No       | `Moneyex_test`     |
| `DB_USER`        | MySQL database User        | No       | `root`          |
| `DB_PASSWORD`    | MySQL database password    | No       | `passw0rd`      |
| `PAGARME_APIKEY` | Pagar.me API Key           | **Yes**  | `<blank>`       |


Then run:

```
mvn test
```