package unsw.gloriaromanus;

import java.io.IOException;

import org.json.JSONObject;

public class UnitFactory {
    private String configString;
    private Boolean isTraining;
    private Unit training;

    public UnitFactory(String config) {
        configString = config;
        isTraining = false;
        training = null;
    }

    public void addToTraining(String unitType, int numTroops) throws IOException {
        isTraining = true;
        training = newUnit(unitType, numTroops);
    }

    public Unit newUnit(String unitType, int numTroops) throws IOException {

        JSONObject unitStats = getUnitStats(unitType);
        
        Unit u = new Unit();
        u.setID();
        u.setNumTroops(numTroops);
        u.setMeleeAttack(unitStats.getInt("meleeAttack"));
        u.setRangedAttack(unitStats.optInt("rangedAttack"));
        u.setDefenseSkill(unitStats.getInt("defense"));
        u.setArmour(unitStats.optInt("armour"));
        u.setShieldDefense(unitStats.optInt("shield"));
        u.setMorale(unitStats.getInt("morale"));
        u.setSpeed(unitStats.getInt("speed"));
        u.setRange(unitStats.getString("range"));
        u.setType(unitStats.getString("type"));
        u.setAbility(unitStats.getString("ability"));
        u.setTurnsToProduce(unitStats.optInt("turnsToProduce"));

        return u;
    }

    public JSONObject getUnitStats(String unitType) throws IOException {
        // String configString = Files.readString(Paths.get("src/unsw/gloriaromanus/unit_config.json"));
        JSONObject config = new JSONObject(configString);
        return config.getJSONObject(unitType);
    }

    public Unit nextTrainingTurn() {
        if (isTraining && training.getTurnsToProduce() > 0) {
            training.minusTurnsToProduce(1);
            if (training.getTurnsToProduce() == 0) {
                Unit u = training;
                removeTraining();
                return u;
            }
        }
        return null;
    }

	public void removeTraining() {
        isTraining = false;
        training = null;
    }
    
    public int getCost(String unitType, int numTroops) throws IOException {
        JSONObject unitStats = getUnitStats(unitType);
        return unitStats.getInt("cost") * numTroops;
    }

    public Boolean getIsTraining() {
        return isTraining;
    }

    public Unit getTraining() {
        return training;
    }

    public String getConfigString() {
        return configString;
    }

    public void setConfigString(String configString) {
        this.configString = configString;
    }
}
