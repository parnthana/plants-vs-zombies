package logic;

import entity.zombie.BucketHeadZombie;
import entity.zombie.DefaultZombie;
import entity.zombie.FunnelHeadZombie;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collections;

public class GameEntity {
    private int level;
    private int numDefaultZombie;
    private int numFunnelHeadZombie;
    private int numBucketHeadZombie;
    private ArrayList<Integer> availableZombies;
    private ArrayList<Integer> zombieList1;
    private ArrayList<Integer> zombieList2;
    private int totalZombies;

    public GameEntity(int level) {
        this.level = level;
        this.zombieList1 = new ArrayList<>();
        this.zombieList2 = new ArrayList<>();
        this.availableZombies = new ArrayList<>();
        if (level >= 5) {
            this.totalZombies = 30;
            this.numDefaultZombie = 12;
            this.numFunnelHeadZombie = 10;
            this.numBucketHeadZombie = 8;
        } else if (level >= 4) {
            this.totalZombies = 25;
            this.numDefaultZombie = 12;
            this.numFunnelHeadZombie = 9;
            this.numBucketHeadZombie = 4;
        } else if (level >= 3) {
            this.totalZombies = 20;
            this.numDefaultZombie = 10;
            this.numFunnelHeadZombie = 8;
            this.numBucketHeadZombie = 2;
        } else if (level >= 2) {
            this.totalZombies = 15;
            this.numDefaultZombie = 10;
            this.numFunnelHeadZombie = 5;
            this.numBucketHeadZombie = 0;
        } else {
            this.totalZombies = 10;
            this.numDefaultZombie = 10;
            this.numFunnelHeadZombie = 0;
            this.numBucketHeadZombie = 0;
        }
        for (int idx = 0; idx < this.numDefaultZombie; idx++) {
            this.availableZombies.add(1);
        }
        for (int idx = 0; idx < this.numFunnelHeadZombie; idx++) {
            this.availableZombies.add(2);
        }
        for (int idx = 0; idx < this.numBucketHeadZombie; idx++) {
            this.availableZombies.add(3);
        }
        Collections.shuffle(availableZombies);
        for (int idx = 0; idx < availableZombies.size(); idx++) {
            if (idx % 2 == 1) {
                this.zombieList1.add(availableZombies.get(idx));
            } else {
                this.zombieList2.add(availableZombies.get(idx));
            }
        }
    }

    public static void spawnDefaultZombie(Pane pane, int lane, int laneNumber) {
        DefaultZombie Dzombie = new DefaultZombie(1024, lane, laneNumber);
        Dzombie.buildImage(pane);
        GameController.allZombies.add(Dzombie);
        Dzombie.moveZombie();
    }

    public static void spawnFunnelHeadZombie(Pane pane, int lane, int laneNumber) {
        FunnelHeadZombie Fzombie = new FunnelHeadZombie(1024, lane, laneNumber);
        Fzombie.buildImage(pane);
        GameController.allZombies.add(Fzombie);
        Fzombie.moveZombie();
    }

    public static void spawnBucketHeadZombie(Pane pane, int lane, int laneNumber) {
        BucketHeadZombie Bzombie = new BucketHeadZombie(1024, lane, laneNumber);
        Bzombie.buildImage(pane);
        GameController.allZombies.add(Bzombie);
        Bzombie.moveZombie();
    }

    public int getLevel() {
        return level;
    }

    public int getNumDefaultZombie() {
        return numDefaultZombie;
    }

    public int getNumFunnelHeadZombie() {
        return numFunnelHeadZombie;
    }

    public int getNumBucketHeadZombie() {
        return numBucketHeadZombie;
    }

    public ArrayList<Integer> getZombieList1() {
        return zombieList1;
    }

    public ArrayList<Integer> getZombieList2() {
        return zombieList2;
    }

    public int getTotalZombies() {
        return totalZombies;
    }

    public ArrayList<Integer> getAvailableZombies() {
        return availableZombies;
    }
}
