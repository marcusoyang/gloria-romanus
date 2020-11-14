package unsw.gloriaromanus;

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
    private static final int CAVALRY_MP = 15;
    private static final int INFANTRY_MP = 10;
    private static final int ARTILLERY_MP = 4;
    private static int counter = 0;
    private int id;
    private String unitType;
    private int numTroops; // the number of troops in this unit (should reduce based on depletion)
    private int meleeAttack; // can be either missile or melee attack to simplify. Could improve
    private int rangedAttack; // implementation by differentiating!
    private int defenseSkill; // skill to defend in battle. Does not protect from arrows!
    private int armour; // armour defense
    private int shieldDefense; // a shield
    private double morale; // resistance to fleeing
    private double speed; // ability to disengage from disadvantageous battle
    private String range; // range of the unit
    private String type;
    private String ability;
    private int movementPoints;
    private int turnsToProduce;
    private Player player;

    public Unit() {
        increaseIDCounter();
    }

    public void increaseIDCounter() {
        counter++;
    }

    public int getID() {
        return id;
    }

    public void setID() {
        this.id = counter;
    }

    public int getNumTroops() {
        return numTroops;
    }

    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }

    public int getMeleeAttack() {
        return meleeAttack;
    }

    public void setMeleeAttack(int meleeAttack) {
        this.meleeAttack = meleeAttack;
    }

    public void addMeleeAttack(int i) {
        this.meleeAttack += i;
    }

    public int getRangedAttack() {
        return rangedAttack;
    }

    public void setRangedAttack(int rangedAttack) {
        this.rangedAttack = rangedAttack;
    }

    public int getDefenseSkill() {
        return defenseSkill;
    }

    public void setDefenseSkill(int defenseSkill) {
        this.defenseSkill = defenseSkill;
    }

    public int getArmour() {
        return armour;
    }

    public void setArmour(int armour) {
        if (armour < 1) {
            armour = 1;
        }
        this.armour = armour;
    }

    public int getShieldDefense() {
        return shieldDefense;
    }

    public void setShieldDefense(int shieldDefense) {
        if (shieldDefense < 1) {
            shieldDefense = 1;
        }
        this.shieldDefense = shieldDefense;
    }

    public double getMorale() {
        return morale;

    }

    public void setMorale(double morale) {
        if (morale < 1) { morale = 1; }
        this.morale = morale;
    }

    public void addMorale(double d) {
        this.morale += d;
    }
    
	public void minusMorale(double d) {
        this.morale -= d;
        if (this.morale < 1) {
            this.morale = 1;
        }
	}

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public void setMovementPoints(int movementPoints) {
        this.movementPoints = movementPoints;
    }

    public void resetMovementPoint() {
        switch (type) {
        case "heavy infantry":
            this.movementPoints = INFANTRY_MP;
        case "spearmen":
            this.movementPoints = INFANTRY_MP;
        case "missile infantry":
            this.movementPoints = INFANTRY_MP;
        case "cavalry":
            this.movementPoints = CAVALRY_MP;
        case "horse archer":
            this.movementPoints = CAVALRY_MP;
        case "artillery":
            this.movementPoints = ARTILLERY_MP;
        }
    }

    public void minusMovementPoints(int i) {
        this.movementPoints -= i;
    }

    public int getTurnsToProduce() {
        return turnsToProduce;
    }

    public void setTurnsToProduce(int turnsToProduce) {
        this.turnsToProduce = turnsToProduce;
    }

    public void minusTurnsToProduce(int i) {
        this.turnsToProduce -= i;
    }

    public int calculateTotalAttack() {
        return meleeAttack + rangedAttack;
    }

    public int calculateTotalDefense() {
        return defenseSkill + armour + shieldDefense;
    }

    public Boolean isDefeated(int minusTroopSize) {
        int remaining = numTroops - minusTroopSize;
        if (remaining < 0) {
            this.numTroops = 0;
            return true;
        }

        this.numTroops = remaining;
        return false;
    }

    public static void setCounter(int counter) {
        Unit.counter = counter;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitType() {
        return unitType;
    }

    public int calculateTotalRangedAttack() {
        return rangedAttack * numTroops;
    }

    public int calculateTotalArmour() {
        return armour * numTroops;
    }

    public int calculateTotalShieldDefense() {
        return shieldDefense * numTroops;
    }

    public int calculateTotalDefenseSkill() {
        return defenseSkill * numTroops;
    }

    public int calculateTotalMeleeAttack() {
        return meleeAttack * numTroops;
    }
}

/*public Unit(String unitType, int numTroops2, String config) throws IOException {

        JSONObject unitStats = new JSONObject(config).getJSONObject(unitType);

        // Unit stat values obtained from unit config file
        this.numTroops = numTroops2;
        this.meleeAttack = unitStats.getInt("meleeAttack");
        this.rangedAttack = unitStats.optInt("rangedAttack");
        this.defenseSkill = unitStats.getInt("defense");
        this.armour = unitStats.getInt("armour");
        this.shieldDefense = unitStats.getInt("shield");
        this.morale = unitStats.getInt("morale");
        this.speed = unitStats.getInt("speed");
        this.range = unitStats.getString("range");
        this.type = unitStats.getString("type");
        this.ability = unitStats.getString("ability");
        this.turnsToProduce = unitStats.getInt("turnsToProduce");

        // TODO = shield charge ability
        // TODO = heroic charge ability

    }*/

   /*
     * public static void main(String[] args) throws JsonGenerationException,
     * JsonMappingException, IOException { String content =
     * Files.readString(Paths.get("src/unsw/gloriaromanus/unit_config.json"));
     * JSONObject config = new JSONObject(content); JSONObject unitStats =
     * config.getJSONObject("berserker");
     * 
     * // ObjectMapper objectMapper = new ObjectMapper(); //
     * objectMapper.writeValue(new File("src/unsw/gloriaromanus/testFile.json"),
     * unitStats.toString());
     * 
     * FileWriter file = new FileWriter("src/unsw/gloriaromanus/testFile.json");
     * file.write(unitStats.toString());
     * file.close(); }
     */