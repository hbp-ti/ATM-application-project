package PTDA_ATM;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicesTest {

    // Structural test
    @DisplayName("Test: Check Services Object Creation - Verify the correct entity and value")
    @Test
    public void testServicesObject_CorrectEntityAndValue() {
        String expectedEntity = "12345";
        double expectedValue = 123.45;

        Services service = new Services(expectedEntity, expectedValue);

        assertEquals(expectedEntity, service.getEntity(), "The service object should have the correct entity.");
        assertEquals(expectedValue, service.getValue(), "The service object should have the correct value.");
    }

    // Boundary input partition test
    @DisplayName("Test: Services Object Value - Minimum Value Allowed")
    @Test
    public void testServicesObject_MinimumValue() {
        String entity = "TestEntity";
        double minValue = Double.MIN_VALUE;

        Services service = new Services(entity, minValue);

        assertEquals(entity, service.getEntity(), "The service object should accept the minimum allowed value.");
        assertEquals(minValue, service.getValue(), "The service object should accept the minimum allowed value.");
    }

    // Boundary input partition test
    @DisplayName("Test: Services Object Value - Maximum Value Allowed")
    @Test
    public void testServicesObject_MaximumValue() {
        String entity = "TestEntity";
        double maxValue = Double.MAX_VALUE;

        Services service = new Services(entity, maxValue);

        assertEquals(entity, service.getEntity(), "The service object should accept the maximum allowed value.");
        assertEquals(maxValue, service.getValue(), "The service object should accept the maximum allowed value.");
    }

    // Boundary input partition test
    @DisplayName("Test: Services Object Value - Zero Value")
    @Test
    public void testServicesObject_ZeroValue() {
        String entity = "TestEntity";
        double zeroValue = 0.0;

        Services service = new Services(entity, zeroValue);

        assertEquals(entity, service.getEntity(), "The service object should accept a zero value.");
        assertEquals(zeroValue, service.getValue(), "The service object should accept a zero value.");
    }
}
