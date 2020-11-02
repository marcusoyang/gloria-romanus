package unsw.gloriaromanus;

public class Status {
    private Unit u;
    private String status = "fighting";

    public Status(Unit u) {
        this.u = u;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Unit getUnit() {
        return u;
    }
}