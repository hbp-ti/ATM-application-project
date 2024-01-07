package PTDA_ATM;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.stage.Stage;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private Main main;

    @BeforeEach
    public void setUp() {
        main = new Main();
    }

    @AfterEach
    public void tearDown() {
        main = null;
    }

    @Test
    public void testConnectionNotNullAfterStart() throws IOException {
        assertNull(main.getConnection()); // Garante que a conexão seja nula antes de iniciar
        main.start(new Stage()); // Inicia a aplicação
        assertNotNull(main.getConnection()); // Verifica se a conexão não é nula após o início
    }

    @Test
    public void testConnectionClosedAfterStop() throws IOException {
        main.start(new Stage()); // Inicia a aplicação
        assertNotNull(main.getConnection()); // Verifica se a conexão não é nula após o início
        main.stop(); // Encerra a aplicação
        assertNull(main.getConnection()); // Verifica se a conexão é nula após o encerramento
    }
}

