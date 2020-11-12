package unsw.gloriaromanus;

import java.util.Random;

public class MeleeEngagement extends Engagement {
    public MeleeEngagement(Unit human, Unit enemy, Skirmish skirmish) {
        super(human, enemy, skirmish);
        inflictCasualties(human, enemy);
        super.breakAttempt();
    }

    private void inflictCasualties(Unit human, Unit enemy) {
        super.setHumanCasualty(calculateHumanCasualty(human, enemy));
        super.setEnemyCasualty(calculateEnemyCasualty(human, enemy));

        super.inflictCasualties();
    }

    public int calculateHumanCasualty(Unit human, Unit enemy) {
        Random N = new Random();
        double denominator = human.getTotalArmour() + human.getTotalShieldDefense()+ human.getTotalDefenseSkill();
        if (denominator == 0) { denominator = 1; }
        Double humanCasualty = (human.getNumTroops() * 0.1) * (enemy.getTotalMeleeAttack() / denominator) * (N.nextGaussian() + 1);
        
        if (humanCasualty < 1.0) { humanCasualty = 1.0; }
        return humanCasualty.intValue();
    }

    public int calculateEnemyCasualty(Unit human, Unit enemy) {
        Random N = new Random();
        double denominator = enemy.getTotalArmour() + enemy.getTotalShieldDefense()+ enemy.getTotalDefenseSkill();
        if (denominator == 0) { denominator = 1; }
        Double enemyCasualty = (enemy.getNumTroops() * 0.1) * (human.getTotalMeleeAttack() / denominator) * (N.nextGaussian() + 1);
        
        if (enemyCasualty < 1.0) { enemyCasualty = 1.0;}
        return enemyCasualty.intValue();
    }
}