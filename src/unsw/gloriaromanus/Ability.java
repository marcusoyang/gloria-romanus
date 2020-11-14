package unsw.gloriaromanus;

import java.util.ArrayList;

public abstract class Ability {
    private static ArrayList<Province> provinces;
    private static ArrayList<Unit> units; 

    public static void initiate(ArrayList<Unit> units) {
        Ability.units = units;
        for (Unit u : units) {
            processAbility(u);
        }
    }

	public static void restore(ArrayList<Unit> units) {
        Ability.units = units;
        for (Unit u : units) {
            restoreAbility(u);
        }
	}

    public static void processAbility(Unit u) {
        switch (u.getAbility()) {
            case "Legionary Eagle": processLegionaryEagle(); break;
            case "Berserker Rage": processBerserkerRage(u); break;
            case "Phalanx": processPhalanx(u); break;
            case "Elephant Amok": 
            case "Cantabrian Circle":
            case "Druidic Fervour":
        }
    }

    public static void restoreAbility(Unit u) {
        switch (u.getAbility()) {
            case "Legionary Eagle": restoreLegionaryEagle(); break;
            case "Berserker Rage": restoreBerserkerRage(u); break;
            case "Phalanx": 
            case "Elephant Amok": 
            case "Cantabrian Circle":
            case "Druidic Fervour":
        }

        if (u.getRange().equals("melee") && u.getType().equals("cavalry")) {
            restoreHeroicCharge(u);
        }
    }

    private static void processLegionaryEagle() {
        for (Unit u : units) {
            u.addMorale(1);
        }
    }

    private static void restoreLegionaryEagle() {
        for (Unit u : units) {
            u.minusMorale(1);
        }
    }

    private static void processBerserkerRage(Unit u) {
        u.addMorale(9999);
        u.setMeleeAttack(u.getMeleeAttack() * 2);
        u.setArmour(0);
        u.setShieldDefense(0);
    }

    private static void restoreBerserkerRage(Unit u) {
        u.minusMorale(9999);
        u.setMeleeAttack(u.getMeleeAttack() / 2);
        restoreArmour(u);
        restoreShieldDefense(u);
    }

    private static void restoreArmour(Unit u) {
        UnitFactory uf = provinces.get(0).getFactories().get(0);
        u.setArmour(uf.restoreArmour(u.getUnitType()));
    }

    private static void restoreShieldDefense(Unit u) {
        UnitFactory uf = provinces.get(0).getFactories().get(0);
        u.setArmour(uf.restoreArmour(u.getUnitType()));
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

    public static void restoreHeroicCharge(Unit u) {
        u.setMeleeAttack(u.getMeleeAttack() / 2);
        u.setMorale(u.getMorale() * (2/3));
    }

    /**
     * Processes legionary eagle penalty after each battle
     * @param unit
     * @param initialTroops
     * @param humanProvince
     */
    public static void processLegionaryEagleDeath(Unit u, int casualty, Province p) {
        if (u.getAbility() == "Legionary Eagle") {
            double penalty = casualty * 0.2;
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

	public static void checkLERecapture(Province p) {
        Player player = p.getPlayer();
        if (player.mapContainsProvince(p)) {
            player.mapRemoveProvince(p);
        }
	}

    public static void setProvinces(ArrayList<Province> provinces) {
        Ability.provinces = provinces;
    }
}
