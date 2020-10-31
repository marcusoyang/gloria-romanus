package unsw.gloriaromanus;

import org.json.JSONObject;

public class UnitFactory {
    JSONObject config;
    Boolean isTraining;
    Unit training;

    public UnitFactory(String configString) {
        config = new JSONObject(configString);
    }

    public void addToTraining(String unitType, int numTroops) {
        isTraining = true;
        training = newUnit(unitType, numTroops);
    }

    public Unit newUnit(String unitType, int numTroops) {

        JSONObject unitStats = config.getJSONObject(unitType);

        Unit u = new Unit();
        u.setID();
        u.setNumTroops(numTroops);
        u.setMeleeAttack(unitStats.getInt("meleeAttack"));
        u.setRangedAttack(unitStats.optInt("rangedAttack"));
        u.setDefenseSkill(unitStats.getInt("defense"));
        u.setArmour(unitStats.getInt("armour"));
        u.setShieldDefense(unitStats.getInt("shield"));
        u.setMorale(unitStats.getInt("morale"));
        u.setSpeed(unitStats.getInt("speed"));
        u.setRange(unitStats.getString("range"));
        u.setType(unitStats.getString("type"));
        u.setAbility(unitStats.getString("ability"));
        u.setTurnsToProduce(unitStats.getInt("turnsToProduce"));

        return u;
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
}
