package test;

import java.io.IOException;
import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.DijkstraAlgorithm;

public class DijkstraAlgorithmTest {
    @Test
    private void algorithmTest() throws IOException {
        assert(DijkstraAlgorithm.findShortestPathLength("Britannia", "Lugdunensis") == 1);
        assert(DijkstraAlgorithm.findShortestPathLength("Britannia", "Belgica") == 2);
    }
}