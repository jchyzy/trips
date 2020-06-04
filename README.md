# Trips manager (client side)
This is a web application for managing trips. For working properly it requires running 
[trips server](https://github.com/Ula2017/trip_server "Trip server repository"). 
Server address can be changed in *application.properties* file (*remote_server* property)


### Run the application
To run the application, Java (with environment variable *JAVA_HOME* set) and RabbitMQ server
must be installed. You can download and run RabbitMQ Server on your computer with plugins 
*rabbitmq_mqtt rabbitmq_federation_management rabbitmq_stomp rabbitmq_management* or use Docker 
container.
####Downloading project
```
$ git clone https://github.com/jchyzy/trips.git
```
####Using Docker with RabbitMQ servver
###### Creating image with RabbitMQ
Firstly, you need to have docker image. You can download it from docker-hub or creating with Dockerfie.

```
Creating sobou/trip-rabbitmq image
$ cd trips/src/main/resources/docker/RabbitMQ
$ docker build -t sobou/trip-rabbitmq:latest .
```
```
Downloading sobou/trip-rabbitmq image
$ docker pull sobou/trip-rabbitmq:latest
```

###### Create and run container
Create container
```bash
$ docker create --name rabbitmq-server -it -p 61613:61613 sobou/trip-rabbitmq
```
To start or stop your container use
```bash
$ docker start rabbitmq-server
$ docker stop rabbitmq-server
```
#####Run application
```
$ cd trips
$ mvnw spring-boot:run
```
After start, application URL should be printed in the console.