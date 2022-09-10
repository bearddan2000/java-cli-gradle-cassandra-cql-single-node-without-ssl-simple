# java-cli-gradle-cassandra-cql-single-node-without-ssl-simple

## Description
Creates a small database table
called `dog`. Uses batch operation to `insert | delete | update` dog table.

A java gradle build, that connects to single node
cassandra database without ssl.

## Tech stack
- docker-compose-wait
- java
- gradle
  - cql
  - cassandra drivers

## Docker stack
- cassandra:4.0
- gradle:jdk11

## To run
`sudo ./install.sh -u`

## To stop
`sudo ./install.sh -d`

## For help
`sudo ./install.sh -h`

## Credit
- [Docker setup](https://2much2learn.com/setting-up-cassandra-with-docker/)
- [Cql java client](https://github.com/eugenp/tutorials/tree/master/persistence-modules/java-cassandra)
