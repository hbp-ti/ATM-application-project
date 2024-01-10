package PTDA_ATM;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicesTest {

    @Test
    @DisplayName("Create Service - Correct Entity and Value")
    void createServiceCorrectEntityAndValue() {
        String expectedEntity = "12345";
        double expectedValue = 123.45;

        Services service = new Services(expectedEntity, expectedValue);

        assertEquals(expectedEntity, service.getEntity(), "Service should have the correct entity.");
        assertEquals(expectedValue, service.getValue(), "Service should have the correct value.");
    }

    @Test
    @DisplayName("Service Value - Minimum Allowed")
    void serviceValueMinimumAllowed() {
        String entity = "TestEntity";
        double minValue = Double.MIN_VALUE;

        Services service = new Services(entity, minValue);

        assertEquals(entity, service.getEntity(), "Service should accept the minimum allowed value.");
        assertEquals(minValue, service.getValue(), "Service should accept the minimum allowed value.");
    }

    @Test
    @DisplayName("Service Value - Maximum Allowed")
    void serviceValueMaximumAllowed() {
        String entity = "TestEntity";
        double maxValue = Double.MAX_VALUE;

        Services service = new Services(entity, maxValue);

        assertEquals(entity, service.getEntity(), "Service should accept the maximum allowed value.");
        assertEquals(maxValue, service.getValue(), "Service should accept the maximum allowed value.");
    }

    @Test
    @DisplayName("Service Value - Zero Value")
    void serviceValueZero() {
        String entity = "TestEntity";
        double zeroValue = 0.0;

        Services service = new Services(entity, zeroValue);

        assertEquals(entity, service.getEntity(), "Service should accept a zero value.");
        assertEquals(zeroValue, service.getValue(), "Service should accept a zero value.");
    }
}
