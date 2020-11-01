package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public class UnitFactory {
    Boolean isTraining;
    private Unit training;

    public UnitFactory() {
        isTraining = false;
        training = null;
    }

    public void addToTraining(String unitType, int numTroops) throws IOException {
        isTraining = true;
        training = newUnit(unitType, numTroops);
    }

    public Unit newUnit(String unitType, int numTroops) throws IOException {

        JSONObject unitStats = showUnitStats(unitType);

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

    public JSONObject showUnitStats(String unitType) throws IOException {
        String configString = Files.readString(Paths.get("src/unsw/gloriaromanus/unit_config.json"));
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
        JSONObject unitStats = showUnitStats(unitType);
        return unitStats.getInt("cost") * numTroops;
    }

    public Boolean getIsTraining() {
        return isTraining;
    }

    public Unit getTraining() {
        return training;
    }
}
