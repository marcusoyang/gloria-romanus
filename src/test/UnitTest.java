package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;

public class UnitTest {
    @Test
    public void blahTest() {
        assertEquals("a", "a");
    }

    @Test
    public void blahTest2() throws IOException {
        Unit u = new Unit("legionary", 50);
        assertEquals(u.getNumTroops(), 50);
    }

    @Test
    public void checkDirectory() {
        System.out.println(new File(".").getAbsolutePath());
    }

}

