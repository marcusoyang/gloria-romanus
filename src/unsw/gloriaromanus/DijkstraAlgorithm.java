package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;

// Code based on example code from: https://www.baeldung.com/java-dijkstra
public class DijkstraAlgorithm {
    private static Map<String, Integer> provinceDistances = new HashMap<String, Integer>();

    public static int findShortestPathLength(String src, String dest) throws IOException {
        provinceDistances.put(src, 0);
        
        ArrayList<String> settledProvinces = new ArrayList<String>();
        ArrayList<String> unsettledProvinces = new ArrayList<String>();

        unsettledProvinces.add(src);

        while (unsettledProvinces.size() != 0) {
            String currentProvince = getLowestDistanceProvince(unsettledProvinces);
            unsettledProvinces.remove(currentProvince);
            for (String adjacentProvince : getAdjacentProvinces(currentProvince)) {
                if (!settledProvinces.contains(adjacentProvince)) {
                    calculateMinimumDistance(adjacentProvince, currentProvince);
                    unsettledProvinces.add(adjacentProvince);
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

    private static String getLowestDistanceProvince(ArrayList<String> unsettledProvinces) {
        String lowestDistanceProvince = "";
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

    private static ArrayList<String> getAdjacentProvinces(String province) throws IOException {
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        
        JSONObject adjacencyObj = new JSONObject(content).getJSONObject(province);
        ArrayList<String> adjacentProvinceList = new ArrayList<String>();
        for (String p : adjacencyObj.keySet()) {
            if (adjacencyObj.getBoolean(p)) {
                adjacentProvinceList.add(p);
            }
        }
        return adjacentProvinceList;
    }

    private static void calculateMinimumDistance(String evaluationProvince, String sourceProvince) {
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