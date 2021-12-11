package entity;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Iterator;

public class ChilliPepper extends Plant {
    private ArrayList<Zombie> roastedZombies;
    private ImageView[] fires;

    public ChilliPepper(int x, int y, int row, int col) {
        super(x, y, 100, 100, "/res/jalapeno.gif", 4, row, col);
        this.path = "/assets/jalapeno.gif";
        fires = new ImageView[9];
    }

    @Override
    public void buildImage(GridPane lawn) {
        super.buildImage(lawn);
        for (int i = 0; i < 9; i++) {
            fires[i] = new ImageView(new Image(getClass().getResource("/res/jalapenoFire.gif").toString(), 100, 100, false, false));
            fires[i].setDisable(true);
            fires[i].setVisible(false);
            lawn.add(fires[i], i, this.row, 1, 1);
        }
        this.roastedZombies = new ArrayList<Zombie>();
    }

    @Override
    public void attack(Pane pane) {
        new Thread(() -> {
            try {
                Thread.sleep(1700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            image.setVisible(false);
            image.setDisable(true);
            Media blast = new Media(getClass().getResource("/res/sounds/jalapeno.wav").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(blast);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
            for (int i = 0; i < 9; i++) {
                fires[i].setVisible(true);
            }
            synchronized (GamePlayController.allZombies) {
                Iterator<Zombie> zombieIterator = GamePlayController.allZombies.iterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();
                    if (row == zombie.getLane()) {
                        zombie.burntZombie();
                    }
                }
            }
            for (Plant plant : GamePlayController.allPlants) {
                if (this == plant) {
                    GamePlayController.allPlants.remove(plant);
                }
            }
            for (int i = 0; i < this.roastedZombies.size(); i++) {
                for (int j = 0; j < GamePlayController.allZombies.size(); j++) {
                    if (roastedZombies.get(i) == GamePlayController.allZombies.get(j)) {
                        GamePlayController.allZombies.remove(j);
                    }
                }
            }
            removeFire();
        }).start();
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

    @Override
    public void attacking() {
        super.attacking();
    }
}
