<<<<<<< HEAD
/*package unsw.gloriaromanus;

import java.util.Random;

public class Engagement {
    private String range;
    
    private Unit human;
    private Unit enemy;
    
    private int humanCasulty;
    private int enemyCasulty;
=======
package unsw.gloriaromanus;

import java.util.Random;

public abstract class Engagement {
    private Unit human;
    private Unit enemy;
    
    private int humanCasualty;
    private int enemyCasualty;
>>>>>>> battles
    
    private double humanBreakChance;
    private double enemyBreakChance;

<<<<<<< HEAD
    public Engagement(String range, Unit human, Unit enemy, Skirmish curr) {
        this.range = range;
        
        this.human = human;
        this.enemy = enemy;
        
        calculateCasulty();
        breakAttempt();
    }

    private void calculateCasulty() {
        Random N = new Random();
        switch(range) { 
            case "ranged":
                // Ranged units can inflict casulty caluclated below in ranged engagements. We cap the balanced battle stats to 10.
                if (human.getRange().equals("ranged")) {
                    double balancedBattleStats = (human.getRangedAttack() / (enemy.getArmour() + enemy.getShieldDefense()));
                    if (balancedBattleStats > 10) {
                        balancedBattleStats = 10;
                    }
                    Double enemyCasulty = (enemy.getNumTroops() * 0.1) * balancedBattleStats * (N.nextGaussian() + 1);
                    this.enemyCasulty = enemyCasulty.intValue();
                } else if (enemy.getRange().equals("ranged")) {
                    double balancedBattleStats = (enemy.getRangedAttack() / (human.getArmour() + human.getShieldDefense()));
                    if (balancedBattleStats > 10) {
                        balancedBattleStats = 10;
                    }
                    Double humanCasulty = (human.getNumTroops() * 0.1) * balancedBattleStats * (N.nextGaussian() + 1);
                    this.humanCasulty = humanCasulty.intValue();
                } 
                break;
            case "melee":
                // Units in a melee engagement inflict casualties against the opposing unit equal to (size of enemy unit at start of engagement x 10%) x (Effective melee attack damage of unit/(effective armor of enemy unit + effective shield of enemy unit + effective defense skill of enemy unit)) x (N+1 where N ~ N(0,1)
                Double enemyCasulty = (enemy.getNumTroops() * 0.1) * (human.getMeleeAttack() / enemy.getArmour() + enemy.getShieldDefense()+ enemy.getDefenseSkill()) * (N.nextGaussian() + 1);
                this.enemyCasulty = enemyCasulty.intValue();
                Double humanCasulty = (human.getNumTroops() * 0.1) * (enemy.getMeleeAttack() / human.getArmour() + human.getShieldDefense()+ human.getDefenseSkill()) * (N.nextGaussian() + 1);
                this.humanCasulty = humanCasulty.intValue();
        }
    }

    private void breakAttempt() {
        // Human is already broken.
        if (curr.getBroken().equals(human)) {
            
        }
            // The base-level probability of a unit "breaking" following an engagement is calculated as: 100% - (morale x 10%)
        humanBreakChance = 1 - (human.getMorale() * 0.1);
        enemyBreakChance = 1 - (enemy.getMorale() * 0.1);
        // The chance of breaking is increased by (a scalar addition): 
        humanBreakChance += (humanCasulty / human.getNumTroops()) / (enemyCasulty / enemy.getNumTroops()) * 0.1;
        enemyBreakChance += (enemyCasulty / enemy.getNumTroops()) / (humanCasulty / human.getNumTroops()) * 0.1;
=======
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
>>>>>>> battles

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
<<<<<<< HEAD

    }

    public Boolean checkHumanDefeat() {
        return human.checkDefeated(this.humanCasulty);
    }

    public Boolean checkEnemyDefeat() {
        return enemy.checkDefeated(this.enemyCasulty);
    }

    public Boolean checkHumanBreak() {
        Random r = new Random();
        if (r.nextDouble() <= humanBreakChance) {
            return true;
        } return false;
    }

    public Boolean checkEnemyBreak() {
        Random r = new Random();
        if (r.nextDouble() <= enemyBreakChance) {
            return true;
        } return false;
    }


}*/
=======
        
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
>>>>>>> battles
