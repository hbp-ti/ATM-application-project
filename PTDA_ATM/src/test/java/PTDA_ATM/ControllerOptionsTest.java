package PTDA_ATM;

import PTDA_ATM.ControllerOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerOptionsTest {

    private ControllerOptions controller;

    @BeforeEach
    void setUp() {
        controller = new ControllerOptions();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

}
