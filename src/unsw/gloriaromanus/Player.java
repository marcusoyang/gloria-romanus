package unsw.gloriaromanus;

import java.util.HashMap;

public class Player {
    private int id;
    private String faction;
    private int gold;
    private double moralePenalty;
    private HashMap<String,Double> legionaryEaglePenaltyMap;

    public Player() {
        // super();
    }

    public Player(int id, String faction) {
        this.id = id;
        this.faction = faction;
        this.gold = 100;
        this.moralePenalty = 0;
        this.legionaryEaglePenaltyMap = new HashMap<String,Double>();
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void minusGold(int cost) {
        this.gold -= cost;
    }

    public void plusGold(int cost) {
        this.gold += cost;
    }

    public int getID() {
        return id;
    }

    public double getMoralePenalty() {
        return moralePenalty;
    }

    public void setMoralePenalty(double moralePenalty) {
        this.moralePenalty = moralePenalty;
    }

    public void increaseMoralePenalty(double moralePenalty) {
        this.moralePenalty += moralePenalty;
    }

    public void addToLEPenaltyMap(String province, double penalty) {
        this.legionaryEaglePenaltyMap.put(province, penalty);
    }

    public HashMap<String, Double> getLegionaryEaglePenaltyMap() {
        return legionaryEaglePenaltyMap;
    }

    public boolean mapContainsProvince(Province p) {
        if (this.legionaryEaglePenaltyMap.containsKey(p.getName())) {
            return true;
        }
        return false;
    }

    public void mapRemoveProvince(Province p) {
        this.legionaryEaglePenaltyMap.remove(p.getName());
    }
}