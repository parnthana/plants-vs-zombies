package entity;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Iterator;

public class CherryBomb extends Plant {
    protected ImageView cherry;
    private ArrayList<Zombie> roastedZombies;

    public CherryBomb(int x, int y, int row, int col) {
        super(x, y, 68, 90, "/assets/anim_cherrybomb.gif", 4, row, col);
        this.path = "/assets/anim_cherrybomb.gif";
    }

    @Override
    public void attacking() {
    }

    @Override
    public void buildImage(GridPane lawn) {
        super.buildImage(lawn);
        Image img = new Image("/assets/powie.gif");
        cherry = new ImageView(img);
        cherry.setFitHeight(180);
        cherry.setFitWidth(160);
        cherry.setX(x - 40);
        cherry.setY(y - 20);
        cherry.setVisible(false);
        cherry.setDisable(true);
        roastedZombies = new ArrayList<Zombie>();
    }

    @Override
    public void attack(Pane pane) {
        pane.getChildren().add(cherry);
        new Thread(() -> {
            try {
                Thread.sleep(1700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Media blast = new Media(getClass().getResource("/assets/sounds/cherrybomb.wav").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(blast);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
            image.setVisible(false);
            image.setDisable(true);
            cherry.setVisible(true);
            synchronized (GamePlayController.allZombies) {
                Iterator<Zombie> itr = GamePlayController.allZombies.iterator();
                while (itr.hasNext()) {
                    Zombie x = itr.next();
                    if (x.getX() <= (getX() + 250) && x.getX() >= (getX() - 150)) {
                        if (x.getY() <= (getY() + 250) && x.getY() >= (getY() - 150)) {
                            roastedZombies.add(x);
                            x.burntZombie();
                        }
                    }
                }
            }
            for (Plant plant : GamePlayController.allPlants) {
                if (this == plant) {
                    GamePlayController.allPlants.remove(plant);
                    break;
                }
            }
            for (int roastzombie = 0; roastzombie < roastedZombies.size(); roastzombie++) {
                for (int allzombie = 0; allzombie < GamePlayController.allZombies.size(); allzombie++) {
                    if (this.roastedZombies.get(roastzombie) == GamePlayController.allZombies.get(allzombie)) {
                        GamePlayController.allZombies.remove(allzombie);
                    }
                }
            }
            removeCherry();
        }).start();
    }

    public void removeCherry() {
        new Thread(() -> {
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cherry.setVisible(false);
        }).start();
        this.setHealthpoint(0);
    }

    public ArrayList<Zombie> getRoastedZombies() {
        return roastedZombies;
    }
}
