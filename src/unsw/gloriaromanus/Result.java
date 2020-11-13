package unsw.gloriaromanus;

public class Result {
    private String result;
    
    public Result() {
        result = "";
    }

    public String getResult() {
        return result;
    }

    public void setVictory() {
        result = "victory";
    }

    public void setDefeat() {
        result = "defeat";
    }

    public void setDraw() {
        result = "draw";
    }

    public void setNotStarted() {
        result = "battle not started";
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setRouted() {
        result = "routed";
    }
}