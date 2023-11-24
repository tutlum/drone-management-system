package coding.challange.dronemanagementsystem.api;

import coding.challange.dronemanagementsystem.domain.Drone;
import coding.challange.dronemanagementsystem.service.FieldService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class DroneManagementController {

    private final FieldService field = new FieldService(10, 10);

    @PostMapping("/registerDrone")
    public Drone registerDrone(@RequestBody Drone drone) throws ResponseStatusException {
        try {
            if (drone.getSpeed()==0)
                drone.setSpeed(200);
            field.addDrone(drone);
            return field.getDrone(drone.getName());
        } catch (RuntimeException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage(), exc);
        }
    }

    @GetMapping("/droneInfo/{name}")
    public Drone getDroneInfo(@PathVariable String name) throws ResponseStatusException {
        try {
            return field.getDrone(name);
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage(), exc);
        }
    }
    @DeleteMapping("/removeDrone/{name}")
    public String removeDrone(@PathVariable String name) throws ResponseStatusException {
        try {
            field.removeDrone(name);
            return "Drone " + name + " removed";
        } catch (Exception exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exc.getMessage(), exc);
        }
    }

    @PostMapping("/moveDrone")
    public String moveDrone(@RequestBody Drone drone) throws ResponseStatusException {
        try {
            field.moveDrone(drone);
            return "Started moving";
        } catch (IllegalArgumentException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage(), exc);
        }

    }
    @PostMapping("/moveDroneWait")
    public Drone moveDroneWait(@RequestBody Drone drone) throws ResponseStatusException {
        try {
            field.moveDrone(drone, true);
            return field.getDrone(drone.getName());
        } catch (IllegalArgumentException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage(), exc);
        }
    }
}
