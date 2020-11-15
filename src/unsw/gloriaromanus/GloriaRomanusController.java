package unsw.gloriaromanus;

/**
 * Scenebuilder does not work with this line in main.fxml
 * Restore it to the top before launching
 * <?import com.esri.arcgisruntime.mapping.view.MapView?>
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

public class GloriaRomanusController{

  @FXML
  private MapView mapView;
  @FXML
  private TextField current_faction;
  @FXML
  private TextField current_player_gold;
  @FXML
  private TextField current_year;
  @FXML
  private TextField invading_province;
  @FXML
  private TextField opponent_province;
  @FXML
  private TextArea output_terminal;
  @FXML
  private TextField saveFilename;
  @FXML
  private Slider volumeSlider;

  private ArcGISMap map;

  private static final int MOVE_COST = 4;
  private static final int TREASURY_GOAL = 100000;
  private static final int WEALTH_GOAL = 400000;
  private static final int CONQUEST_VICTORY = 1;
  private static final int TREASURY_VICTORY = 2;
  private static final int WEALTH_VICTORY = 3;
  private static final String DEFAULT_FILENAME = "_default";

  private ArrayList<Player> players;
  private ArrayList<String> factions;
  private ArrayList<Province> provinces;
  private int currentPlayerID;
  private int currentYear;
  private String filename;
  private String unitConfig;
  private boolean hasWon;
  private boolean displayEngagement;
  private Province waitingForDestination;
  private Province destination;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;
  private ArrayList<Unit> currentlySelectedHumanProvinceUnits;

  private FeatureLayer featureLayer_provinces;

  private StartScreen startScreen;
  private RecruitScreen recruitScreen;
  private InvadeScreen invadeScreen;
  private Audio audio;

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException {
    
    filename = DEFAULT_FILENAME;  // Default prefix for save filename.

    initializeVolumeSlider();
    provinces = new ArrayList<Province>();
    players = new ArrayList<Player>();
    hasWon = false;
    displayEngagement = false;
    waitingForDestination = null;

    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;  
  }

  public void newGame(Integer numPlayers) throws IOException {
    initialize();
    generatePlayers(numPlayers);
    initializeOfflineMultiOwnership();
    Random r = new Random();
    for (Province p: provinces) {
      p.setInitialArmy(r.nextInt(500));
    }
    currentPlayerID = 1;
    currentYear = 0;
    clearTextFields();
    updateFrontendText();
    initializeProvinceLayers();  
  }

  public void loadGame(String loadFilename) throws IOException {  
    // String content = stringFromCampaignFile(filename);
    // JSONObject j = new JSONObject(content);
    filename = loadFilename;
    clearTextFields();
    saveFilename.setText(filename);

    restoreSavedDetails();
    updateFrontendText();
    initializeProvinceLayers();  
  }

  private void updateFrontendText() {
    current_faction.setText(getPlayerFromID(currentPlayerID).getFaction());
    current_year.setText(String.valueOf(currentYear));
    current_player_gold.setText(String.valueOf(getPlayerGold(currentPlayerID)));
  }

  private void clearTextFields() {
    output_terminal.clear();
    saveFilename.clear();
  }

  private int getPlayerGold(int ID) {
    Double gold = getPlayerFromID(ID).getGold();
    return gold.intValue();
  }

  /**
   * Initializes an observer to update the volume when the slider has changed.
   */
  private void initializeVolumeSlider() {
    volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      audio.changeVolume((double) newValue);
    });
  }

  /**
   * Initializes the player list from the number of players and allocates a faction.
   * @param numPlayers
   * @throws IOException
   */
  private void generatePlayers(Integer numPlayers) throws IOException {
    // JSONArray factions = readFactionsList();
    for (int i = 0; i < numPlayers; i++) {
      Player p = new Player(i + 1, factions.get(i));
      players.add(p);
    }
  }

  /*private JSONArray readFactionsList() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/factions_list.json"));
    JSONObject ownership = new JSONObject(content);
    JSONArray ja = ownership.getJSONArray("factions_list");
    return ja;
  }*/

  public void loadConfig(String path) throws IOException {
    unitConfig = Files.readString(Paths.get(path));
  }

  public boolean moveUnits(List<Integer> ids, Province src, Province dest) throws IOException {
    String adjacencyMatrix = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    DijkstraAlgorithm d = new DijkstraAlgorithm(adjacencyMatrix, provinces);
    int shortestPathLength = d.findShortestPathLength(src.getName(), dest.getName());
    boolean allCanMove = true;
    for (int id : ids) {
      Unit u = src.findUnit(id);
      if (u.getMovementPoints() < (MOVE_COST * shortestPathLength)) {
        printMessageToTerminal("Insufficient movement points!");
        allCanMove = false;
      }
    }
    if (allCanMove == true) {
      for (int id2 : ids) {
        Unit u2 = src.findUnit(id2);
        u2.minusMovementPoints(MOVE_COST * shortestPathLength);     // MOVE FUNCTION NOT IMPLEMENTED
        src.moveUnit(dest, id2);
      }
      return true;
    }
    return false;
  }

  /**
   * Recruit menu requests training to check sufficient wealth and space in province.
   * @param unitType
   * @param numTroops
   * @throws IOException
   */
  public void requestTraining(String unitType, int numTroops) throws IOException {

    if (currentlySelectedHumanProvince == null) {
      printMessageToTerminal("Please select a province before recruiting!");
      return;
    }
    Province humanProvince = deserializeProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
    int cost = getCostOfUnit(unitType) * numTroops;
    if (getPlayerFromID(currentPlayerID).getGold() < cost) {
      printMessageToTerminal("Insufficient gold!");
    } else {
      unitTrainRequest(humanProvince, unitType, numTroops);
    }
  }

  private int getCostOfUnit(String unitType) {
    JSONObject config = new JSONObject(unitConfig);
    return config.getJSONObject(unitType).optInt("cost");
  }

  private void unitTrainRequest(Province p, String unitType, int numTroops) throws IOException {
    boolean requestSuccess = p.trainUnit(unitType, numTroops);  // Charges the player for cost of production.
    if (!requestSuccess) {
      printMessageToTerminal("Province has no open training slots!");
    } else {
      updateFrontendText();
      printMessageToTerminal(p.getName() + " is now recruiting " + numTroops + " " + unitType + "'s.'");
    }
  }

  public void setStartScreen(StartScreen startScreen) {
    this.startScreen = startScreen;
  }

  public void setRecruitScreen(RecruitScreen recruitScreen) {
    this.recruitScreen = recruitScreen;
  }

  public void setInvadeScreen(InvadeScreen invadeScreen) {
    this.invadeScreen = invadeScreen;
  }

  @FXML
  public void clickedStartMenu(ActionEvent e) {
    startScreen.start();
  }

  @FXML
  public void clickedMove(ActionEvent e) {
    
    if (currentlySelectedHumanProvince == null) {
      printMessageToTerminal("Please select a province to move from!");
      return;
    }    
    String provinceName = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    Province province = deserializeProvince(provinceName);
    currentlySelectedHumanProvinceUnits = province.getUnits();

    if (checkProvinceInvaded(province)) { 
      printMessageToTerminal("Units can not move after invading.");
      return;
    }

    if (province.getUnits() == null || province.getArmySize() == 0) {
      printMessageToTerminal(provinceName + " has no troops.");
      return;
    }
    printMessageToTerminal(provinceName + " with an army size of " + province.getArmySize() + " has been selected for movement.");
    
    waitingForDestination = province;
  }

  private boolean checkProvinceInvaded(Province province) {
    return province.invadedThisTurn();
  }

  @FXML
  public void clickedRecruit(ActionEvent e) {
    recruitScreen.start();
  }

  @FXML
  public void clickedLowTax(ActionEvent e) {
    setTaxRate("low");
  }

  @FXML
  public void clickedNormalTax(ActionEvent e) {
    setTaxRate("normal");
  }

  @FXML
  public void clickedHighTax(ActionEvent e) {
    setTaxRate("high");
  }

  @FXML
  public void clickedVeryHighTax(ActionEvent e) {
    setTaxRate("vhigh");
  }

  private void setTaxRate(String taxRate) {
    printMessageToTerminal("Tax Rate for " + getPlayerFromID(currentPlayerID).getFaction() + " set to " + taxRate + ".");
    for (Province p : provinces) {
      if (p.getPlayer().getID() == currentPlayerID) {
        p.changeTaxRate(taxRate);
      }
    }
  }

  @FXML
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null) {
      Province humanProvince = deserializeProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
      currentlySelectedHumanProvinceUnits = humanProvince.getUnits();

      // Some tests. We cannot have an empty army
      if (currentlySelectedHumanProvinceUnits.size() == 0) {
        printMessageToTerminal("No soldiers, cannot invade!");
        return;
      }

      invadeScreen.showInvadeButton();
      invadeScreen.hideMoveButton();
      invadeScreen.start(currentlySelectedHumanProvinceUnits);
    }
  }

  public void invade(ArrayList<Integer> ids) throws IOException {
    
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null) {
      Province humanProvince = deserializeProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
      Province enemyProvince = deserializeProvince((String)currentlySelectedEnemyProvince.getAttributes().get("name"));
      
      if (hasAlreadyInvaded(ids, humanProvince)) {
        printMessageToTerminal("Soldiers have already invaded this turn!");
        return;
      }
        
      Ability.setProvinces(provinces);
      
      ArrayList<Unit> invadingList = getInvadingList(ids, humanProvince);
      
      Result battleResult = new Result();

      // Provinces should be connected
      if (!confirmIfProvincesConnected(humanProvince.getName(), enemyProvince.getName())) {
        printMessageToTerminal("Provinces not adjacent, cannot invade!");
        battleResult.setNotStarted();
      }
      // If the enemy province is empty, automatic victory
      if (enemyProvince.getUnits().size() == 0) {
        battleResult.setVictory();
      }

      battle(battleResult, invadingList, enemyProvince, humanProvince);

      resetSelections();  // reset selections in UI
      addAllPointGraphics(); // reset graphics
    }
  }

  private boolean hasAlreadyInvaded(ArrayList<Integer> ids, Province humanProvince) {
    for (Unit u : humanProvince.getUnits()) {
      for (int id : ids) {
        if (u.getID() == id && u.hasInvaded()) {
          return true;
        }
      }
    }
    return false;
  }

  private void battle(Result battleResult, ArrayList<Unit> invadingList, Province enemyProvince,
      Province humanProvince) {
    // Starting the battle
    int engagementIndex = 0;

    initiateAbilities(invadingList, enemyProvince, humanProvince); 

    ArrayList<Unit> routedList = new ArrayList<Unit>();

    while (battleResult.getResult().equals("")) {
      // We take away these troops from the humanProvince as the battle has started
      humanProvince.getUnits().removeAll(invadingList);

      // Random units from each side are chosen
      Unit human;
      Random r = new Random();
      if(invadingList.size() > 1) {
        human = invadingList.get(r.nextInt(invadingList.size()));
      } else {
        human = invadingList.get(0);
      }
      
      Unit enemy;
      if (enemyProvince.getUnits().size() > 1) {
        enemy = enemyProvince.getUnits().get(r.nextInt(enemyProvince.getUnits().size()));
      } else {
        enemy = enemyProvince.getUnits().get(0);
      }

      initiateSkirmishAbilities(human, enemy);
      
      Skirmish s = new Skirmish(human, enemy, engagementIndex, invadingList);
      
      // If both units are melee units, there is a 100% chance of a melee engagment.
      if (human.getRange().equals("melee") && enemy.getRange().equals("melee")) {
        s.start("melee");
      } 
      
      // If both units are ranged units, there is a 100% chance of a ranged engagement.
      else if (human.getRange().equals("ranged") && enemy.getRange().equals("ranged")) {
        s.start("ranged");
      }

      // If they are both melee and ranged, the chances are calculated.
      else {
        s.start(getEngagementType(human, enemy));
      }

      restoreSkirmishAbilities(human, enemy);

      printSkirmishResult(s, humanProvince.getFaction(), enemyProvince.getFaction());

      // Skirmish should have finished. we check the result of the skirmish.
      battleResult = checkSkirmishResult(s, enemyProvince, enemy, invadingList, human, battleResult, routedList);

      engagementIndex = s.getEngagementIndex();
    }

    // Battle has been finished.
    restoreAbilities(invadingList, enemyProvince.getUnits());

    switch(battleResult.getResult()) {
      case "victory":
        printMessageToTerminal("The " + humanProvince.getFaction() + " won the battle!");
        // Setting the invaded province as the winner's faction
        enemyProvince.setPlayer(humanProvince.getPlayer());
        
        // Moving leftover armies to the invaded province
        enemyProvince.getUnits().addAll(invadingList);

        // Moving the routed armies to the invaded province
        enemyProvince.getUnits().addAll(routedList);

        // If this province has been recaptured by human, we restore the morale penalty
        Ability.checkLERecapture(humanProvince);

        // All soldiers are tagged as having invaded this turn
        for (Unit u : enemyProvince.getUnits()) {
          u.setHasInvaded(true);
        }
        enemyProvince.setInvadedThisTurn(true);
        enemyProvince.stopUnitProduction();

        break;
      case "defeat":
        printMessageToTerminal("The " + enemyProvince.getFaction() + " won the battle.");
        
        // Moving the routed armies back to the human province
        humanProvince.getUnits().addAll(routedList);
        break;
      case "draw":
        printMessageToTerminal("The battle was a draw.");

        // We move the army back to our human province.
        humanProvince.getUnits().addAll(invadingList);
        break;
      case "routed":
        printMessageToTerminal("The " + humanProvince.getFaction() + " succesfully routed!");
          
        // Moving the routed armies back to the human province
        humanProvince.getUnits().addAll(routedList);
    }
  }

  private void printSkirmishResult(Skirmish s, String humanFaction, String enemyFaction) {
    if (displayEngagement) {
      for (Engagement e: s.getEngagements()) {
        printMessageToTerminal(humanFaction + " " + s.getHumanType() + " has defeated " + e.getEnemyCasualty() + " " + enemyFaction + " " + s.getEnemyType());
        printMessageToTerminal(enemyFaction + " " + s.getEnemyType() + " has defeated " + e.getHumanCasualty() + " " + enemyFaction + " " + s.getHumanType());
      }
    }
  }

  private Result checkSkirmishResult(Skirmish s, Province enemyProvince, Unit enemy, ArrayList<Unit> humanArmies, Unit human, Result battleResult, ArrayList<Unit> humanRoutedArmies) {
    switch(s.getResult()) {
      case "victory":
        enemyProvince.getUnits().remove(enemy);
        if (enemyProvince.getUnits().size() == 0) {
          // Adding morale penalty of the enemy
          Ability.processLegionaryEagleDeath(enemy, s.getEnemyInitialNumTroops(), enemyProvince);
          battleResult.setVictory();
        }
        break;  
      case "defeat":
      // We remove this losing unit from our armies list.
        humanArmies.remove(human);
        // We also check that we still have other units to continue the battle.
        if (humanArmies.size() == 0) {
          battleResult.setDefeat();
        }

        break;
      case "human routed":
        humanArmies.remove(human);
        humanRoutedArmies.add(human);
        
        if (humanArmies.size() == 0) {
          battleResult.setRouted();
        } 
        break;
      case "enemy routed":
        enemyProvince.getUnits().remove(enemy);
        if (enemyProvince.getUnits().size() == 0) {
          // Adding morale penalty of the enemy
          Ability.processLegionaryEagleDeath(enemy, s.getEnemyInitialNumTroops(), enemyProvince);
          battleResult.setVictory();
        }
    }
    return battleResult;
  }

  private void initiateAbilities(ArrayList<Unit> invadingList, Province enemyProvince, Province humanProvince) {
    Ability.initiate(invadingList, enemyProvince.getUnits());
  } 

  private void restoreAbilities(ArrayList<Unit> invadingList, ArrayList<Unit> defendingList) {
    Ability.restore(invadingList);
    Ability.restore(defendingList);
  }

  private void initiateSkirmishAbilities(Unit human, Unit enemy) {
    Ability.processSkirmishAbility(human, enemy);
    Ability.processSkirmishAbility(enemy, human);
  
  }

  private void restoreSkirmishAbilities(Unit human, Unit enemy) {
    Ability.restoreSkirmishAbility(human, enemy);
    Ability.restoreSkirmishAbility(enemy, human);
  }

  @FXML
  public void clickedEndTurnButton() throws IOException {
    printMessageToTerminal("Player " + currentPlayerID + " ended their turn.");

    // Reloading the save doesn't continue prompts.
    if (!hasWon) { 
      processVictories();
    }

    // Reset hasInvaded variables.
    resetHasInvaded();

    // Check for factions with no provinces.
    checkLostFactions();

    currentPlayerID++;
    
    // All players had their turn, goes back to player 1.
    if (currentPlayerID > players.size()) {
      currentPlayerID = 1;
      currentYear++;
      
      adjustProvincesTownWealth();
      for (Province p : provinces) {
        p.collectTaxRevenue();
      }
      
    }
    resetMovementPoints();

    // Collect taxes for the next player
    // Trained units are available at the beginning of the players' next turn
    for (Province p : provinces) {
      if (p.getPlayer().getID() == currentPlayerID) {
        if (p.nextTurn()) {
          printMessageToTerminal(p.getName() + " has just recruited a new unit!");
        }
      }
    }

    updateFrontendText();
    addAllPointGraphics();
    printMessageToTerminal("It is Player " + currentPlayerID + "'s turn.");
  }

  private void resetHasInvaded() {
    for (Province p : provinces) {
      p.setInvadedThisTurn(false);
      for (Unit u : p.getUnits()) {
        u.setHasInvaded(false);
      }
    }
  }

  private void checkLostFactions() {
    boolean hasLost;
    for (Player p : players) {
      hasLost = true;
      for (Province prov : provinces) {
        if (prov.getFaction().equals(p.getFaction())) {
          hasLost = false;
        }
      }
      if (hasLost) {
        printMessageToTerminal(p.getFaction() + " has lost all their provinces. GG!");
      }
    }
  }

  private void processVictories() throws IOException {
    switch (detectVictory()) {      
      case CONQUEST_VICTORY:
      saveGame();
      printMessageToTerminal("Player " + currentPlayerID + " has achieved Conquest Victory!");
      break;
      case TREASURY_VICTORY:
      saveGame();
      printMessageToTerminal("Player " + currentPlayerID + " has achieved Treasury Victory!");
      break;
      case WEALTH_VICTORY:
      saveGame();
      printMessageToTerminal("Player " + currentPlayerID + " has achieved Wealth Victory!");
    }
  }

  private String getEngagementType(Unit human, Unit enemy) {
    // we check who's the melee and who's ranged
    Unit meleeUnit = findMeleeUnit(human, enemy);
    Unit rangedUnit = findRangedUnit(human, enemy);

    double meleeEngagementChance = findMeleeEngagementChance(meleeUnit.getSpeed(), rangedUnit.getSpeed());
    Random r = new Random();
    if(r.nextDouble() <= meleeEngagementChance) {
      return "melee";
    } else {
      return "ranged";
    }
  }

  private Unit findMeleeUnit(Unit a, Unit b) {
    if (a.getRange().equals("melee")) {
      return a;
    }
    return b;
  }

  private Unit findRangedUnit(Unit a, Unit b) {
    if (a.getRange().equals("melee")) {
      return b;
    }
    return a;
  }

  private int detectVictory() {
    hasWon = true;
    if (detectConquestVict()) { return CONQUEST_VICTORY; }
    if (detectTreasuryVict()) { return TREASURY_VICTORY; }
    if (detectWealthVict()) { return WEALTH_VICTORY; }
    hasWon = false;
    return 0;
  }

  private boolean detectWealthVict() {
    Player currentPlayer = getPlayerFromID(currentPlayerID);
    if (getTotalWealth(currentPlayer) >= WEALTH_GOAL) { return true; }
    return false;
  }

  private int getTotalWealth(Player currentPlayer) {
    int totalWealth = 0;
    for (Province p : provinces) {
      if (p.getPlayer().equals(currentPlayer)) {
        totalWealth += p.getWealth();
      }
    }
    return totalWealth;
  }

  private boolean detectTreasuryVict() {
    return (getPlayerFromID(currentPlayerID).getGold() >= TREASURY_GOAL);
  }

  private boolean detectConquestVict() {
    boolean ownsAllProvinces = true;
    for (Province p : provinces) {
      if (p.getPlayer().getID() != currentPlayerID) {
        ownsAllProvinces = false;
      }
    }
    return ownsAllProvinces;
  }

  private Player getPlayerFromID(int currentPlayerID) {
    for (Player p : players) {
      if (p.getID() == currentPlayerID) {
        return p;
      }
    }
    return null;
  }

  private void resetMovementPoints() {
    for (Province p : provinces) {
      p.resetMovePoints();
    }
  }

  private void adjustProvincesTownWealth() {
    for (Province p : provinces) {
      p.adjustTownWealth();
    }
  }

  @FXML
  public void clickedSaveButton(ActionEvent e) throws IOException {
    filename = saveFilename.getText();
    if (filename == "") {
      filename = DEFAULT_FILENAME;
    }
    saveGame();
    printMessageToTerminal("Game is saved!");
  }

  private void saveGame() throws IOException {

    // Things to save: data in the province class
    JSONArray provinceList = new JSONArray();
    for (Province p : provinces) {
      // Using a mapper to parse a java instance to a json
      ObjectMapper mapper = new ObjectMapper();
      String jsonString = mapper.writeValueAsString(p);
      JSONObject joProvince = new JSONObject(jsonString);

      // remove unneeded data
      joProvince.remove("armySize");
      joProvince.remove("faction");
      joProvince.remove("armyStrength");

      // Adding the JSONObject to the JSONArray
      provinceList.put(joProvince);
    }
    String content = provinceList.toString(2);
    Files.writeString(Paths.get("src/unsw/gloriaromanus/saves/" + filename + "_province.json"), content);

    JSONObject campaignData = new JSONObject();
    // The saved status
    campaignData.put("status", "saved");
    // Who's turn it is
    campaignData.put("currentPlayerID", currentPlayerID);
    // What year it is (How many turns have passed)
    campaignData.put("currentYear", currentYear);
    // Whether the game victory has already been achieved
    campaignData.put("hasWon", hasWon);
    
    content = campaignData.toString(2);
    Files.writeString(Paths.get("src/unsw/gloriaromanus/saves/" + filename + "_campaign.json"), content);
  }

  private double findMeleeEngagementChance(double meleeSpeed, double rangedSpeed) {
    // Base level is 50%
    double chance = 0.5;

    // melee engagement chance is increased by 10% x (speed of melee unit - speed of ranged unit)
    chance += (0.1 * (meleeSpeed - rangedSpeed));

    // Max percentage is 95% for both engagements
    if (chance > 0.95) {
      chance = 0.95;
    } else if (chance < 0.05) {
      chance = 0.05;
    }

    return chance;
  }

  private Province deserializeProvince(String provinceName) {
    for (Province p : provinces) {
      if (p.getName().equals(provinceName)) {
        return p;
      }
    }
    return null;
  }

  private void restoreSavedDetails() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/saves/" + filename + "_campaign.json"));
    JSONObject jo = new JSONObject(content);
    currentPlayerID = jo.getInt("currentPlayerID");
    currentYear = jo.getInt("currentYear");
    hasWon = jo.getBoolean("hasWon");

    content = Files.readString(Paths.get("src/unsw/gloriaromanus/saves/" + filename + "_province.json"));
    JSONArray jaProvince = new JSONArray(content);
    for (int i = 0; i < jaProvince.length(); i++) {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = jaProvince.getJSONObject(i).toString();
      Province newProvince =  objectMapper.readValue(jsonString, Province.class);
      provinces.add(newProvince);
    }

    for (Province province : provinces) {
      Player player = findPlayer(province.getFaction());
      if (player == null) {
        players.add(province.getPlayer());
        for (Unit u : province.getUnits()) {
          u.setPlayer(province.getPlayer());
        }
      } else {
        province.setPlayer(player);
        for (Unit u : province.getUnits()) {
          u.setPlayer(player);
        } 
      }
    }
  }

  public Player findPlayer(String faction) {
    for (Player p : players) {
      if (p.getFaction().equals(faction)) {
        return p;
      }
    } return null;
  }

  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  } 

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String provinceName = (String) f.getProperty("name");
        Province province = deserializeProvince(provinceName);
        String faction = province.getFaction();

        TextSymbol t = new TextSymbol(10,
            faction + "\n" + provinceName + "\nArmy size: " + province.getArmySize() + "\nWealth: " + province.getWealth(), 0xFFFF0000,
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        s = new PictureMarkerSymbol("images/legionary.png"); // TODO: Import other images
        
        switch (faction) {
          case "Gaul":
            // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image https://stackoverflow.com/a/30970114

            // you can pass in a filename to create a PictureMarkerSymbol...
            s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
            break;
          case "Rome":
            // you can also pass in a javafx Image to create a PictureMarkerSymbol (different to BufferedImage)
            s = new PictureMarkerSymbol("images/legionary.png");
            break;
          // TODO = handle all faction names, and find a better structure...
        }
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String provinceName = (String)f.getAttributes().get("name");
                Province province = deserializeProvince(provinceName);

                if (province.getFaction().equals(getPlayerFromID(currentPlayerID).getFaction())){
                  // province owned by human
                  if (currentlySelectedHumanProvince != null){
                    featureLayer.unselectFeature(currentlySelectedHumanProvince);
                  }
                  currentlySelectedHumanProvince = f;
                  invading_province.setText(provinceName);
                  if (waitingForDestination != null) {
                    invadeScreen.hideInvadeButton();
                    invadeScreen.showMoveButton();
                    invadeScreen.start(currentlySelectedHumanProvinceUnits);
                    destination = province;
                  }
                }
                else{
                  if (currentlySelectedEnemyProvince != null){
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  }
                  currentlySelectedEnemyProvince = f;
                  opponent_province.setText(provinceName);
                }

                featureLayer.selectFeature(f);
              }             
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  /**
   * 
   * @throws IOException
   */
  public void processMove(ArrayList<Integer> moveUnitIDs) throws IOException {
    Province origin = waitingForDestination;

    if (checkProvinceInvaded(destination)) {
      printMessageToTerminal("Units can not move to a recently invaded province!");
      return;
    }

    if (origin.getName().equals(destination.getName())) {
      printMessageToTerminal("Can not move to the same province.");
      return;
    }

    if (moveUnits(moveUnitIDs, origin, destination)) {
      printMessageToTerminal("Units successfully moved!");
    }

    waitingForDestination = null;
    addAllPointGraphics(); // reset graphics
  }

  private void initializeOfflineMultiOwnership() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/provinces_list.json"));
    JSONObject ownership = new JSONObject(content);
    JSONArray ja = ownership.getJSONArray("provinces_list");
    Random r = new Random();

    for (int i = 0; i < players.size(); i++) {
      int randNum = r.nextInt(ja.length());
      provinces.add(new Province(ja.getString(randNum), players.get(i), unitConfig));
      ja.remove(randNum);
    }

    for (int i = 0; i < ja.length(); i++) {
      int randNum = r.nextInt(players.size());
      provinces.add(new Province(ja.getString(i), players.get(randNum), unitConfig));
    }
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(getPlayerFromID(currentPlayerID).getFaction()));
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery() throws IOException {
    LinkedList<String> l = new LinkedList<String>();
    for (String hp : getHumanProvincesList()) {
      l.add("name='" + hp + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    JSONObject provinceAdjacencyMatrix = new JSONObject(content);
    return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
  }

  private void resetSelections(){
    featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedHumanProvince));
    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
    invading_province.setText("");
    opponent_province.setText("");
  }

  public void printMessageToTerminal(String message){
    output_terminal.appendText(message+"\n");
  }

  public void setAudio(Audio audio) {
    this.audio = audio;
    volumeSlider.setValue(Audio.getDefaultVol());
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }

  public int getFactionsSize() {
    return factions.size();
  }

  public void setFactions(ArrayList<String> factions) {
    this.factions = factions;
  }

  private ArrayList<Unit> getInvadingList(ArrayList<Integer> ids, Province p) {
    ArrayList<Unit> units = new ArrayList<Unit>();
    for (int id : ids) {
      for (Unit u : p.getUnits()) {
        if (u.getID() == id) {
          units.add(u);
        }
      }
    }
    return units;
  }
}
