# Trips manager (client side)
This is a web application for managing trips. For working properly it requires running 
trips server (https://github.com/Ula2017/trip_server). 
Server address can be changed in *application.properties* file (*remote_server* property)


### Run the application
To run the application, Java must be installed (and environment variable *JAVA_HOME* must be set).
```
$ git clone https://github.com/jchyzy/trips.git
$ cd trips
$ mvnw spring-boot:run
```
After start, application URL should be printed in the console.