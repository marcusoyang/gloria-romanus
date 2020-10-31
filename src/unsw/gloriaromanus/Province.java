package unsw.gloriaromanus;

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Province {
    private static final int MAX_FAC = 2;

    private String name;
    private String faction;
    private ArrayList<Unit> units;
    // the below armySize variable will be deleted when units list is implemented
    private int armySize;
    private int wealth;
    private UnitFactory[] factories;

    public Province(String name, String faction, String unitConfig) {
        generateFactories(unitConfig);
        this.name = name;
        this.faction = faction;
        this.units = new ArrayList<Unit>();
        this.armySize = 0;
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
            if (!fac.isTraining) {
                fac.addToTraining(unitType, numTroops);
                return true;
            }
        }
        return false;
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
        return armySize;
    }

    public void setArmySize(int size) {
        this.armySize = size;
    }

    public int getWealth() {
        return wealth;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public JSONObject getJSONObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(this);
        
        JSONObject j = new JSONObject(jsonString);
        /*j.put("name", name);
        j.put("faction", faction);
        JSONArray ja = new JSONArray();
        for (Unit u : units) {
            JSONObject jo = u.getJSONObject();
            ja.put(jo);
        }
        j.put("units", ja);
        j.put("armySize", armySize);
        j.put("wealth", wealth);
        j.put("factories", new JSONArray(factories));*/
        return j;
    }

    public void setDetails(JSONObject joProvince) {
        JSONArray ja = joProvince.getJSONArray("units");
        for (int i = 0; i < ja.length(); i++) {
            JSONObject joUnit = ja.getJSONObject(i);
        }
    }
}