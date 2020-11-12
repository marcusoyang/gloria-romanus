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

    public Engagement(Unit human, Unit enemy, Skirmish skirmish) {
        this.human = human;
        this.enemy = enemy;

        this.skirmish = skirmish;
    }

    public abstract int calculateHumanCasualty(Unit human, Unit enemy);
    public abstract int calculateEnemyCasualty(Unit human, Unit enemy);

    public void breakAttempt() {
        // The base-level probability of a unit "breaking" following an engagement is calculated as: 100% - (morale x 10%)
        humanBreakChance = 1 - (human.getMorale() * 0.1);
        enemyBreakChance = 1 - (enemy.getMorale() * 0.1);
        // The chance of breaking is increased by (a scalar addition):
        humanBreakChance += (humanCasualty / Double.valueOf(human.getNumTroops())) / (enemyCasualty / Double.valueOf(enemy.getNumTroops())) * 0.1;
        enemyBreakChance += (enemyCasualty / Double.valueOf(enemy.getNumTroops())) / (humanCasualty / Double.valueOf(human.getNumTroops())) * 0.1;

        //the minimum chance of breaking is 5%, and the maximum chance of breaking is 100%
        if (humanBreakChance < 0.05) {
            humanBreakChance = 0.05;
        } else if (humanBreakChance > 1) {
            humanBreakChance = 1;
        } else if (enemyBreakChance < 0.05) {
            enemyBreakChance = 0.05;
        } else if (enemyBreakChance > 1) {
            enemyBreakChance = 1;
        }
        
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
        if (chance < 0.1) {
            chance = 0.1;
        } else if (chance > 1) {
            chance = 1;
        }

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
        if (skirmish.isBroken(human)) {
            attemptRoute(human, enemy);
        } else if (human.isDefeated(humanCasualty)) {
            skirmish.setNormalResult(enemy, human);
        } 
        
        if (skirmish.isBroken(enemy)) {
            attemptRoute(enemy, human);
        } else if (enemy.isDefeated(enemyCasualty)) {
            skirmish.setNormalResult(human, enemy);
        }
    }
}   