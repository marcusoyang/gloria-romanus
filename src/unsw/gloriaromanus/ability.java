package unsw.gloriaromanus;

import java.util.ArrayList;

public class ability {
    private static ArrayList<Province> provinces;
    
    public static void process() {
        for (Province p : provinces) {
            ArrayList<Unit> units = p.getUnits();
            for (Unit u : units) {
                processAbility(u);
            }
        }
    }

    private static void processAbility(Unit u) {
        switch (u.getAbility()) {
            case "Legionary Eagle": processLegionaryEagle(u);
            case "Berserker Rage":
            case "Phalanx":
            case "Skirmisher Anti-Armour":
            case "Elephant Amok":
            case "Cantabrian Circle":
            case "Druidic Fervour":
        }
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

    public static void processLegionaryEagleDeath(Province p) {
        for (Province p2 : provinces) {
            if (p2.getFaction().equals(p.getFaction())) {
                ArrayList<Unit> units = p2.getUnits();
                for (Unit u2 : units) {
                    u2.minusMorale(0.2);
                }
            }
        }
    }

    public static void setProvinces(ArrayList<Province> provinces) {
        ability.provinces = provinces;
    }
}
