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
    public ChilliPepper(int x, int y, int col, int row) {
        super(x, y, 100, 100, "/gif/chillipepper.gif", 4, col, row);
        fires = new ImageView[9];
    }

    // Methods
    @Override
    public void attacking() {
    }

    @Override
    public void buildImage(GridPane lawn) {
        super.buildImage(lawn);
        for (int i = 0; i < 9; i++) {
            fires[i] = new ImageView(new Image(getClass().getResource("/gif/chillipepperFire.gif").toString(), 100, 100, false, false));
            fires[i].setDisable(true);
            fires[i].setVisible(false);
            lawn.add(fires[i], i,row, 1, 1);
        }
        roastedZombies = new ArrayList<>();
    }

    @Override
    public void attacking(Pane pane) {
        new Thread(() -> {
            try {
                Thread.sleep(1650);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            image.setVisible(false);
            image.setDisable(true);
            Media blast = new Media(getClass().getResource("/sounds/chillipepper.wav").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(blast);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
            for (int i = 0; i < 9; i++) {
                fires[i].setVisible(true);
            }
            synchronized (GameController.allZombies) {
                for (Object zombie : GameController.allZombies) {
                    if (row == ((Zombie) zombie).getLane()) {
                        ((Zombie) zombie).burntZombie();
                    }
                }
            }
            GameController.allPlants.removeIf(plant -> this == plant);
            for (Zombie roastedZombie : roastedZombies) {
                for (int j = 0; j < GameController.allZombies.size(); j++) {
                    if (roastedZombie == GameController.allZombies.get(j)) {
                        GameController.allZombies.remove(j);
                    }
                }
            }
            removeFire();
        }).start();
    }

    public void removeFire() {
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 9; i++) {
                fires[i].setVisible(false);
                fires[i].setDisable(true);
            }
        }).start();
        setHealthpoint(0);
    }

    public ArrayList<Zombie> getRoastedZombies() {
        return roastedZombies;
    }

}
