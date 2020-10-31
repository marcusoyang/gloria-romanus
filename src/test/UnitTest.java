package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.Unit;
import unsw.gloriaromanus.UnitFactory;

public class UnitTest {
    private String config = "{\r\n    \"legionary\": {\r\n        \"meleeAttack\": 10,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 10,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"melee\",\r\n        \"type\": \"heavy infantry\",\r\n        \"ability\": \"Legionary Eagle\",\r\n        \"turnsToProduce\": 1\r\n    },\r\n    \"berserker\": {\r\n        \"meleeAttack\": 10,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"melee\",\r\n        \"type\": \"heavy infantry\",\r\n        \"ability\": \"Berserker Rage\",\r\n        \"turnsToProduce\": 1\r\n    },\r\n    \"pikemen\": {\r\n        \"meleeAttack\": 10,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"melee\",\r\n        \"type\": \"spearmen\",\r\n        \"ability\": \"Phalanx\",\r\n        \"turnsToProduce\": 1\r\n    },\r\n    \"hoplite\": {\r\n        \"meleeAttack\": 10,\r\n        \"rangedAttack\": 8,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 10,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"ranged\",\r\n        \"type\": \"heavy infantry\",\r\n        \"ability\": \"Phalanx\",\r\n        \"turnsToProduce\": 1\r\n    },\r\n    \"javelin-skirmisher\": {\r\n        \"meleeAttack\": 8,\r\n        \"rangedAttack\": 10,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 10,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"ranged\",\r\n        \"type\": \"missile infantry\",\r\n        \"ability\": \"Skirmisher Anti-Armour\",\r\n        \"turnsToProduce\": 1\r\n    },\r\n    \"elephant\": {\r\n        \"meleeAttack\": 10,\r\n        \"defense\": 12,\r\n        \"armour\": 10,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 12,\r\n        \"range\": \"melee\",\r\n        \"type\": \"cavalry\",\r\n        \"ability\": \"Elephant Amok\",\r\n        \"turnsToProduce\": 2\r\n    },\r\n    \"horse_archer\": {\r\n        \"meleeAttack\": 4,\r\n        \"rangedAttack\": 14,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 14,\r\n        \"range\": \"ranged\",\r\n        \"type\": \"horse archer\",\r\n        \"ability\": \"Cantabrian Circle\",\r\n        \"turnsToProduce\": 1\r\n    },\r\n    \"druid\": {\r\n        \"meleeAttack\": 4,\r\n        \"defense\": 10,\r\n        \"armour\": 6,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"melee\",\r\n        \"type\": \"infantry\",\r\n        \"ability\": \"Druidic Fervour\",\r\n        \"turnsToProduce\": 1\r\n    },\r\n    \"catapult\": {\r\n        \"meleeAttack\": 0,\r\n        \"rangedAttack\": 24,\r\n        \"defense\": 19,\r\n        \"armour\": 10,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 4,\r\n        \"range\": \"ranged\",\r\n        \"type\": \"artillery\",\r\n        \"ability\": \"\",\r\n        \"turnsToProduce\": 1\r\n    }\r\n}";

    @Test
    public void numUnitTest() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        Unit u = myFac.newUnit("legionary", 50);
        assertEquals(u.getNumTroops(), 50);
    }

    @Test
    public void unitTypeTest() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        Unit u = myFac.newUnit("legionary", 50);
        assertEquals(u.getType(), "heavy infantry");
    }

    @Test
    public void unitAbilityTest() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        Unit u = myFac.newUnit("legionary", 50);
        assertEquals(u.getAbility(), "Legionary Eagle");
    }

    @Test
    public void unitElephantTest() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        Unit u = myFac.newUnit("elephant", 50);
        assertEquals(u.getAbility(), "Elephant Amok");
        assertEquals(u.getTurnsToProduce(), 2);
    }

    @Test
    public void noAbilityTest() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        Unit u = myFac.newUnit("catapult", 50);
        assertEquals(u.getAbility(), "");
    }
}

