package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.HashMap;

public class Ability {
    private static ArrayList<Province> provinces;
    
    public static void initiate(Province p) {
        ArrayList<Unit> units = p.getUnits();
        for (Unit u : units) {
            processAbility(u);
        }
    }

	public static void restore(Province p) {
	}

    private static void processAbility(Unit u) {
        switch (u.getAbility()) {
            case "Legionary Eagle": processLegionaryEagle(u);
            case "Berserker Rage": processBerserkerRage(u);
            case "Phalanx": processPhalanx(u);
            case "Skirmisher Anti-Armour":
            case "Elephant Amok":
            case "Cantabrian Circle":
            case "Druidic Fervour":
        }
    }

	private static void processPhalanx(Unit u) {
        u.setDefenseSkill(u.getDefenseSkill() * 2);   // double defense
        u.setSpeed(u.getSpeed() / 2);                 // half speed
    }

    public static void processHeroicCharge(Province humanProvince, Province enemyProvince) {
        if (humanProvince.getArmySize() < (enemyProvince.getArmySize() / 2)) {
            ArrayList<Unit> units = humanProvince.getUnits();
            for (Unit u : units) {
                if (u.getRange() == "melee" && u.getType() == "cavalry") {
                    u.setMeleeAttack(u.getMeleeAttack() * 2);  // double melee attack
                    u.addMorale(u.getMorale() / 2);            // 50% higher morale
                }
            }
        }
	}

    private static void processBerserkerRage(Unit u) {
        u.addMorale(99999);
        u.setMeleeAttack(u.getMeleeAttack() * 2);
        u.setArmour(0);
        u.setShieldDefense(0);
    }

    private static void processLegionaryEagle(Unit u) {
        for (Province p : provinces) {
            ArrayList<Unit> units = p.getUnits();
            if (units.contains(u)) {
                for (Unit u2 : units) {
                    u2.addMorale(1);
                }
            }
        }
    }

    /**
     * Processes legionary eagle penalty after each skirmish
     * @param unit
     * @param initialTroops
     * @param humanProvince
     */
    public static void processLegionaryEagleDeath(Unit u, int initialTroops, Province p) {
        if (u.getAbility() == "Legionary Eagle") {
            double casualties = initialTroops - u.getNumTroops();
            double penalty = casualties * 0.2;
            sufferMoralePenalty(u.getPlayer(), penalty);
            u.getPlayer().addToLEPenaltyMap(p.getName(), penalty);
        }
    }

    public static void sufferMoralePenalty(Player player, double penalty) {
        for (Province p : provinces) {
            if (p.getPlayer().equals(player)) {
                ArrayList<Unit> units = p.getUnits();
                for (Unit u2 : units) {
                    u2.minusMorale(penalty);
                }
                player.increaseMoralePenalty(penalty);
            }
        }
    }

	public static void checkLERecapture(Unit u, Province p) {
        if (u.getAbility() == "Legionary Eagle") {
            Player player = u.getPlayer();
            if (player.mapContainsProvince(p)) {
                player.mapRemoveProvince(p);
            }
        }
	}

    public static void setProvinces(ArrayList<Province> provinces) {
        Ability.provinces = provinces;
    }
}
