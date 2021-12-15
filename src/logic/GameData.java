package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import entity.Plant;
import entity.Zombie;

public class GameData {
    private  List<Zombie> allZombie;
    private  List<Plant> allPlants;
    private  ArrayList<Integer> zombieList1;
    private  ArrayList<Integer> zombieList2;
    private  boolean status;
    private double timeElapsed;
    private int Level;
    private int sunCount;

    public GameData(int level) {
        this.sunCount = 50;
        this.zombieList1 = new ArrayList<>();
        this.zombieList2 = new ArrayList<>();
        this.allZombie = Collections.synchronizedList(new ArrayList<>());
        this.allPlants = Collections.synchronizedList(new ArrayList<>());
        GameEntity gamelevel = new GameEntity(level);
        zombieList1 = gamelevel.getZombieList1();
        zombieList2 = gamelevel.getZombieList2();
        status = LevelMenuController.status;
    }

    public void updateData(int level, int sunCount, List<Plant> allPlants, List<Zombie> allZombie, double timeElapsed, ArrayList<Integer> zombieList1, ArrayList<Integer> zombieList2, boolean status) {
        this.timeElapsed = timeElapsed;
        this.sunCount = sunCount;
        this.Level = level;
        this.status = status;
        this.allPlants = allPlants;
        this.allZombie = allZombie;
        this.zombieList1 = zombieList1;
        this.zombieList2 = zombieList2;
    }

    public String LevelToString() {
        return ("Level: " + Level);
    }

    public List<Zombie> getAllZombie() {
        return allZombie;
    }

    public List<Plant> getAllPlants() {
        return allPlants;
    }

    public ArrayList<Integer> getZombieList1() {
        return zombieList1;
    }

    public ArrayList<Integer> getZombieList2() {
        return zombieList2;
    }

    public boolean getStatus() {
        return status;
    }

    public double getTimeElapsed() {
        return timeElapsed;
    }

    public int getLevel() {
        return Level;
    }

    public int getSunCount() {
        return sunCount;
    }
}
