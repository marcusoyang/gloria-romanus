package unsw.gloriaromanus;

import java.util.ArrayList;

public class Province {
    private String name;
    private String faction;
    private ArrayList<Unit> units;
    // the below armySize variable will be deleted when units list is implemented
    private int armySize;

    public Province(String name, String faction) {
        this.name = name;
        this.faction = faction;
        this.units = new ArrayList<Unit>();
        this.armySize = 0;
    }

    public int getArmyStrength() {
        // the sum of number of soldiers in unit x attack x defense for all units in the army

        // the below code will be used when units list is implemented
        /*int totalAttack = 0;
        int totalDefense = 0;
        for (Unit u : units) {
            totalAttack += u.getAttack();
            totalDefense += u.getDefense();
        }*/
        
        // We initially assume that a unit has 1 attack and 1 defense
        return (armySize * armySize * armySize);
    }

    public String getName() {
        return name;
    }

    public int getArmySize() {
        return armySize;
    }

    public void setArmySize(int size) {
        this.armySize = size;
    }
}