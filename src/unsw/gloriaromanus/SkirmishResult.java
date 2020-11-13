package unsw.gloriaromanus;

public class SkirmishResult extends Result {
    private Unit human;
    private Unit enemy;

    public SkirmishResult(Unit human, Unit enemy) {
        super();
        this.human = human;
        this.enemy = enemy;
    }

    private Unit winner;

    public void setNormal(Unit winner) {
        if (human.equals(winner)) {
            super.setVictory();
        } else {
            super.setDefeat();
        }
        this.winner = winner;
    }

    public void setOneRouted(Unit winner) {
        if (human.equals(winner)) {
            super.setResult("human routed");
        } else {
            super.setResult("enemy routed");
        }
        this.winner = winner;
    }

    public void setBothRouted() {
        super.setRouted();
    }

    private Unit brokenUnit1 = null;
    private Unit brokenUnit2 = null;

    public void setBrokenUnit(Unit u) {
        if (brokenUnit1 == null) {
            brokenUnit1 = u;
        } else {
            brokenUnit2 = u;
        }
    }

    public boolean isBroken(Unit u) {
        if (brokenUnit1 == null) {
            return false;
        }
        
        if (brokenUnit1.equals(u)) {
            return true;
        } 
        
        if (brokenUnit2 != null) {
            return true;
        }
        return false;
    }
}