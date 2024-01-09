import PTDA_ATM.ControllerLogIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerLogInTest {

    private ControllerLogIn controller;

    @BeforeEach
    void setUp() {
        controller = new ControllerLogIn();
        // Configurar objetos mock ou injetar dependências, se necessário
    }

    @Test
    void switchToMainPage_ValidCredentials_ShouldSwitchToMenuPage() throws Exception {
        // Configurar condições de teste
        Query mockQuery = Mockito.mock(Query.class);
        controller.query = mockQuery;

        when(mockQuery.verifyCardInfo(anyString(), anyString())).thenReturn(true);
        when(mockQuery.getAccountNumber(anyString())).thenReturn("mockedAccountNumber");
        when(mockQuery.getClientName(anyString())).thenReturn("Mocked Client");

        ActionEvent mockEvent = Mockito.mock(ActionEvent.class);

        // Executar o método a ser testado
        controller.switchToMainPage(mockEvent);

        // Verificar se a transição para a página do menu foi realizada
        assertNotNull(controller.clientAccountNumber);
        assertNotNull(controller.clientName);
    }

    @Test
    void switchToMainPage_InvalidCredentials_ShouldNotSwitchToMenuPage() throws Exception {
        // Configurar condições de teste
        Query mockQuery = Mockito.mock(Query.class);
        controller.query = mockQuery;

        when(mockQuery.verifyCardInfo(anyString(), anyString())).thenReturn(false);

        ActionEvent mockEvent = Mockito.mock(ActionEvent.class);

        // Executar o método a ser testado
        controller.switchToMainPage(mockEvent);

        // Verificar se não houve transição para a página do menu
        assertNull(controller.clientAccountNumber);
        assertNull(controller.clientName);
    }

    @Test
    void switchToMenu_ValidCredentials_ShouldLoadMenuPage() throws Exception {
        // Configurar condições de teste
        Query mockQuery = Mockito.mock(Query.class);
        controller.query = mockQuery;

        when(mockQuery.verifyCardInfo(anyString(), anyString())).thenReturn(true);
        when(mockQuery.getAccountNumber(anyString())).thenReturn("mockedAccountNumber");
        when(mockQuery.getClientName(anyString())).thenReturn("Mocked Client");

        FXMLLoader mockLoader = Mockito.mock(FXMLLoader.class);
        when(mockLoader.load()).thenReturn(new VBox());

        Stage mockStage = Mockito.mock(Stage.class);
        doNothing().when(mockStage).setScene(any(Scene.class));

        ActionEvent mockEvent = Mockito.mock(ActionEvent.class);
        when(mockEvent.getSource()).thenReturn(new Button());

        // Executar o método a ser testado
        controller.switchToMenu(mockEvent);

        // Verificar se a cena do menu foi carregada
        assertNotNull(controller.clientAccountNumber);
        assertNotNull(controller.clientName);
    }

    // Adicione mais testes conforme necessário para outros métodos

}
