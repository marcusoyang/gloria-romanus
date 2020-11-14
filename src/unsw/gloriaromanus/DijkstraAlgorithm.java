package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;

// Code based on example code from: https://www.baeldung.com/java-dijkstra
public class DijkstraAlgorithm {
    public Map<String, Integer> provinceDistances;
    private String adjacencyMatrix;
    private ArrayList<Province> provinces;

    public DijkstraAlgorithm(String adjacencyMatrix, ArrayList<Province> provinces) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.provinces = provinces;
    }

    public int findShortestPathLength(String src, String dest) {
        provinceDistances = new HashMap<String, Integer>();
        provinceDistances.put(src, 0);
        
        ArrayList<String> settledProvinces = new ArrayList<String>();
        ArrayList<String> unsettledProvinces = new ArrayList<String>();

        unsettledProvinces.add(src);

        while (unsettledProvinces.size() != 0) {
            String currentProvince = getLowestDistanceProvince(unsettledProvinces);
            unsettledProvinces.remove(currentProvince);
            for (String adjacentProvince : getAdjacentProvinces(currentProvince)) {
                if (!settledProvinces.contains(adjacentProvince)) {
                    // The adjacent province is only added to the unsettledProvinces list
                    // if it is in the same faction as the player's or
                    // if it is the destination province.
                    if (checkSameFaction(src, adjacentProvince) || adjacentProvince.equals(dest)) {
                        calculateMinimumDistance(adjacentProvince, currentProvince);
                        unsettledProvinces.add(adjacentProvince);
                    }
                }
            }
            settledProvinces.add(currentProvince);
        }
        
        if (provinceDistances.containsKey(dest)) {
            return provinceDistances.get(dest);
        } else {
            return -1;
        }
    }

    private boolean checkSameFaction(String name1, String name2) {
        // Find Province instances of both province names
        Province province1 = deserialize(name1);
        Province province2 = deserialize(name2);
        
        return province1.getFaction().equals(province2.getFaction());
    }

    private Province deserialize(String name) {
        for (Province p: provinces) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    private String getLowestDistanceProvince(ArrayList<String> unsettledProvinces) {
        String lowestDistanceProvince = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (String province : unsettledProvinces) {
            int provinceDistance = Integer.MAX_VALUE;
            if (provinceDistances.containsKey(province)) {
                provinceDistance = provinceDistances.get(province);
            } 
            
            if (provinceDistance < lowestDistance) {
                lowestDistance = provinceDistance;
                lowestDistanceProvince = province;
            }
        }
        return lowestDistanceProvince;
    }

    private ArrayList<String> getAdjacentProvinces(String province) {
        JSONObject adjacencyObj = new JSONObject(adjacencyMatrix).getJSONObject(province);
        ArrayList<String> adjacentProvinceList = new ArrayList<String>();
        for (String p : adjacencyObj.keySet()) {
            if (adjacencyObj.getBoolean(p)) {
                adjacentProvinceList.add(p);
            }
        }
        return adjacentProvinceList;
    }

    private void calculateMinimumDistance(String evaluationProvince, String sourceProvince) {
        int sourceDistance = Integer.MAX_VALUE;
        if (provinceDistances.containsKey(sourceProvince)) {
            sourceDistance = provinceDistances.get(sourceProvince);
        }
        int evaluationDistance = Integer.MAX_VALUE;
        if (provinceDistances.containsKey(evaluationProvince)) {
            evaluationDistance = provinceDistances.get(evaluationProvince);
        }
        if (sourceDistance + 1 < evaluationDistance) {
            provinceDistances.put(evaluationProvince, sourceDistance + 1);
        }
    }

    
}