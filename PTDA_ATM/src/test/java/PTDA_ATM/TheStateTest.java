package PTDA_ATM;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TheStateTest {

    // Structural test
    @DisplayName("Test: Check State Object Creation - Verify the correct value")
    @Test
    public void testTheStateObject_CorrectValue() {
        double expectedValue = 123.45;
        TheState state = new TheState(expectedValue);
        assertEquals(expectedValue, state.getValue(), "The state object should have the correct value.");
    }

    // Boundary input partition test
    @DisplayName("Test: State Object Value - Minimum Value Allowed")
    @Test
    public void testTheStateObject_MinimumValue() {
        double minValue = Double.MIN_VALUE;
        TheState state = new TheState(minValue);
        assertEquals(minValue, state.getValue(), "The state object should accept the minimum allowed value.");
    }

    // Boundary input partition test
    @DisplayName("Test: State Object Value - Maximum Value Allowed")
    @Test
    public void testTheStateObject_MaximumValue() {
        double maxValue = Double.MAX_VALUE;
        TheState state = new TheState(maxValue);
        assertEquals(maxValue, state.getValue(), "The state object should accept the maximum allowed value.");
    }

    // Boundary input partition test
    @DisplayName("Test: State Object Value - Zero Value")
    @Test
    public void testTheStateObject_ZeroValue() {
        double zeroValue = 0.0;
        TheState state = new TheState(zeroValue);
        assertEquals(zeroValue, state.getValue(), "The state object should accept a zero value.");
    }
}
