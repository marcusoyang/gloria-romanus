package unsw.gloriaromanus;

import java.util.Random;

public class Engagement {
    private String range;
    
    private Unit human;
    private Unit enemy;
    
    private int humanCasualty;
    private int enemyCasualty;
    
    private double humanBreakChance;
    private double enemyBreakChance;

    private Skirmish s;

    public Engagement(String range, Unit human, Unit enemy, Skirmish skirmish) {
        this.range = range;
        
        this.human = human;
        this.enemy = enemy;

        this.s = skirmish;
        
        calculatecasualty();
        breakAttempt();
    }

    private void calculatecasualty() {
        Random N = new Random();
        switch(range) { 
            case "ranged":
                // Ranged units can inflict casualty caluclated below in ranged engagements. We cap the balanced battle stats to 10.
                if (human.getRange().equals("ranged")) {
                    
                    double balancedBattleStats = (human.getTotalRangedAttack() / (enemy.getTotalArmour() + enemy.getTotalShieldDefense()));
                    if (balancedBattleStats > 10) {
                        balancedBattleStats = 10;
                    }
                    Double enemyCasualty = (enemy.getNumTroops() * 0.1) * balancedBattleStats * (N.nextGaussian() + 1);
                    if (enemyCasualty < 0.0) { enemyCasualty = 0.0;}
                    this.enemyCasualty = enemyCasualty.intValue();
                } else if (enemy.getRange().equals("ranged")) {
                    double balancedBattleStats = (enemy.getTotalRangedAttack() / (human.getTotalArmour() + human.getTotalShieldDefense()));
                    if (balancedBattleStats > 10) {
                        balancedBattleStats = 10;
                    }
                    Double humanCasualty = (human.getNumTroops() * 0.1) * balancedBattleStats * (N.nextGaussian() + 1);
                    if (humanCasualty < 0.0) { humanCasualty = 0.0; }
                    this.humanCasualty = humanCasualty.intValue();
                } 
                break;
            case "melee":
                // Units in a melee engagement inflict casualties as below.
                double denominator = enemy.getTotalArmour() + enemy.getTotalShieldDefense()+ enemy.getTotalDefenseSkill();
                if (denominator == 0) { denominator = 1; }
                Double enemyCasualty = (enemy.getNumTroops() * 0.1) * (human.getTotalMeleeAttack() / denominator) * (N.nextGaussian() + 1);
                
                if (enemyCasualty < 1.0) { enemyCasualty = 1.0;}
                this.enemyCasualty = enemyCasualty.intValue();
                
                denominator = human.getTotalArmour() + human.getTotalShieldDefense()+ human.getTotalDefenseSkill();
                if (denominator == 0) { denominator = 1; }
                Double humanCasualty = (human.getNumTroops() * 0.1) * (enemy.getTotalMeleeAttack() / denominator) * (N.nextGaussian() + 1);
                
                if (humanCasualty < 1.0) { humanCasualty = 1.0; }
                this.humanCasualty = humanCasualty.intValue();
        }
    }

    private void breakAttempt() {
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

    }

    private Boolean attemptRoute(Unit flee, Unit enemy) {
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

        return human.checkDefeated(this.humanCasualty);
    }

    public Boolean checkEnemyDefeat() {
        if (s.getHumanStatus().equals("broken")) {
            return attemptRoute(human, enemy);
        }
        
        return enemy.checkDefeated(this.enemyCasualty);
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
