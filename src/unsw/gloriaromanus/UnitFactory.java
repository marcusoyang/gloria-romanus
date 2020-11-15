package unsw.gloriaromanus;

import java.io.IOException;

import org.json.JSONObject;

public class UnitFactory {
    private Player player;
    private String configString;
    private Boolean isTraining;
    private Unit training;

    public UnitFactory() {
        // super();
    }

    public UnitFactory(String config, Player p) {
        player = p;
        configString = config;
        isTraining = false;
        training = null;
    }

    public void addToTraining(String unitType, int numTroops) throws IOException {
        isTraining = true;
        training = newUnit(unitType, numTroops);
    }

    public Unit newUnit(String unitType, int numTroops) {

        JSONObject unitStats = generateUnitStats(unitType);
        
        Unit u = new Unit();
        u.setHasInvaded(false);
        u.setID();
        u.setUnitType(unitType);
        u.setNumTroops(numTroops);
        u.setMeleeAttack(unitStats.getInt("meleeAttack"));
        u.setRangedAttack(unitStats.optInt("rangedAttack"));
        u.setDefenseSkill(unitStats.getInt("defense"));
        u.setArmour(unitStats.optInt("armour"));
        u.setShieldDefense(unitStats.optInt("shield"));
        u.setMorale(unitStats.getDouble("morale"));
        u.setSpeed(unitStats.getDouble("speed"));
        u.setRange(unitStats.getString("range"));
        u.setType(unitStats.getString("type"));
        u.setAbility(unitStats.getString("ability"));
        u.resetMovementPoint();
        u.setTurnsToProduce(unitStats.optInt("turnsToProduce"));
        u.setPlayer(player);

        return u;
    }

    public JSONObject generateUnitStats(String unitType) {
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
        JSONObject unitStats = generateUnitStats(unitType);
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

    public int restoreArmour(String unitType) {
        JSONObject unitStats = generateUnitStats(unitType);
        return unitStats.getInt("armour");
    }

    public int restoreShieldDefense(String unitType) {
        JSONObject unitStats = generateUnitStats(unitType);
        return unitStats.getInt("shield");
    }
}
