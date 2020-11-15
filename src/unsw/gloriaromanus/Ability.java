package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Random;

public abstract class Ability {
    private static ArrayList<Province> provinces;
    private static ArrayList<Unit> invadingUnits;
    private static ArrayList<Unit> defendingUnits; 

    public static void setProvinces(ArrayList<Province> provinces) {
        Ability.provinces = provinces;
    }
    
    public static void initiate(ArrayList<Unit> invadingUnits, ArrayList<Unit> defendingUnits) {
        Ability.invadingUnits = invadingUnits;
        Ability.defendingUnits = defendingUnits;
        for (Unit u : invadingUnits) {
            processAbility(u);
        }

        for (Unit u : defendingUnits) {
            processAbility(u);
        }
    }

    public static void initiateDefend(ArrayList<Unit> units) {
        Ability.defendingUnits = units;
        for (Unit u : units) {
            processAbility(u);
        }
    }

	public static void restore(ArrayList<Unit> units) {
        for (Unit u : units) {
            restoreAbility(u);
        }
	}

    public static void processAbility(Unit u) {
        switch (u.getAbility()) {
            case "Legionary Eagle": processLegionaryEagle(u); break;
            case "Berserker Rage": processBerserkerRage(u); break;
            case "Phalanx": processPhalanx(u); break;
        }
    }

    public static void processSkirmishAbility(Unit invadingUnit, Unit defendingUnit) {
        switch (invadingUnit.getAbility()) {
            case "Cantabrian Circle" : processCantabrianCircle(invadingUnit, defendingUnit); break;
            case "Druidic Fervour" : processDruidicFervour(invadingUnit);
        }

        if (invadingUnit.getRange().equals("melee") && invadingUnit.getType().equals("cavalry")) {
            processHeroicCharge(invadingUnit);
        }
    }

    public static void restoreAbility(Unit u) {
        switch (u.getAbility()) {
            case "Legionary Eagle": restoreLegionaryEagle(u); break;
            case "Berserker Rage": restoreBerserkerRage(u); break;
            case "Phalanx": restorePhalanx(u);
        } 
    }

    public static void restoreSkirmishAbility(Unit invadingUnit, Unit defendingUnit) {
        switch (invadingUnit.getAbility()) {
            case "Cantabrian Circle" : restoreCantabrianCircle(invadingUnit, defendingUnit);
            case "Druidic Fervour" : restoreDruidicFervour(invadingUnit);
        }

        if (invadingUnit.getRange().equals("melee") && invadingUnit.getType().equals("cavalry")) {
            restoreHeroicCharge(invadingUnit);
        }
    }

    private static void processLegionaryEagle(Unit u) {
        for (Unit u2 : getUnits(u)) {
            u2.addMorale(1);
        }
    }

    private static void restoreLegionaryEagle(Unit u) {
        for (Unit u2 : getUnits(u)) {
            u2.minusMorale(1);
        }
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
                ArrayList<Unit> provinceUnits = p.getUnits();
                for (Unit u2 : provinceUnits) {
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

    private static boolean heroicChargeInitiated = false;

    public static void processHeroicCharge(Unit u) {
        if (getUnits(u).size() * 2 < getOtherUnits(u).size()) {
            u.setMeleeAttack(u.getMeleeAttack() * 2);  // double melee attack
            u.addMorale(u.getMorale() / 2);            // 50% higher morale
            Ability.heroicChargeInitiated = true;
        }
	}

    public static void restoreHeroicCharge(Unit u) {
        if (heroicChargeInitiated) {
            u.setMeleeAttack(u.getMeleeAttack() / 2);
            u.setMorale(u.getMorale() * (2/3));
            Ability.heroicChargeInitiated = false;
        }
    }

    private static void processPhalanx(Unit u) {
        u.setDefenseSkill(u.getDefenseSkill() * 2);   // double defense
        u.setSpeed(u.getSpeed() / 2);                 // half speed
    }

    private static void restorePhalanx(Unit u) {
        u.setDefenseSkill(u.getDefenseSkill() / 2);
        u.setSpeed(u.getSpeed() / 2);
    }

    public static void processSkirmisherAntiArmour(Unit javelinSkirmisher, Unit other) {
        if (javelinSkirmisher.getAbility().equals("Skirmisher Anti-Armour")) {
            other.setArmour(other.getArmour() / 2);
        }
    }

    public static void restoreSkirmisherAntiArmour(Unit javelinSkirmisher, Unit other) {
        if (javelinSkirmisher.getAbility().equals("Skirmisher Anti-Armour")) {
           other.setArmour(other.getArmour() * 2);
        }
    }

    public static Unit processElephantAmok(Unit elephant, Unit other) {
        if (!elephant.getAbility().equals("Elephant Amok")) {
            return other;
        }

        ArrayList<Unit> units = getUnits(elephant);        
        if (units.size() != 1) {
            Random r = new Random();
            if (/*r.nextDouble() <= 0.1*/true) {
                int randNum = r.nextInt(units.size());
                if (units.get(randNum).equals(elephant)) {
                    randNum = findNextIndex(units, randNum);
                }
                Unit u = units.get(randNum);
                return u;
            }
        }
        return other;
    }

    public static void processCantabrianCircle(Unit horseArcher, Unit missileUnit) {
        if (missileUnit.getType().startsWith("missile")) {
            missileUnit.setRangedAttack(missileUnit.getRangedAttack() / 2);
        } 
    }

    public static void restoreCantabrianCircle(Unit horseArcher, Unit missileUnit) {
        if (missileUnit.getType().startsWith("missile")) {
            missileUnit.setRangedAttack(missileUnit.getRangedAttack() * 2);
        }
    }

    public static void processDruidicFervour(Unit druid) {
        int numDruids = countDruidUnit(getUnits(druid));
     
        for (Unit u : getUnits(druid)) {
            u.addMorale(u.getMorale() * (numDruids * 0.1));
        }

        for (Unit u : getOtherUnits(druid)) {
            u.minusMorale(u.getMorale() * (numDruids * 0.05));
        }
    }

    private static void restoreDruidicFervour(Unit druid) {
        int numDruids = countDruidUnit(getUnits(druid));
     
        for (Unit u : getUnits(druid)) {
            u.addMorale(u.getMorale() / (numDruids * 0.1));
        }

        for (Unit u : getOtherUnits(druid)) {
            u.minusMorale(u.getMorale() / (numDruids * 0.05));
        }
    }

    private static int countDruidUnit(ArrayList<Unit> units) {
        int counter = 0;
        for (Unit u : units) {
            if (u.getAbility().equals("Druidic Fervour")) {
                counter++;
            }
        }

        if (counter > 5) {
            return 5;
        }

        return counter;
    }

    private static ArrayList<Unit> getUnits(Unit u) {
        if (invadingUnits.contains(u)) {
            return invadingUnits;
        }
        return defendingUnits;
    }

    private static ArrayList<Unit> getOtherUnits(Unit u) {
        if (invadingUnits.contains(u)) {
            return defendingUnits;
        }
        return invadingUnits;
    }

    private static int findNextIndex(ArrayList<Unit> units, int index) {
        if (index == units.size() - 1) {
            return 0;
        } 
        return index++;
    }

}
