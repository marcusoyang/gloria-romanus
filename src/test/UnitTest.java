package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.*;

public class UnitTest {
    private String config = "{\r\n    \"legionary\": {\r\n        \"meleeAttack\": 10,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 10,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"melee\",\r\n        \"type\": \"heavy infantry\",\r\n        \"ability\": \"Legionary Eagle\",\r\n        \"turnsToProduce\": 1,\r\n        \"cost\" : 10\r\n    },\r\n    \"berserker\": {\r\n        \"meleeAttack\": 10,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"melee\",\r\n        \"type\": \"heavy infantry\",\r\n        \"ability\": \"Berserker Rage\",\r\n        \"turnsToProduce\": 1,\r\n        \"cost\" : 10\r\n    },\r\n    \"pikemen\": {\r\n        \"meleeAttack\": 10,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"melee\",\r\n        \"type\": \"spearmen\",\r\n        \"ability\": \"Phalanx\",\r\n        \"turnsToProduce\": 1,\r\n        \"cost\" : 15\r\n    },\r\n    \"hoplite\": {\r\n        \"meleeAttack\": 10,\r\n        \"rangedAttack\": 8,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 10,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"ranged\",\r\n        \"type\": \"heavy infantry\",\r\n        \"ability\": \"Phalanx\",\r\n        \"turnsToProduce\": 1,\r\n        \"cost\" : 15\r\n    },\r\n    \"javelin-skirmisher\": {\r\n        \"meleeAttack\": 8,\r\n        \"rangedAttack\": 10,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 10,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"ranged\",\r\n        \"type\": \"missile infantry\",\r\n        \"ability\": \"Skirmisher Anti-Armour\",\r\n        \"turnsToProduce\": 1,\r\n        \"cost\" : 15\r\n    },\r\n    \"elephant\": {\r\n        \"meleeAttack\": 10,\r\n        \"defense\": 12,\r\n        \"armour\": 10,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 12,\r\n        \"range\": \"melee\",\r\n        \"type\": \"cavalry\",\r\n        \"ability\": \"Elephant Amok\",\r\n        \"turnsToProduce\": 2,\r\n        \"cost\" : 50\r\n    },\r\n    \"horse_archer\": {\r\n        \"meleeAttack\": 4,\r\n        \"rangedAttack\": 14,\r\n        \"defense\": 10,\r\n        \"armour\": 10,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 14,\r\n        \"range\": \"ranged\",\r\n        \"type\": \"horse archer\",\r\n        \"ability\": \"Cantabrian Circle\",\r\n        \"turnsToProduce\": 1,\r\n        \"cost\" : 25\r\n    },\r\n    \"druid\": {\r\n        \"meleeAttack\": 4,\r\n        \"defense\": 10,\r\n        \"armour\": 6,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"range\": \"melee\",\r\n        \"type\": \"infantry\",\r\n        \"ability\": \"Druidic Fervour\",\r\n        \"turnsToProduce\": 1,\r\n        \"cost\" : 15\r\n    },\r\n    \"catapult\": {\r\n        \"meleeAttack\": 0,\r\n        \"rangedAttack\": 24,\r\n        \"defense\": 19,\r\n        \"armour\": 10,\r\n        \"shield\": 0,\r\n        \"morale\": 10,\r\n        \"speed\": 4,\r\n        \"range\": \"ranged\",\r\n        \"type\": \"artillery\",\r\n        \"ability\": \"\",\r\n        \"turnsToProduce\": 1,\r\n        \"cost\" : 40\r\n    },\r\n    \"normal\": {\r\n        \"meleeAttack\": 1,\r\n        \"defense\" : 1,\r\n        \"morale\" : 10,\r\n        \"speed\" : 1,\r\n        \"range\" : \"melee\",\r\n        \"type\" : \"heavy infantry\",\r\n        \"ability\" : \"\"\r\n    }\r\n}";

    @Test
    public void unitIDTest() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        Unit.setCounter(0);
        Unit u1 = myFac.newUnit("catapult", 4);
        assertEquals(u1.getID(), 1);
        Unit u2 = myFac.newUnit("horse_archer", 8);
        assertEquals(u2.getID(), 2);
        Unit u3 = myFac.newUnit("druid", 12);
        assertEquals(u1.getID(), 1);
        assertEquals(u2.getID(), 2);
        assertEquals(u3.getID(), 3);
    }

    @Test
    public void basicNewUnitTest() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        Unit u = myFac.newUnit("legionary", 50);
        assertEquals(u.getNumTroops(), 50);
        assertEquals(u.getRange(), "melee");
        assertEquals(u.getAbility(), "Legionary Eagle");
    }

    @Test
    public void basicNewUnitTest2() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        Unit u = myFac.newUnit("catapult", 100);
        assertEquals(u.getShieldDefense(), 0);
        assertEquals(u.getType(), "artillery");
        assertEquals(u.getAbility(), "");
    }

    @Test
    public void trainingTest() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        myFac.addToTraining("elephant", 30);
        assertEquals(myFac.nextTrainingTurn(), null);
        assertEquals(myFac.nextTrainingTurn().getClass(), Unit.class);
        assertEquals(myFac.nextTrainingTurn(), null);
    }

    @Test
    public void trainingTest2() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        assertEquals(myFac.getIsTraining(), false);
        myFac.addToTraining("catapult", 30);
        assertEquals(myFac.getIsTraining(), true);
        assertEquals(myFac.nextTrainingTurn().getClass(), Unit.class);
        assertEquals(myFac.nextTrainingTurn(), null);
    }

    @Test
    public void trainingTest3() throws IOException {
        UnitFactory myFac = new UnitFactory(config);
        assertEquals(myFac.getIsTraining(), false);
        myFac.addToTraining("pikemen", 100);
        assertEquals(myFac.getIsTraining(), true);
        assertEquals(myFac.nextTrainingTurn().getClass(), Unit.class);
        assertEquals(myFac.nextTrainingTurn(), null);
    }

    
}

