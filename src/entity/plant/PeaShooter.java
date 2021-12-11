package entity.plant;

public class PeaShooter extends Shooter {

    // Constructor
    public PeaShooter(int x, int y, int row, int column) {
        super(x, y, "/assets/gif/peashooter.gif", 100, 60, 62, row, column);
        this.path = getClass().getResource("/assets/gif/peashooter.gif").toString();
    }

}

