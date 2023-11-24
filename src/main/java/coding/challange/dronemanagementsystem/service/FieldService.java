package coding.challange.dronemanagementsystem.service;

import coding.challange.dronemanagementsystem.domain.Direction;
import coding.challange.dronemanagementsystem.domain.Drone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class FieldService {
    private final int sizeX;
    private final int sizeY;
    private final HashMap<String, Drone> drones;

    private int timeoutTries;

    Logger logger = LoggerFactory.getLogger(FieldService.class);

    public FieldService(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        drones = new HashMap<>();
        timeoutTries = sizeX * 2;
    }

    private boolean isInsideFieldBoundaries(int x, int y) {
        return x >= 0 && x < sizeX && y >= 0 && y < sizeY;
    }

    public void addDrone(Drone drone) throws RuntimeException {
        if (drone.getName() == null || drone.getDirection() == null)
            throw new IllegalArgumentException("Drone needs to have at least a name and direction");
        if (drones.containsKey(drone.getName()))
            throw new IllegalArgumentException("Drone with this name (" + drone.getName() + ") already registered");
        if (!isInsideFieldBoundaries(drone.getPosX(), drone.getPosY()))
            throw new IllegalArgumentException("Drone outside field boundaries");
        if (drones.values().stream().anyMatch(d -> d.getPosX()== drone.getPosX() && d.getPosY()== drone.getPosY()))
            throw new RuntimeException("There is already a drone in that area.");
        drones.put(drone.getName(), drone);
    }
    public void removeDrone(String name) throws Exception {
        if (!drones.containsKey(name))
            throw new Exception("Drone with this name (" + name + ") not found");
        drones.remove(name);
    }

    public void moveDrone(Drone targetDrone) throws Exception {
        moveDrone(targetDrone, false);
    }

    public void moveDrone(Drone targetDrone, boolean wait) throws Exception {
        if (targetDrone.getName() == null)
            throw new IllegalArgumentException("Drone needs to have at least a name");
        if (!drones.containsKey(targetDrone.getName()))
            throw new Exception("Drone with this name (" + targetDrone.getName() + ") not found");
        if (!isInsideFieldBoundaries(targetDrone.getPosX(), targetDrone.getPosY()))
            throw new IllegalArgumentException("Drone outside field boundaries");

        Drone drone = getDrone(targetDrone.getName());
        if (targetDrone.getSpeed()!=0)
            drone.setSpeed(targetDrone.getSpeed());
        logger.info("Speed: " + drone.getSpeed());
        if (wait) {
            moveDroneInSteps(drone, targetDrone);
        } else {
            new Thread(() -> {
                try {
                    moveDroneInSteps(drone, targetDrone);
                } catch (InterruptedException e) {
                    logger.warn("Drone moving was interrupted");
                }
            }).start();
        }
    }

    private void moveDroneInSteps(Drone drone, Drone targetDrone) throws InterruptedException {
        int dx = targetDrone.getPosX()- drone.getPosX()  < 0 ? -1 : 1;
        int dy = targetDrone.getPosY() - drone.getPosY()  < 0 ? -1 : 1;

        // Rotate and move to correct X position
        Direction targetDirection = dx == 1 ? Direction.East : Direction.West;
        while (drone.getDirection() != targetDirection)
            rotateDrone(drone);
        int tries = 0;
        while (drone.getPosX() != targetDrone.getPosX() && tries++ < timeoutTries)
            tryMoving(drone, drone.getPosX() + dx, drone.getPosY());
        if (tries>timeoutTries) logger.warn("drone did not reach destination");

        // Rotate and move to correct Y position
        targetDirection = dy == 1 ? Direction.North : Direction.South;
        while (drone.getDirection() != targetDirection)
            rotateDrone(drone);
        tries = 0;
        while (drone.getPosY() != targetDrone.getPosY() && tries++ < timeoutTries)
            tryMoving(drone, drone.getPosX(), drone.getPosY() + dy);
        if (tries>timeoutTries) logger.warn("drone did not reach destination");

        // If nessesary rotate to correct direction
        if (targetDrone.getDirection() != null)
            while (drone.getDirection() != targetDrone.getDirection())
                rotateDrone(drone);
    }

    private void tryMoving(Drone drone, int posX, int posY) throws InterruptedException {
        if (drones.values().stream().noneMatch(d -> d != drone && d.getPosX() == posX && d.getPosY() == posY)) {
            drone.setPosX(posX);
            drone.setPosY(posY);
        }
        Thread.sleep(drone.getSpeed());
        logger.info(drone.getName() + ": " + drone.getPosX() + ", " + drone.getPosY());
    }

    private void rotateDrone(Drone drone) throws InterruptedException {
        if (drone.getDirection() == Direction.North)
            drone.setDirection(Direction.West);
        else if (drone.getDirection() == Direction.West)
            drone.setDirection(Direction.South);
        else if (drone.getDirection() == Direction.South)
            drone.setDirection(Direction.East);
        else if (drone.getDirection() == Direction.East)
            drone.setDirection(Direction.North);
        Thread.sleep(drone.getSpeed());
        logger.info(drone.getName() + ": " + drone.getDirection());
    }

    public Drone getDrone(String name) throws Exception {
        if (!drones.containsKey(name))
            throw new Exception("Drone with this name (" + name + ") not found");
        return drones.get(name);
    }
}
