package unsw.gloriaromanus;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a basic unit of soldiers
 * 
 * incomplete - should have heavy infantry, skirmishers, spearmen, lancers,
 * heavy cavalry, elephants, chariots, archers, slingers, horse-archers,
 * onagers, ballista, etc... higher classes include ranged infantry, cavalry,
 * infantry, artillery
 * 
 * current version represents a heavy infantry unit (almost no range, decent
 * armour and morale)
 */
public class Unit {
    private int numTroops; // the number of troops in this unit (should reduce based on depletion)
    private int range; // range of the unit
    private int armour; // armour defense
    private int morale; // resistance to fleeing
    private int speed; // ability to disengage from disadvantageous battle
    private int attack; // can be either missile or melee attack to simplify. Could improve
                        // implementation by differentiating!
    private int defenseSkill; // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense; // a shield

    public Unit(String unitType, int numTroops2) throws IOException {
        
        // TODO = obtain these values from the file for the unit
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/unit_config.json"));
        JSONObject config = new JSONObject(content);
        JSONObject unitStats = config.getJSONObject(unitType);

        FileWriter file = new FileWriter("src/unsw/gloriaromanus/testFile.json");
        file.write(unitStats.toString());
        file.close();
        
        // TODO = shield charge ability
        // TODO = heroic charge ability
        
        numTroops = numTroops2;
        range = 1;
        armour = 5;
        morale = 10;
        speed = 10;
        attack = 6;
        defenseSkill = 10;
        shieldDefense = 3;
    }

    public int getNumTroops(){
        return numTroops;
    }

    
}
