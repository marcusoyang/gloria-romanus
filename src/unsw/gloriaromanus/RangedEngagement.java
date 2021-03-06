package unsw.gloriaromanus;

import java.util.Random;

public class RangedEngagement extends Engagement {
    public RangedEngagement(Unit human, Unit enemy, Skirmish skirmish) {
        super(human, enemy, skirmish);
        inflictCasualties(human, enemy);
        super.breakAttempt();
    }
    
    public void inflictCasualties(Unit human, Unit enemy) {
        super.setHumanCasualty(calculateHumanCasualty(human, enemy));
        super.setEnemyCasualty(calculateEnemyCasualty(human, enemy));

        super.inflictCasualties();
    } 

    public int calculateHumanCasualty(Unit human, Unit enemy) {
       if (enemy.getRange().equals("melee")) {
            return 0;
        }
        // initiating the Skirmish Anti-Armour ability
        Ability.processSkirmisherAntiArmour(enemy, human);
        double balancedBattleStats = (enemy.calculateTotalRangedAttack() / (human.calculateTotalArmour() + human.calculateTotalShieldDefense()));
        Ability.restoreSkirmisherAntiArmour(enemy, human);

        // We cap this stats to 10 if it exceeds 10.
        if (balancedBattleStats > 10) {
            balancedBattleStats = 10;
        }
        Random N = new Random();
        Double humanCasualty = (human.getNumTroops() * 0.1) * balancedBattleStats * (N.nextGaussian() + 1);
        // Casualty cannot be less than 1.
        if (humanCasualty < 1.0) { humanCasualty = 1.0; }
        return humanCasualty.intValue();
    }

    public int calculateEnemyCasualty(Unit human, Unit enemy) {
        Random N = new Random();
        if (human.getRange().equals("melee")) {
            return 0;
        }
        Ability.processSkirmisherAntiArmour(human, enemy);
        double balancedBattleStats = (human.calculateTotalRangedAttack() / (enemy.calculateTotalArmour() + enemy.calculateTotalShieldDefense()));
        Ability.restoreSkirmisherAntiArmour(human, enemy);
        if (balancedBattleStats > 10) {
            balancedBattleStats = 10;
        }
        Double enemyCasualty = (enemy.getNumTroops() * 0.1) * balancedBattleStats * (N.nextGaussian() + 1);
        if (enemyCasualty < 1.0) { enemyCasualty = 1.0;}
        return enemyCasualty.intValue();
    }
}