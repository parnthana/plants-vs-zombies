package entity.plant;

import entity.Plant;
import entity.Zombie;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import logic.GameController;

import java.util.ArrayList;

public class ChilliPepper extends Plant {

    // Fields
    private ArrayList<Zombie> roastedZombies;
    private ImageView[] fires;

    // Constructor
    public ChilliPepper(int x, int y, int row, int col) {
        super(x, y, 100, 100, "/assets/gif/jalapeno.gif", 4, row, col);
        fires = new ImageView[9];
    }

    // Methods
    @Override
    public void buildImage(GridPane lawn) {
        super.buildImage(lawn);
        for (int i = 0; i < 9; i++) {
            fires[i] = new ImageView(new Image(getClass().getResource("/assets/gif/jalapenoFire.gif").toString(), 100, 100, false, false));
            fires[i].setDisable(true);
            fires[i].setVisible(false);
            lawn.add(fires[i], i, this.row, 1, 1);
        }
        this.roastedZombies = new ArrayList<Zombie>();
    }

    @Override
    public void attacking(Pane pane) {
        new Thread(() -> {
            try {
                Thread.sleep(1700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            image.setVisible(false);
            image.setDisable(true);
            Media blast = new Media(getClass().getResource("/assets/sounds/jalapeno.wav").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(blast);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
            for (int i = 0; i < 9; i++) {
                fires[i].setVisible(true);
            }
            synchronized (GameController.allZombies) {
                for (Zombie zombie : GameController.allZombies) {
                    if (row == zombie.getLane()) {
                        zombie.burntZombie();
                    }
                }
            }
            GameController.allPlants.removeIf(plant -> this == plant);
            for (Zombie roastedZombie : this.roastedZombies) {
                for (int j = 0; j < GameController.allZombies.size(); j++) {
                    if (roastedZombie == GameController.allZombies.get(j)) {
                        GameController.allZombies.remove(j);
                    }
                }
            }
            removeFire();
        }).start();
    }

    @Override
    public void attacking() {
    }

    public void removeFire() {
        new Thread(() -> {
            try {
                Thread.sleep(1400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 9; i++) {
                fires[i].setVisible(false);
                fires[i].setDisable(true);
            }
        }).start();
        this.setHealthpoint(0);
    }

    public ArrayList<Zombie> getRoastedZombies() {
        return roastedZombies;
    }

}
