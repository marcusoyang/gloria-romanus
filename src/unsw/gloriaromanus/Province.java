package unsw.gloriaromanus;

import java.util.ArrayList;

public class Province {
    private String name;
    private Faction faction;
    private ArrayList<Unit> units;

    public Province(String name, Faction faction) {
        this.name = name;
        this.faction = faction;
        units = new ArrayList<Unit>();
    }

    public float getArmyStrength() {
        // the sum of number of soldiers in unit x attack x defense for all units in the army
        int noOfSoldiers = units.size();
        int totalAttack = 0;
        int totalDefense = 0;
        for (Unit u : units) {
            totalAttack += u.getAttack();
            totalDefense += u.getDefense();
        }
        
        return (noOfSoldiers * totalAttack * totalDefense);
    }

    public String getName() {
        return name;
    }

    public int getArmySize() {
        return units.size();
    }
}