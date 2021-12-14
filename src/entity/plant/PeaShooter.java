package entity.plant;

public class PeaShooter extends Shooter {

    // Constructor
    public PeaShooter(int x, int y, int row, int column) {
        super(x, y, "/gif/peashooter.gif", 100, 60, 62, row, column);
        this.path = getClass().getResource("/gif/peashooter.gif").toString();
    }

}

