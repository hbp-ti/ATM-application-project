package PTDA_ATM;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TheStateTest {

    @Test
    @DisplayName("Create State - Correct Value")
    void createStateCorrectValue() {
        double expectedValue = 123.45;
        TheState state = new TheState(expectedValue);
        assertEquals(expectedValue, state.getValue(), "State should have the correct value.");
    }

    @Test
    @DisplayName("State Value - Minimum Allowed")
    void stateValueMinimumAllowed() {
        double minValue = Double.MIN_VALUE;
        TheState state = new TheState(minValue);
        assertEquals(minValue, state.getValue(), "State should accept the minimum allowed value.");
    }

    @Test
    @DisplayName("State Value - Maximum Allowed")
    void stateValueMaximumAllowed() {
        double maxValue = Double.MAX_VALUE;
        TheState state = new TheState(maxValue);
        assertEquals(maxValue, state.getValue(), "State should accept the maximum allowed value.");
    }

    @Test
    @DisplayName("State Value - Zero Value")
    void stateValueZero() {
        double zeroValue = 0.0;
        TheState state = new TheState(zeroValue);
        assertEquals(zeroValue, state.getValue(), "State should accept a zero value.");
    }
}
