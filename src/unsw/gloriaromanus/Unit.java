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
    private static int counter = 0;
    private int id;
    private int numTroops; // the number of troops in this unit (should reduce based on depletion)
    private int meleeAttack; // can be either missile or melee attack to simplify. Could improve
    private int rangedAttack; // implementation by differentiating!
    private int defenseSkill; // skill to defend in battle. Does not protect from arrows!
    private int armour; // armour defense
    private int shieldDefense; // a shield
    private int morale; // resistance to fleeing
    private int speed; // ability to disengage from disadvantageous battle
    private String range; // range of the unit
    private String type;
    private String ability;
    private int movementPoints;
    private int turnsToProduce;

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
        this.armour = armour;
    }

    public int getShieldDefense() {
        return shieldDefense;
    }

    public void setShieldDefense(int shieldDefense) {
        this.shieldDefense = shieldDefense;
    }

    public int getMorale() {
        return morale;
    }

    public void setMorale(int morale) {
        this.morale = morale;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
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

    public int getTurnsToProduce() {
        return turnsToProduce;
    }

    public void setTurnsToProduce(int turnsToProduce) {
        this.turnsToProduce = turnsToProduce;
    }

    public void minusTurnsToProduce(int i) {
        this.turnsToProduce -= i;
    }

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
     * file.write(unitStats.toString()); file.close(); }
     */

    public int getTotalDefense() {
        return defenseSkill + shieldDefense;
    }
}
