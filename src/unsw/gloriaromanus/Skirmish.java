package unsw.gloriaromanus;

import java.util.ArrayList;

public class Skirmish {
    private static final int MAX_ENG = 200;
    
    // During a skirmish, both units engage in a sequence of "engagements" against each other
    private ArrayList<Engagement> engagements;
    private String range;

    private Unit human;
    private Unit enemy;
    
    private Unit winner;
    private Unit defeated;
    private Unit broken;

    public Skirmish(Unit human, Unit enemy) {
        engagements = new ArrayList<Engagement>();
        this.human = human;
        this.enemy = enemy;
        this.winner = null;
        this.defeated = null;
        this.broken = null;
    }

    public Boolean start(String range) {
        this.range = range;
        // A sequence of skirmishes continuously run until a whole army is eliminated or routed entirely
        int i = 0;
        while(i < MAX_ENG) {
            if(addEngagement(range)) {
                break;
            }
            i++;
        }
        if (i == MAX_ENG || (human == null && enemy == null)) {
            // draw
            return false;
        }
        return true;
    }

    public Boolean addEngagement(String range) {
        Engagement e = new Engagement(range, human, enemy, this);
        engagements.add(e);
        if (e.checkEnemyDefeat()) {
            winner = human;
            defeated = enemy;
            return true;
        } else if (e.checkHumanDefeat()) {
            winner = enemy;
            defeated = human;
            return true;
        } else if (e.checkEnemyBreak()) {
            broken = enemy;
            return false;
        } else if (e.checkHumanBreak()) {
            broken = human;
            return false;
        } else if (enemy == null && human == null) {
            // successful routing
            return true;
        }
        return false;
    }

    public Unit getBroken() {
        return broken;
    }
}

/*

If all units from the opposing team have routed or are defeated, then the battle is a victory
If the attacking army in an invasion is victorious, the province is conquered by the invaders

If a battle is a draw, the invading army in a draw should return to the province it invaded from */