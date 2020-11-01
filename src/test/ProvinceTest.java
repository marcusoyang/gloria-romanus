package test;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.Player;
import unsw.gloriaromanus.Province;

public class ProvinceTest {
    @Test
    private void TrainingCostTest() throws IOException {
        Player pl = new Player(0, "faction");
        String unitConfig = (Files.readString(Paths.get("src/unsw/gloriaromanus/unit_config.json")));
        Province pr = new Province("province", pl, unitConfig);
        pr.trainUnit("legionary", 100);
        assert(pl.getGold() == 99000);
    }
}