package coding.challange.dronemanagementsystem.api;

import coding.challange.dronemanagementsystem.domain.Direction;
import coding.challange.dronemanagementsystem.domain.Drone;
import coding.challange.dronemanagementsystem.service.FieldService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldServiceTest {

    @Test
    public void testAddDrone() throws Exception {
        FieldService fieldService = new FieldService(10, 10);
        Drone drone1 = new Drone("test1", Direction.North, 5, 5, 0);

        fieldService.addDrone(drone1);
        assertEquals(fieldService.getDrone("test1"), drone1);
        try {
            fieldService.addDrone(drone1);
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("already registered"));
        }
        try {
            fieldService.getDrone("test2");
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("not found"));
        }
        try {
            fieldService.addDrone(new Drone("test3", Direction.North, 12, 12, 0));
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Drone outside field boundaries"));
        }
    }

    @Test
    public void removeDrone() throws Exception {
        FieldService fieldService = new FieldService(10, 10);
        Drone drone1 = new Drone("test1", Direction.North, 5, 5, 0);
        fieldService.addDrone(drone1);
        fieldService.removeDrone("test1");
        try {
            fieldService.getDrone("test1");
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("not found"));
        }
        try {
            fieldService.removeDrone("test1");
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("not found"));
        }
    }

    @Test
    public void testMoveDrone() throws Exception {
        FieldService fieldService = new FieldService(10, 10);
        fieldService.addDrone(new Drone("test", Direction.North, 5, 5, 0));
        fieldService.moveDrone(new Drone("test", Direction.South, 7, 2, 0), true);
        Drone drone = fieldService.getDrone("test");
        assertEquals(7, drone.getPosX());
        assertEquals(2, drone.getPosY());
        assertEquals(Direction.South, drone.getDirection());

        fieldService.moveDrone(new Drone("test", null, 4, 4, 0), true);
        drone = fieldService.getDrone("test");
        assertEquals(4, drone.getPosX());
        assertEquals(4, drone.getPosY());
        assertEquals(Direction.North, drone.getDirection());
    }

    @Test
    public void testMoveDroneOutside() throws Exception {
        FieldService fieldService = new FieldService(10, 10);
        fieldService.addDrone(new Drone("test", Direction.North, 5, 5, 0));
        try {
            fieldService.moveDrone(new Drone("test", Direction.South, 11, 2, 0), true);
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Drone outside field boundaries"));
        }

    }

    @Test
    public void testMoveDroneObstacle() throws Exception {
        FieldService fieldService = new FieldService(10, 10);
        fieldService.addDrone(new Drone("test", Direction.North, 5, 5, 0));
        fieldService.addDrone(new Drone("test1", Direction.North, 5, 6, 0));
        fieldService.moveDrone(new Drone("test", Direction.South, 5, 7, 0), true);
        Drone drone = fieldService.getDrone("test");
        assertEquals(drone.getPosX(), 5);
        assertEquals(drone.getPosY(), 5);
        assertEquals(drone.getDirection(), Direction.South);
    }

    @Test
    public void testMoveDroneObstacle2() throws Exception {
        FieldService fieldService = new FieldService(10, 10);
        fieldService.addDrone(new Drone("test", Direction.North, 5, 5, 10));
        fieldService.addDrone(new Drone("test1", Direction.West, 5, 6, 10));
        fieldService.moveDrone(new Drone("test", Direction.South, 5, 7, 0));
        fieldService.moveDrone(new Drone("test1", Direction.South, 4, 6, 0));
        Thread.sleep(2000);
        Drone drone = fieldService.getDrone("test");
        assertEquals(drone.getPosX(), 5);
        assertEquals(drone.getPosY(), 7);
        assertEquals(drone.getDirection(), Direction.South);
    }

}