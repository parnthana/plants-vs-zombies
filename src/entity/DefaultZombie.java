package entity;

public class DefaultZombie extends Zombie {
    public DefaultZombie(int x, int y, int lane) {
        super(5, 2, x, y, 68, 118, lane, "/res/defaultzombie.gif");
        this.path = "/res/defaultzombie.gif";
    }
}

