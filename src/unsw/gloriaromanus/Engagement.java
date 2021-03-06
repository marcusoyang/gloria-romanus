package unsw.gloriaromanus;

import java.util.Random;

public abstract class Engagement {
    private Unit human;
    private Unit enemy;
    
    private int humanCasualty;
    private int enemyCasualty;
    
    private double humanBreakChance;
    private double enemyBreakChance;

    private Skirmish skirmish;

    private int humanInitialNumTroops;
    private int enemyInitialNumTroops;

    public Engagement(Unit human, Unit enemy, Skirmish skirmish) {
        this.human = human;
        this.enemy = enemy;

        this.skirmish = skirmish;

        humanInitialNumTroops = human.getNumTroops();
        enemyInitialNumTroops = enemy.getNumTroops();
    }

    // These abstract methods are implemented in the subclasses of Engagement. The use of factory pattern is here.
    public abstract int calculateHumanCasualty(Unit human, Unit enemy);
    public abstract int calculateEnemyCasualty(Unit human, Unit enemy);

    public void breakAttempt() {
        // The base-level probability of a unit "breaking" following an engagement is calculated as: 100% - (morale x 1%). (Different from Spec, changed to fit our game).
        humanBreakChance = 1 - (human.getMorale() * 0.01);
        enemyBreakChance = 1 - (enemy.getMorale() * 0.01);

        // The chance of breaking is increased by (a scalar addition):
        humanBreakChance += (((humanCasualty / Double.valueOf(humanInitialNumTroops)) / (min((double)enemyCasualty, 1) / Double.valueOf(enemyInitialNumTroops))) * 0.1);
        enemyBreakChance += (((enemyCasualty / Double.valueOf(enemyInitialNumTroops)) / (min((double)humanCasualty, 1) / Double.valueOf(humanInitialNumTroops))) * 0.1);

        //the minimum chance of breaking is 5%, and the maximum chance of breaking is 100%
        humanBreakChance = min(humanBreakChance, 0.05);
        humanBreakChance = max(humanBreakChance, 1);
        enemyBreakChance = min(enemyBreakChance, 0.05);
        enemyBreakChance = max(enemyBreakChance, 1);
        
        Random r = new Random();
        if (r.nextDouble() <= humanBreakChance) {
            skirmish.setBrokenUnit(human);
        }

        if (r.nextDouble() <= enemyBreakChance) {
            skirmish.setBrokenUnit(enemy);
        }
        
        if (skirmish.isBroken(human) && skirmish.isBroken(enemy)) {
            skirmish.setBothRoutedResult();
        }
    }

    private void attemptRoute(Unit flee, Unit enemy) {
        // Units repeatedly attempt to flee the battle until it is successful or destroyed
        // There is a calculated chance of routing which depends on the unit's speeds
        double chance = 0.5 + (0.1 * (flee.getSpeed() - enemy.getSpeed()));

        // Minium chance is 10% and the maximum chance is 100%
        chance = min(chance, 0.1);
        chance = max(chance, 1);

        // A unit that successfully routes from the battle as a losing team will return to the province it attacked from
        Random r = new Random();
        if (r.nextDouble() <= chance) {
            skirmish.setOneRoutedResult(enemy);
        }
    }

    public void setHumanCasualty(int casualty) {
        this.humanCasualty = casualty;
    }

    public void setEnemyCasualty(int casualty) {
        this.enemyCasualty = casualty;
    }

    public void inflictCasualties() {
        // human attacking the enemy
        if (skirmish.isBroken(human)) {
            attemptRoute(human, enemy);
        } else {
            Unit directedUnit = Ability.processElephantAmok(human, enemy);
            if (directedUnit.equals(enemy)) {
                if(enemy.isDefeated(enemyCasualty)) {
                    skirmish.setNormalResult(human, enemy);
                }
            } else {
                if (directedUnit.isDefeated(enemyCasualty)) {
                skirmish.removeUnit(directedUnit);}
            }

        }

        // enemy attacking human
        if (skirmish.isBroken(enemy)) {
            attemptRoute(enemy, human);
        } else {
            Unit directedUnit = Ability.processElephantAmok(enemy, human);
            if (directedUnit.equals(human)) {
                if (human.isDefeated(humanCasualty)) {
                    skirmish.setNormalResult(enemy, human);
                }
            } else if (directedUnit.isDefeated(humanCasualty)) {
                skirmish.setNormalResult(human, enemy);
            }
        }
    }

    private double min(double n, double min) {
        if (n < min) {
            return min;
        }
        return n;
    }

    private double max(double n, double max) {
        if (n > max) {
            return max;
        }
        return n;
    }

    public int getEnemyCasualty() {
        return enemyCasualty;
    }

    public int getHumanCasualty() {
        return humanCasualty;
    }
}   
