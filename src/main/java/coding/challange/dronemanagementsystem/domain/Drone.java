package coding.challange.dronemanagementsystem.domain;

public class Drone {
    private String name;
    private Direction direction;
    private int posX;
    private int posY;

    // speed in time for moving one unit distance
    private int speed = 500;

    public Drone(String name, Direction direction, int posX, int posY, int speed) {
        init(name, direction, posX, posY, speed);
    }

    private void init(String name, Direction direction, int posX, int posY, int speed) {
        this.name = name;
        this.direction = direction;
        this.posX = posX;
        this.posY = posY;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
