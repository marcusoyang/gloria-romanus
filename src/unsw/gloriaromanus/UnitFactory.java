package unsw.gloriaromanus;

import org.json.JSONObject;

public class UnitFactory {
    JSONObject config;

    public UnitFactory(String configString) {
        config = new JSONObject(configString);
    }

    public Unit newUnit(String unitType, int numTroops) {

        JSONObject unitStats = config.getJSONObject(unitType);

        Unit u = new Unit();
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
}
