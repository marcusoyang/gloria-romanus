package unsw.gloriaromanus;

import java.util.ArrayList;

public class Skirmish {
    private static final int MAX_ENG = 200;
    
    // During a skirmish, both units engage in a sequence of "engagements" against each other
    private ArrayList<Engagement> engagements;

    private Unit human;
    private Unit enemy;

    private int engagementIndex;

    private SkirmishResult result;

    private int enemyInitialNumTroops;

    public Skirmish(Unit human, Unit enemy, int engagementIndex) {
        engagements = new ArrayList<Engagement>();
        this.human = human;
        this.enemy = enemy;
        this.engagementIndex = engagementIndex;
        result = new SkirmishResult(human, enemy);
        enemyInitialNumTroops = enemy.getNumTroops();
    }
    
    public void start(String range) {
        // A sequence of skirmishes continuously run until a whole army is eliminated or routed entirely
        while(engagementIndex < MAX_ENG) {
            addEngagement(range);
            if(!result.getResult().equals("")) {
                break;
            }
            engagementIndex++;
        }
        
        if (engagementIndex >= MAX_ENG) {
            result.setDraw();
        }
    }

    public void addEngagement(String range) {
        Engagement e;
        if (range.equals("melee")) {
            e = new MeleeEngagement(human, enemy, this);
        } else {
            e = new RangedEngagement(human, enemy, this);
        }  

        engagements.add(e);
    }

    public int getEngagementIndex() {
        return engagementIndex;
    }

    public void setNormalResult(Unit winner, Unit loser) {
        result.setNormal(winner);
    }

    public void setOneRoutedResult(Unit winner) {
        result.setOneRouted(winner);
    }

    public boolean isBroken(Unit u) {
        return result.isBroken(u);
    }

    public void setBrokenUnit(Unit u) {
        result.setBrokenUnit(u);
    }

    public void setBothRoutedResult() {
        result.setBothRouted();
    }

    public String getResult() {
        return result.getResult();
    }

    public int getEnemyInitialNumTroops() {
        return enemyInitialNumTroops;
    }
 }
