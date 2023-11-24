# Drone Management System
With this server application you can manage drone on a field.
It is REST based and you can 

 - register drones
 - move drones
 - removing drones
 - get information about drones

## Behavior of the drones
 - A drone can only be registered inside the filed (default 10x10) so it can only have coordinates ranging from 0-10
 - A drone can only be registered where no other drone is.
 - On moving a drone it will follow this steps, whe ech steps takes the time given by the ``speed`` parameter:
   - orientate itself in the flying forward direction
   - fly along the x axis
   - orientate itself in the flying forward direction
   - fly along the y axis
   - orientate itself in the given direction
 - The drone performs collision detection. It will not move into a space occupied by another drone.
   - It will wait a few steps if the other drone moves out of the way, otherwise it will time out and abort the path. In this case the drone does not reach the destination.
   

## Managing drones via REST
assuming you are running the server on your local host `http://localhost:8080` which is the default you have the following REST endpoints:

### Data types
The information is sent to the REST API via json objects representing a drone:
````
Drone:
{
    name: String,
    posX: Int,
    posY: Int,
    direction: String,
    [speed: Int]
}
````
````
Error:
{
    "timestamp": DateTime,
    "status": 400/404,
    "error": "Bad Request/Not found",
    "message": String,
    "path": String
}
````

### Registering a drone
You can register a drone with its name, position, direction and speed.
The default speed is set to 200 and the default position is set to (0, 0) if not set manually.
- endpoint: `/registerDrone`
- content type: json
- content: Drone
- result content: Drone
- example:
- ``curl -X POST -H "Content-Type: application/json"
  -d '{"name": "test", "direction": "East", "posX": 3, "posY": 3}'
  http://localhost:8080/registerDrone/test``
- error: There will be an error if:
  - no name or direction is given
  - the name is already registered
  - the position is outside the field
  - there is already a done on that position

### Moving a drone
You can move a drone with its name and change position, direction and speed.
This call will start moving the drone will NOT give a feedback if the drone reached the destination.

- endpoint: `/moveDrone`
- content type: json
- content: Drone
- result content: String: ``Started moving``
- examples:
    - keeping speed: ``curl -X POST -H "Content-Type: application/json"
    -d '{"name": "test", "direction": "East", "posX": 5, "posY": 1}'
    http://localhost:8080/moveDrone``
    - changing speed: ``curl -X POST -H "Content-Type: application/json"
        -d '{"name": "test", "direction": "East", "posX": 5, "posY": 1, "speed": 500}'
        http://localhost:8080/moveDrone``
- error: There will be an error if:
    - no name or direction is given
    - the position is outside the field
    - no drone with that name was found

#### Moving a drone with feedback
You can use a different endpoint to move a drone and wait for it to reach its target position.
- endpoint: `/moveDroneWait`
- content type: json
- content: Drone
- result content: json / Drone
- examples:
    - keeping speed: ``curl -X POST -H "Content-Type: application/json"
      -d '{"name": "test", "direction": "East", "posX": 5, "posY": 1}'
      http://localhost:8080/moveDroneWait``

### Deleting a drone
You can delete a drone just by its name.
- endpoint: `/removeDrone/{name}`
- result content: String: ``Drone {name} removed``
- example:
- ``curl -X DELETE http://localhost:8080/removeDrone/name``
- error: There will be an error if:
    - no drone with that name was found

### getting information about drone
You can get all the data of a drone by its name.
- endpoint: `/droneInfo/{name}`
- result content: json / Drone
- example:
- ``curl -X DELETE http://localhost:8080/droneInfo/name``
- error: There will be an error if:
    - no drone with that name was found

## About the application
This application is build with [Maven](https://maven.apache.org/) and uses its build and test features.

It is build on Java 17 and uses a [Tomcat server](https://tomcat.apache.org/) to provide the web functionality. 

### Building and starting
To build the application enter the root directory and execute:
````bash
mvn package
````
If you have other projects using different repositories make sure to have a clean maven-folder and setting or create a new one and use the following parameters: ``-Dmaven.repo.local=~/.m2-2/ -s ~/.m2-2/settings.xml``

After that the application ``drone-management-system-0.0.1-SNAPSHOT.jar`` is sitting in the generated folder ``target`` and can be executed with:
````bash
java -jar drone-management-system-0.0.1-SNAPSHOT.jar
````

### Testing

All basic tests can be found in the folder ``src/tests`` and can be executed by running 
````bash
mvn test
````
in the main directory.