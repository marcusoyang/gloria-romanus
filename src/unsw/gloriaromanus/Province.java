package unsw.gloriaromanus;

import java.io.IOException;
import java.util.ArrayList;

public class Province {
    private static final int MAX_FAC = 2;
    // Tax rates
    private static final double LOW_TR = 0.1;
    private static final double NOR_TR = 0.15;
    private static final double HI_TR = 0.2;
    private static final double VH_TR = 0.25;

    private String name;
    private Player player;
    private ArrayList<UnitFactory> factories;
    private ArrayList<Unit> units;
    private double taxRate;
    private int wealth;

    public Province() {
        //super();
    }

    public Province(String name, Player player, String unitConfig) {
        generateFactories(unitConfig);
        this.name = name;
        this.player = player;
        this.units = new ArrayList<Unit>();
        this.wealth = 0;
        this.taxRate = NOR_TR;
    }

    public Unit findUnit(int id) {
        for (Unit u : units) {
            if (u.getID() == id) {
                return u;
            }
        }
        return null;
    }

    public void moveUnit(Province p, int id) {
        // ArrayList<Unit> newUnits = new ArrayList<Unit>();
        // newUnits = units;
        for (Unit u : units) {
            if (u.getID() == id) {
                p.insertUnit(u);
                units.remove(u);
                return;
            }
        }
    }

    public void insertUnit(Unit u) {
        units.add(u);
    }

    /**
     * 
     * @return true if 
     */
    public boolean nextTurn() {
        boolean hasNewUnit = false;
        for (UnitFactory fac : factories) {
            Unit u = fac.nextTrainingTurn();
            if (u != null) {
                insertUnit(u);
                hasNewUnit = true;
            }
        }
        return hasNewUnit;
    }

    public boolean trainUnit(String unitType, int numTroops) throws IOException {
        for (UnitFactory fac : factories) {
            int price = fac.getCost(unitType, numTroops);
            if (!fac.getIsTraining() && player.getGold() >= price) {
                fac.addToTraining(unitType, numTroops);
                player.minusGold(price);
                return true;
            }
        }
        return false;
    }

    public int getArmyStrength() {
        // the sum of number of soldiers in unit x attack x defense for all units in the army
        // We initially assume that a unit that has been initially recruited on the province has 1 attack and 1 defense
        int totalAttack = 0;
        int totalDefense = 0;
        if (units != null) {
            for (Unit u : units) {
                totalAttack += u.calculateTotalAttack();
                totalDefense += u.calculateTotalDefense();
            }
        }
        return (this.getArmySize() * totalAttack * totalDefense);
    }

    private void generateFactories(String unitConfig) {
        factories = new ArrayList<UnitFactory>();
        for (int i = 0; i < MAX_FAC; i++) {
            UnitFactory factory = new UnitFactory(unitConfig, player);
            factories.add(factory);
        }
    }

    public String getName() {
        return name;
    }

    public int getArmySize() {
        int size = 0;
        if (units != null) {
            for (Unit u : units) {
                size += u.getNumTroops();
            }
        }
        return size;
    }

    public void setInitialArmy(int size) throws IOException {
        units.add(factories.get(0).newUnit("legionary", size));
    }

    public void setArmySize(int remainingSize) {
        int casulties = getArmySize() - remainingSize;
        if (units != null) {
            for (Unit u : units) {
                if (u.getNumTroops() < casulties) {
                    units.remove(u);
                    casulties -= u.getNumTroops();
                } else {
                    u.setNumTroops(u.getNumTroops() - casulties);
                }
            }
        }
    }

    public int getWealth() {
        return wealth;
    }

    public String getFaction() {
        return player.getFaction();
    }

    public void setFaction(String faction) {
        player.setFaction(faction);
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

	public void resetMovePoints() {
        for (Unit u : units) {
            u.resetMovementPoint();
        }
    }
    
    public void setMovePoints(int mp) {
        for (Unit u : units) {
            u.setMovementPoints(mp);
        }
	}

	public void stopUnitProduction() {
        for (UnitFactory uf : factories) {
            uf.removeTraining();
        }
	}

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void collectTaxRevenue() {
        Double taxRevenue = wealth * taxRate;
        player.plusGold(taxRevenue.intValue());
    }

    public void adjustTownWealth() {
        if (taxRate == LOW_TR) {
            wealth += 10;
        } else if (taxRate == HI_TR) {
            wealth -= 10;
        } else if (taxRate == VH_TR) {
            wealth -= 30;
            for (Unit u : units) {
                u.minusMorale(1);
            }
        }
    }

    public void changeTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public ArrayList<UnitFactory> getFactories() {
        return factories;
    }

    public double getTaxRate() {
        return taxRate;
    }
}