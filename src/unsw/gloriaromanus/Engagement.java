package unsw.gloriaromanus;

import java.util.Random;

public class Engagement {
    private String range;
    
    private Unit human;
    private Unit enemy;
    
    private int humanCasulty;
    private int enemyCasulty;
    
    private double humanBreakChance;
    private double enemyBreakChance;

    private Skirmish s;

    public Engagement(String range, Unit human, Unit enemy, Skirmish skirmish) {
        this.range = range;
        
        this.human = human;
        this.enemy = enemy;

        this.s = skirmish;
        
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
                    Double enemyCasulty = (s.getEnemyInitialNumTroops() * 0.1) * balancedBattleStats * (N.nextGaussian() + 1);
                    if (enemyCasulty < 0.0) { enemyCasulty = 0.0;}
                    this.enemyCasulty = enemyCasulty.intValue();
                } else if (enemy.getRange().equals("ranged")) {
                    double balancedBattleStats = (enemy.getRangedAttack() / (human.getArmour() + human.getShieldDefense()));
                    if (balancedBattleStats > 10) {
                        balancedBattleStats = 10;
                    }
                    Double humanCasulty = (s.getHumanInitialNumTroops() * 0.1) * balancedBattleStats * (N.nextGaussian() + 1);
                    if (humanCasulty < 0.0) { humanCasulty = 0.0; }
                    this.humanCasulty = humanCasulty.intValue();
                } 
                break;
            case "melee":
                // Units in a melee engagement inflict casualties as below.
                double denominator = enemy.getArmour() + enemy.getShieldDefense()+ enemy.getDefenseSkill();
                if (denominator == 0) { denominator = 1; }
                Double enemyCasulty = (s.getEnemyInitialNumTroops() * 0.1) * (human.getMeleeAttack() / denominator) * (N.nextGaussian() + 1);
                
                if (enemyCasulty < 0.0) { enemyCasulty = 0.0;}
                this.enemyCasulty = enemyCasulty.intValue();
                
                denominator = human.getArmour() + human.getShieldDefense()+ human.getDefenseSkill();
                if (denominator == 0) { denominator = 1; }
                Double humanCasulty = (s.getHumanInitialNumTroops() * 0.1) * (enemy.getMeleeAttack() / denominator) * (N.nextGaussian() + 1);
                
                if (humanCasulty < 0.0) { humanCasulty = 0.0; }
                this.humanCasulty = humanCasulty.intValue();
        }
    }

    private void breakAttempt() {
        // The base-level probability of a unit "breaking" following an engagement is calculated as: 100% - (morale x 10%)
        humanBreakChance = 1 - (human.getMorale() * 0.1);
        enemyBreakChance = 1 - (enemy.getMorale() * 0.1);
        // The chance of breaking is increased by (a scalar addition):
        humanBreakChance += (humanCasulty / Double.valueOf(s.getHumanInitialNumTroops())) / (enemyCasulty / Double.valueOf(s.getEnemyInitialNumTroops())) * 0.1;
        enemyBreakChance += (enemyCasulty / Double.valueOf(s.getEnemyInitialNumTroops())) / (humanCasulty / Double.valueOf(s.getHumanInitialNumTroops())) * 0.1;

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

    }

    public Boolean attemptRoute(Unit flee, Unit enemy) {
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
            s.setStatus(flee, "routed");
            s.setStatus(enemy, "winner");
            return true;
        }
        return false;
    }


    public Boolean checkHumanDefeat() {
        // if the enemy has been broken, they do not do damage to human unit.
        // Instead, they'll try to route.
        if (s.getEnemyStatus().equals("broken")) {
            return attemptRoute(enemy, human);
        }

        return human.checkDefeated(this.humanCasulty);
    }

    public Boolean checkEnemyDefeat() {
        if (s.getHumanStatus().equals("broken")) {
            return attemptRoute(human, enemy);
        }
        
        return enemy.checkDefeated(this.enemyCasulty);
    }

    public Boolean checkBreak() {
        Random r = new Random();
        if (r.nextDouble() <= this.humanBreakChance) {
            s.setStatus(human, "broken");
        }

        if (r.nextDouble() <= this.enemyBreakChance) {
            s.setStatus(enemy, "broken");
        }
        
        if (s.getHumanStatus().equals("broken") && s.getEnemyStatus().equals("broken")) {
            s.setStatus(human, "routed");
            s.setStatus(enemy, "routed");
            return true;
        }
        return false;
    }
}
