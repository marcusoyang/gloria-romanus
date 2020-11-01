package unsw.gloriaromanus;

public class Player {
    private int id;
    private String faction;
    private int gold;

    public Player() {
        // super();
    }

    public Player(int id, String faction) {
        this.id = id;
        this.faction = faction;
        this.gold = 100000;
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

    public int getID() {
        return id;
    }
}