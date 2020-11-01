package unsw.gloriaromanus;

import java.util.ArrayList;

public class Skirmish {
    private static final int MAX_ENG = 200;
    
    // During a skirmish, both units engage in a sequence of "engagements" against each other
    private ArrayList<Engagement> engagements;
    private String range;

    private Unit human;
    private Unit enemy;
    private int engagementIndex;

    private Status humanStatus;
    private Status enemyStatus;

    private int humanInitialNumTroops;
    private int enemyInitialNumTroops;

    public Skirmish(Unit human, Unit enemy, int engagementIndex) {
        engagements = new ArrayList<Engagement>();
        this.human = human;
        this.enemy = enemy;
        this.engagementIndex = engagementIndex;

        this.humanStatus = new Status(human);
        this.enemyStatus = new Status(enemy);

        humanInitialNumTroops = human.getNumTroops();
        enemyInitialNumTroops = enemy.getNumTroops();
    }
    public void start(String range) {
        this.range = range;
        // A sequence of skirmishes continuously run until a whole army is eliminated or routed entirely
        while(engagementIndex < MAX_ENG) {
            
            if(addEngagement(range)) {
                break;
            }
            engagementIndex++;
        }
        if (engagementIndex >= MAX_ENG) {
            humanStatus.setStatus("draw");
            enemyStatus.setStatus("draw");
        }
    }

    public Boolean addEngagement(String range) {
        Engagement e = new Engagement(range, human, enemy, this);
        engagements.add(e);
        if (e.checkEnemyDefeat()) {
            humanStatus.setStatus("winner");
            enemyStatus.setStatus("defeat");
            return true;
        } else if (e.checkHumanDefeat()) {
            enemyStatus.setStatus("winner");
            humanStatus.setStatus("defeat");
            return true;
        }  
        return false;
    }

    public int getEngagementIndex() {
        return engagementIndex;
    }

    public void setStatus(Unit u, String status) {
        if (u.equals(human)) {
            humanStatus.setStatus(status);
        } else {
            enemyStatus.setStatus(status);
        }
    }

    public String getHumanStatus() {
        return humanStatus.getStatus();
    }

    public String getEnemyStatus() {
        return enemyStatus.getStatus();
    }

    public int getHumanInitialNumTroops() {
        return humanInitialNumTroops;
    }

    public int getEnemyInitialNumTroops() {
        return enemyInitialNumTroops;
    }
 }
