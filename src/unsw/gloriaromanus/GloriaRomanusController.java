package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
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
  private TextField invading_province;
  @FXML
  private TextField opponent_province;
  @FXML
  private TextArea output_terminal;

  private ArcGISMap map;

  private static final int MOVE_COST = 4;

  private ArrayList<Player> players;
  private int currentPlayerID;
  private int currentYear;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;

  private FeatureLayer featureLayer_provinces;

  private String unitConfig;
  private ArrayList<Province> provinces;

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException {
    
    readConfig();
    provinces = new ArrayList<Province>();
    players = new ArrayList<Player>();
    
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/saves/campaignData.json"));
    JSONObject j = new JSONObject(content);
    if (j.getString("status").equals("saved")) {
      // restore saved details
      restoreSavedDetails();
    } else {
      // initialise with new details
      initializeOwnership();
      Random r = new Random();
      for (Province p: provinces) {
        p.setInitialArmy(r.nextInt(500));
      }
    }

    currentPlayerID = 0;
    currentYear = 0;

    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

    initializeProvinceLayers();    
  }

  private void readConfig() throws IOException {
    unitConfig = (Files.readString(Paths.get("src/unsw/gloriaromanus/unit_config.json")));
  }

  public boolean moveUnits(List<Integer> ids, Province src, Province dest) throws IOException {
    int shortestPathLength = DijkstraAlgorithm.findShortestPathLength(src.getName(), dest.getName());
    boolean allCanMove = true;
    for (int id : ids) {
      Unit u = src.findUnit(id);
      if (u.getMovementPoints() < (MOVE_COST * shortestPathLength)) {
        allCanMove = false;
      }
    }
    if (allCanMove == true) {
      for (int id2 : ids) {
        Unit u2 = src.findUnit(id2);
        u2.minusMovementPoints(MOVE_COST * shortestPathLength);
      }
      return true;
    }
    return false;
  }

  public boolean unitTrainRequest(Province p, String unitType, int numTroops) throws IOException {   
    boolean requestSuccess = p.trainUnit(unitType, numTroops);
    if (!requestSuccess) {
      printMessageToTerminal("Province has no open training slots!");
    }
    return requestSuccess;
  }

  @FXML
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      Province humanProvince = deserializeProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
      Province enemyProvince = deserializeProvince((String)currentlySelectedEnemyProvince.getAttributes().get("name"));

      if (humanProvince == null || enemyProvince == null) {
        // throw some kind of exception
      }

      for (Unit u : humanProvince.getUnits()) {
        if (u.getMovementPoints() < MOVE_COST) {
          printMessageToTerminal("Troops have insufficient movement points!");
          return;
        }
      }

      Ability.setProvinces(provinces);
      Ability.process();
      Ability.processHeroicCharge(humanProvince, enemyProvince);

      if (humanProvince.getArmySize() > 0) {
        if (confirmIfProvincesConnected(humanProvince.getName(), enemyProvince.getName())){
          // Each army has a uniformly random chance of winning calculated as: army strength/(army strength + enemy army strength)
          double humanWinningChance = humanProvince.getArmyStrength() / (double)(humanProvince.getArmyStrength() + enemyProvince.getArmyStrength());
          double enemyWinningChance = enemyProvince.getArmyStrength() / (double)(humanProvince.getArmyStrength() + enemyProvince.getArmyStrength());
  
          Random r = new Random();
          double choice = r.nextDouble();
  
          if (choice <= humanWinningChance){
            // human won. The casulties are randomly generated according to their winning chances.
            winningArmyCasulties(humanProvince, enemyWinningChance);
            losingArmyCasulties(enemyProvince, humanWinningChance);
  
            // Transfer 40% of the remaining troops of human over to the new province.
            int numTroopsToTransfer = humanProvince.getArmySize()*2/5;
            // Assumption: the remaining troops of the enemy province gets converted to armies of the invading faction.
            changeArmySize(enemyProvince, numTroopsToTransfer);
            changeArmySize(humanProvince, -numTroopsToTransfer);

            enemyProvince.setFaction(players.get(currentPlayerID).getFaction());
            enemyProvince.stopUnitProduction();

            printMessageToTerminal("Won battle!");

          }
          else{
            // enemy won.
            winningArmyCasulties(enemyProvince, humanWinningChance);
            losingArmyCasulties(humanProvince, enemyWinningChance);
            printMessageToTerminal("Lost battle!");
          }
          humanProvince.setMovePoints(0);
          enemyProvince.setMovePoints(0);
          resetSelections();  // reset selections in UI
          addAllPointGraphics(); // reset graphics
          // TODO: For a pass mark, Player must be able to move troops between adjacent regions 1 turn at a time. This condition may change but 
          // we implement it for now.
          clickedEndTurnButton();
        }
        else{
          printMessageToTerminal("Provinces not adjacent, cannot invade!");
        }
      } else {
        printMessageToTerminal("Provinces has no soldiers, cannot invade!");
      }
      
    }
  }

  @FXML
  public void clickedEndTurnButton() throws IOException {
    printMessageToTerminal("player" + currentPlayerID + " ended their turn.");
    currentPlayerID++;
    if (currentPlayerID == players.size()) {
      currentPlayerID = 0;
      currentYear++;
    }
    resetMovementPoints();
    adjustProvincesTownWealth();
    printMessageToTerminal("It is player" + currentPlayerID + "'s turn.");

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
    String content = provinceList.toString();
    Files.writeString(Paths.get("src/unsw/gloriaromanus/saves/provinceData.json"), content);

    JSONObject campaignData = new JSONObject();
    // The saved status
    campaignData.put("status", "saved");
    // Who's turn it is
    campaignData.put("currentPlayerID", currentPlayerID);
    // What year it is (How many turns have passed)
    campaignData.put("currentYear", currentYear);
    
    content = campaignData.toString();
    Files.writeString(Paths.get("src/unsw/gloriaromanus/saves/campaignData.json"), content);

    printMessageToTerminal("Game is saved!");
  }

  private void losingArmyCasulties(Province province, double enemyWinningChance) {
    Random r = new Random();
    double casultyPercentage = enemyWinningChance + (1 - enemyWinningChance) * r.nextDouble();
    Double casultySize = province.getArmySize() * casultyPercentage;
    changeArmySize(province, -(casultySize.intValue()));
  }

  private void winningArmyCasulties(Province province, double enemyWinningChance) {
    Random r = new Random();
    double casultyPercentage = enemyWinningChance * r.nextDouble();
    Double casultySize = province.getArmySize() * casultyPercentage;
    changeArmySize(province, -(casultySize.intValue()));
  }

  private void changeArmySize(Province province, int changeSize) {
    int remainingArmySize = province.getArmySize() + changeSize;
    if (remainingArmySize < 0) { remainingArmySize = 0; }
    province.setArmySize(remainingArmySize);
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
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/saves/campaignData.json"));
    JSONObject jo = new JSONObject(content);
    currentPlayerID = jo.getInt("currentPlayerID");
    currentYear = jo.getInt("currentYear");

    content = Files.readString(Paths.get("src/unsw/gloriaromanus/saves/provinceData.json"));
    JSONArray jaProvince = new JSONArray(content);
    for (int i = 0; i < jaProvince.length(); i++) {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = jaProvince.getJSONObject(i).toString();
      Province newProvince =  objectMapper.readValue(jsonString, Province.class);
      provinces.add(newProvince);
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
            faction + "\n" + provinceName + "\n" + province.getArmySize() + "\n" + province.getWealth(), 0xFFFF0000,
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

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

                if (province.getFaction().equals(players.get(currentPlayerID).getFaction())){
                  // province owned by human
                  if (currentlySelectedHumanProvince != null){
                    featureLayer.unselectFeature(currentlySelectedHumanProvince);
                  }
                  currentlySelectedHumanProvince = f;
                  invading_province.setText(provinceName);
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

  private void initializeOwnership() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    for (String faction : ownership.keySet()) {
      Player p = new Player(players.size(), faction);
      players.add(p);
      JSONArray ja = ownership.getJSONArray(faction);
      for (int i = 0; i < ja.length(); i++) {
        String province = ja.getString(i);
        provinces.add(new Province(province, p, unitConfig));
      }
    }
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(players.get(currentPlayerID).getFaction()));
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

  private void printMessageToTerminal(String message){
    output_terminal.appendText(message+"\n");
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }
}
