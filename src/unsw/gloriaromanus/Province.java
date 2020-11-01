package unsw.gloriaromanus;

import java.util.ArrayList;

public class Province {
    private static final int MAX_FAC = 2;

    private String name;
    private Player player;
    private ArrayList<Unit> units;
    private int initialArmySize;
    private int wealth;
    private UnitFactory[] factories;

    public Province() {
        //super();
    }

    public Province(String name, Player player, String unitConfig) {
        generateFactories(unitConfig);
        this.name = name;
        this.player = player;
        this.units = new ArrayList<Unit>();
        this.initialArmySize = 0;
        this.wealth = 0;
    }

    public void moveUnit(Province p, int id) {
        for (Unit u : units) {
            if (u.getID() == id) {
                p.insertUnit(u);
                units.remove(u);
            }
        }
    }

    public void insertUnit(Unit u) {
        units.add(u);
    }

    public void nextTurn() {
        for (UnitFactory fac : factories) {
            Unit u = fac.nextTrainingTurn();
            if (u != null) {
                insertUnit(u);
            }
        }
    }

    public boolean trainUnit(String unitType, int numTroops) {
        for (UnitFactory fac : factories) {
            int price = fac.getPrice(unitType, numTroops);
            if (!fac.isTraining && player.getGold() >= price) {
                fac.addToTraining(unitType, numTroops);
                player.minusGold(price);
                return true;
            }
        }
        return false;
    }

    public int getArmyStrength() {
        // the sum of number of soldiers in unit x attack x defense for all units in the army
        // We initially assume that a unit that has been initially recruited on the province has 1 attack and 1 defense
        int totalAttack = initialArmySize;
        int totalDefense = initialArmySize;
        if (units != null) {
            for (Unit u : units) {
                totalAttack += u.getTotalAttack();
                totalDefense += u.getTotalDefense();
            }
        }
        return (this.getArmySize() * totalAttack * totalDefense);
    }

    private void generateFactories(String unitConfig) {
        factories = new UnitFactory[MAX_FAC];
        for (int i = 0; i < MAX_FAC; i++) {
            factories[i] = new UnitFactory(unitConfig);
        }
    }

    public String getName() {
        return name;
    }

    public int getArmySize() {
        return initialArmySize + getUnitsTroopSize();
    }

    public int getUnitsTroopSize() {
        int size = 0;
        if (units != null) {
            for (Unit u : units) {
                size += u.getNumTroops();
            }
        }
        return size;
    }

    public void setArmySize(int size) {
        // We assume initial armies are killed off first for the basic battle resolver
        this.initialArmySize = size - getUnitsTroopSize();
    }

    public int getWealth() {
        return wealth;
    }

    public String getFaction() {
        return player.getFaction();
    }

    public void setFaction(String faction) {
        if(player != null) { player.setFaction(faction); }
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    /*public JSONObject getJSONObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(this);
        
        JSONObject j = new JSONObject(jsonString);
        j.put("name", name);
        j.put("faction", faction);
        JSONArray ja = new JSONArray();
        for (Unit u : units) {
            JSONObject jo = u.getJSONObject();
            ja.put(jo);
        }
        j.put("units", ja);
        j.put("armySize", armySize);
        j.put("wealth", wealth);
        j.put("factories", new JSONArray(factories));
        return j;
    }*/
}