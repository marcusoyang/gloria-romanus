package unsw.gloriaromanus;

public class Player {
    private static int IDCounter = 0;
    private int id;
    private String faction;
    private int gold;

    public Player(String faction) {
        this.id = IDCounter;
        this.faction = faction;
        this.gold = 100000;
        IDCounter++;
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

    public void minusGold(int cost) {
        this.gold -= cost;
    }
}