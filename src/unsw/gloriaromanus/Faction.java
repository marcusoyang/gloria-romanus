package unsw.gloriaromanus;

import java.util.ArrayList;

public class Faction {
    String name;
    ArrayList<Province> provinces;

    public Faction(String name) {
        this.name = name;
        this.provinces = new ArrayList<Province>();
    }

    public void addProvince(Province province) {
        provinces.add(province);
    }
}