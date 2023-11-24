package coding.challange.dronemanagementsystem.api;

import coding.challange.dronemanagementsystem.domain.Direction;
import coding.challange.dronemanagementsystem.domain.Drone;
import coding.challange.dronemanagementsystem.service.FieldService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldServiceTest {

    @Test
    public void testAddDrone(){
        FieldService fieldService = new FieldService(10, 10);
        Drone drone1 = new Drone("test1", Direction.North, 5,5,0);

        try {
            fieldService.addDrone(drone1);
            assertEquals(fieldService.getDrone("test1"), drone1);
        } catch (Exception e) {
            fail();
        }
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
            assertTrue(e.getMessage().contains("No drone with name"));
        }
    }

    @Test
    public void testMoveDrone() throws Exception {
        FieldService fieldService = new FieldService(10, 10);
        fieldService.addDrone(new Drone("test", Direction.North, 5,5,0));
        fieldService.moveDrone(new Drone("test", Direction.South, 7,2,0), true);
        Drone drone = fieldService.getDrone("test");
        assertTrue(drone.getPosX()==7);
        assertTrue(drone.getPosY()==2);
        assertTrue(drone.getDirection()==Direction.South);
    }
    @Test
    public void testMoveDroneObstacle() throws Exception {
        FieldService fieldService = new FieldService(10, 10);
        fieldService.addDrone(new Drone("test", Direction.North, 5,5,0));
        fieldService.addDrone(new Drone("test1", Direction.North, 5,6,0));
        fieldService.moveDrone(new Drone("test", Direction.South, 5,7,0), true);
        Drone drone = fieldService.getDrone("test");
        assertTrue(drone.getPosX()==5);
        assertTrue(drone.getPosY()==5);
        assertTrue(drone.getDirection()==Direction.South);
    }
    @Test
    public void testMoveDroneObstacle2() throws Exception {
        FieldService fieldService = new FieldService(10, 10);
        fieldService.addDrone(new Drone("test", Direction.North, 5,5,10));
        fieldService.addDrone(new Drone("test1", Direction.West, 5,6,10));
        fieldService.moveDrone(new Drone("test", Direction.South, 5,7,0));
        fieldService.moveDrone(new Drone("test1", Direction.South, 4,6,0));
        Thread.sleep(2000);
        Drone drone = fieldService.getDrone("test");
        assertTrue(drone.getPosX()==5);
        assertTrue(drone.getPosY()==7);
        assertTrue(drone.getDirection()==Direction.South);
    }

}