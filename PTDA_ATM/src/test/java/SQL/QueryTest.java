package SQL;

import PTDA_ATM.Bills;
import PTDA_ATM.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QueryTest {
    private Conn conn;
    private Query query;

    @BeforeEach
    public void setUp() {
        conn = new Conn();
        conn.connect("estga-dev.ua.pt",3306,"PTDA_BD_003","PTDA_003","Gos_493ft");
        query = new Query();
    }

    @AfterEach
    void tearDown() {
        if (conn.isConnected()) {
            conn.close();
        }
    }

    //Input partition test
    @DisplayName("Test: Get Available Balance - Valid Account")
    @Test
    void testGetAvailableBalance_ValidAccount() {
        float balance = query.getAvailableBalance("07913610992884713357");

        assertNotEquals(0.0f, balance, "Balance should be retrieved for a valid account.");
    }

    //Input partition test
    @DisplayName("Test: Get Available Balance - Invalid Account")
    @Test
    void testGetAvailableBalance_InvalidAccount() {
        float balance = query.getAvailableBalance("07913610992884713322");

        assertEquals(0.0f, balance, "Balance should be 0 for an invalid account.");
    }

    // Structural test
    @DisplayName("Test: Get Available Balance - Not Null")
    @Test
    void testGetAvailableBalance_NotNull() {
        float balance = query.getAvailableBalance("34600481445834244594");
        assertNotNull(balance, "Balance should not be null.");
    }

    // Structural test
    @DisplayName("Test: Get Available Balance - Zero for No Transactions")
    @Test
    void testGetAvailableBalance_ZeroForNoTransactions() {
        float balance = query.getAvailableBalance("64254753326407403316");
        assertEquals(0.0f, balance, "Balance should be 0 for an account with no transactions.");
    }

    @DisplayName("Test: Check Balance - Valid Account")
    @Test
    void testCheckBalance_ValidAccount() {
        float balance = query.getAvailableBalance("07913610992884713357");

        assertNotEquals(0.0f, balance, "O saldo deve ser recuperado para uma conta válida.");
    }

    @DisplayName("Test: Check Balance - Invalid Account")
    @Test
    void testCheckBalance_InvalidAccount() {
        float balance = query.getAvailableBalance("07913610992884713322");

        assertEquals(0.0f, balance, "O saldo deve ser 0 para uma conta inválida.");
    }

    @DisplayName("Test: Check Balance - Non-Existing Account")
    @Test
    void testCheckBalance_NonExistingAccount() {
        float balance = query.getAvailableBalance("12345678901234567890");

        assertEquals(0.0f, balance, "O saldo deve ser NULL para uma conta inexistente.");
    }


    @DisplayName("Test: Check Balance - Valid Account")
    @Test
    void testCheckBalanceValidAccount() {
        float balance = query.getAvailableBalance("07913610992884713357");
        assertNotEquals(0.0f, balance, "O saldo deve ser recuperado para uma conta válida.");
    }

    @DisplayName("Test: Check Balance - Invalid Account")
    @Test
    void testCheckBalanceInvalidAccount() {
        float balance = query.getAvailableBalance("07913610992884713322");
        assertEquals(0.0f, balance, "O saldo deve ser 0 para uma conta inválida.");
    }

    @DisplayName("Test: Check Balance - Non-Existing Account")
    @Test
    void testCheckBalanceNonExistingAccount() {
        float balance = query.getAvailableBalance("12345678901234567890");
        assertEquals(0.0f, balance, "O saldo deve ser NULL para uma conta inexistente.");
    }

    @DisplayName("Test: Check Balance - Not Null")
    @Test
    void testCheckBalanceNotNull() {
        float balance = query.getAvailableBalance("34600481445834244594");
        assertNotNull(balance, "O saldo não deve ser nulo.");
    }

    @DisplayName("Test: Check Balance - Zero for No Transactions")
    @Test
    void testCheckBalanceZeroForNoTransactions() {
        float balance = query.getAvailableBalance("64254753326407403316");
        assertEquals(0.0f, balance, "O saldo deve ser 0 para uma conta sem transações.");
    }

    @DisplayName("Test: Movement - Valid Input")
    @Test
    void testMovement_ValidInput() {
        String clientAccountNumber = "07913610992884713357";
        String type = "Débit";
        float value = 100.0f;
        String description = "Withdraw";
        assertDoesNotThrow(() -> {
            boolean result = query.movement(clientAccountNumber, type, value, description);
            assertTrue(result, "Movement should be registered successfully.");
        });
    }

    @DisplayName("Test: Movement - Invalid Account")
    @Test
    void testMovement_InvalidAccount() {
        String clientAccountNumber = "1";
        String type = "Credit";
        float value = 100.0f;
        String description = "Deposit";
        assertThrows(SQLException.class, () -> {
            query.movement(clientAccountNumber, type, value, description);
        });
    }

    @Test
    @DisplayName("Test: Movement Phone - Successful Movement")
    public void testMovementPhone_SuccessfulMovement() throws SQLException {
        Query query = new Query();
        String phoneNumber = "123456789"; // Existing phone number
        String clientAccountNumber = "62292304241542343424"; // Existing client account number
        String type = "Débit";
        float value = 100.0f;
        String description = "Phone Charge";

        boolean result = query.movementPhone(phoneNumber, clientAccountNumber, type, value, description);

        assertTrue(result);
    }

    @Test
    @DisplayName("Test: Movement Phone - Nonexistent Phone Number")
    public void testMovementPhone_NonexistentPhoneNumber() throws SQLException {
        Query query = new Query();
        String phoneNumber = "965358501"; // Nonexistent phone number
        String clientAccountNumber = "62292304241542343424"; // Existing client account number
        String type = "Débit";
        float value = 100.0f;
        String description = "Phone Charge";

        boolean result = query.movementPhone(phoneNumber, clientAccountNumber, type, value, description);

        assertFalse(result);
    }

    @Test
    @DisplayName("Test: Movement Phone - Null Phone Number")
    public void testMovementPhone_NullPhoneNumber() throws SQLException {
        Query query = new Query();
        String phoneNumber = null; // Null phone number
        String clientAccountNumber = "62292304241542343424"; // Existing client account number
        String type = "Débit";
        float value = 100.0f;
        String description = "Phone Charge";

        boolean result = query.movementPhone(phoneNumber, clientAccountNumber, type, value, description);

        assertFalse(result);
    }

    @DisplayName("Test: Does Account Exist - Existing Account")
    @Test
    void testDoesAccountExist_ExistingAccount() {
        // Arrange
        String existingAccountNumber = "07913610992884713357";

        // Act
        boolean accountExists = query.doesAccountExist(existingAccountNumber);

        // Assert
        assertTrue(accountExists, "The existing account should be found in the database.");
    }

    @DisplayName("Test: Does Account Exist - Non-Existing Account")
    @Test
    void testDoesAccountExist_NonExistingAccount() {
        // Arrange
        String nonExistingAccountNumber = "12345678901234567890";

        // Act
        boolean accountExists = query.doesAccountExist(nonExistingAccountNumber);

        // Assert
        assertFalse(accountExists, "The non-existing account should not be found in the database.");
    }

    @DisplayName("Test: Does Account Exist - Empty Account Number")
    @Test
    void testDoesAccountExist_EmptyAccountNumber() {
        // Arrange
        String emptyAccountNumber = "";

        // Act
        boolean accountExists = query.doesAccountExist(emptyAccountNumber);

        // Assert
        assertFalse(accountExists, "An empty account number should not be found in the database.");
    }

    @DisplayName("Test: Does Account Exist - Null Account Number")
    @Test
    void testDoesAccountExist_NullAccountNumber() {
        // Arrange
        String nullAccountNumber = null;

        // Act
        boolean accountExists = query.doesAccountExist(nullAccountNumber);

        // Assert
        assertFalse(accountExists, "A null account number should not be found in the database.");
    }

    @DisplayName("Test: Get Client Name - Existing Account")
    @Test
    void testGetClientName_ExistingAccount() {
        // Arrange
        String existingAccountNumber = "07913610992884713357";

        // Act
        String clientName = query.getClientName(existingAccountNumber);

        // Assert
        assertNotNull(clientName, "The client name should be retrieved for an existing account.");
    }

    @DisplayName("Test: Get Client Name - Non-Existing Account")
    @Test
    void testGetClientName_NonExistingAccount() {
        // Arrange
        String nonExistingAccountNumber = "12345678901234567890";

        // Act
        String clientName = query.getClientName(nonExistingAccountNumber);

        // Assert
        assertNull(clientName, "The client name should be null for a non-existing account.");
    }

    @DisplayName("Test: Get Client Name - Empty Account Number")
    @Test
    void testGetClientName_EmptyAccountNumber() {
        // Arrange
        String emptyAccountNumber = "";

        // Act
        String clientName = query.getClientName(emptyAccountNumber);

        // Assert
        assertNull(clientName, "The client name should be null for an empty account number.");
    }

    @DisplayName("Test: Get Client Name - Null Account Number")
    @Test
    void testGetClientName_NullAccountNumber() {
        // Arrange
        String nullAccountNumber = null;

        // Act
        String clientName = query.getClientName(nullAccountNumber);

        // Assert
        assertNull(clientName, "The client name should be null for a null account number.");
    }

    @DisplayName("Test: Insert Bank Account Data - Valid Data")
    @Test
    void testInsertBankAccountData_ValidData() {
        // Arrange
        String name = "Carlos Abrantes";
        String NIF = "123456789";
        String address = "Rua das Almas 67";
        String zipCode = "12345";
        String phone = "987654321";
        String email = "john.doe@example.com";
        LocalDate date = LocalDate.of(1990, 1, 1);
        String marital = "Single";
        String gender = "Male";

        // Act
        assertDoesNotThrow(() -> {
            String accountNumber = query.insertBankAccountData(name, NIF, address, zipCode, phone, email, date, marital, gender);
            assertNotNull(accountNumber, "The account number should be generated for valid data.");
        });
    }

    @DisplayName("Test: Insert Bank Account Data - Invalid NIF")
    @Test
    void testInsertBankAccountData_InvalidNIF() {
        // Arrange
        String invalidNIF = "invalidNIF"; // Invalid NIF format

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> {
            query.insertBankAccountData("Angelo Silva", invalidNIF, "Rua das Cerejas", "2635-192", "918203827", "angelo@gmail.com", LocalDate.now(), "Single", "Male");
        }, "NumberFormatException should be thrown for invalid NIF.");
    }

    @DisplayName("Test: Insert Bank Account Data - Null Gender")
    @Test
    void testInsertBankAccountData_NullGender() {
        //Arange
        String gender = null;

        // Act & Assert
        assertThrows(SQLException.class, () -> {
            query.insertBankAccountData("Maria Silva", "123456789", "Rua das Almas", "1234-456", "918273429", "maria@ua.pt", LocalDate.now(), "Single", gender);
        }, "SQLException should be thrown for null gender.");
    }

    @DisplayName("Test: Insert Card Data - Null Account Number")
    @Test
    void testInsertCardData_NullAccountNumber() {
        // Act
        assertThrows(SQLException.class, () -> query.insertCardData(null), "Should throw SQLException for null account number.");
    }

    @DisplayName("Test: Insert Card Data - Empty Account Number")
    @Test
    void testInsertCardData_EmptyAccountNumber() {
        // Act
        assertThrows(SQLException.class, () -> query.insertCardData(""), "Should throw SQLException for empty account number.");
    }

    @DisplayName("Test: Insert Card Data - Nonexistent Account Number")
    @Test
    void testInsertCardData_NonexistentAccountNumber() {
        // Arrange
        String nonExistentAccountNumber = "07111111111184716666";

        // Act
        assertThrows(SQLException.class, () -> query.insertCardData(nonExistentAccountNumber), "Should throw SQLException for nonexistent account number.");
    }

    @DisplayName("Test: Get Account Number - Valid Card Number")
    @Test
    void testGetAccountNumber_ValidCardNumber() {
        // Arrange
        String validCardNumber = "3623515985";

        // Act
        String accountNumber = query.getAccountNumber(validCardNumber);

        // Assert
        assertNotNull(accountNumber, "Account number should not be null for a valid card number.");
    }

    @DisplayName("Test: Get Account Number - Invalid Card Number")
    @Test
    void testGetAccountNumber_InvalidCardNumber() {
        // Arrange
        String invalidCardNumber = "362351598511";

        // Act
        String accountNumber = query.getAccountNumber(invalidCardNumber);

        // Assert
        assertNull(accountNumber, "Account number should be null for an invalid card number.");
    }

    @DisplayName("Test: Get Account Number - Null Card Number")
    @Test
    void testGetAccountNumber_NullCardNumber() {
        // Act
        String accountNumber = query.getAccountNumber(null);

        // Assert
        assertNull(accountNumber, "Account number should be null for a null card number.");
    }

    @DisplayName("Test: Get Account Number - Empty Card Number")
    @Test
    void testGetAccountNumber_EmptyCardNumber() {
        // Act
        String accountNumber = query.getAccountNumber("");

        // Assert
        assertNull(accountNumber, "Account number should be null for an empty card number.");
    }

    @DisplayName("Test: Get Account Number - Whitespace Card Number")
    @Test
    void testGetAccountNumber_WhitespaceCardNumber() {
        // Arrange
        String whitespaceCardNumber = "   ";

        // Act
        String accountNumber = query.getAccountNumber(whitespaceCardNumber);

        // Assert
        assertNull(accountNumber, "Account number should be null for a whitespace card number.");
    }

    // Teste para verificar se o gênero é obtido corretamente para um número de conta válido
    @DisplayName("Test: Get Gender - Valid Account")
    @Test
    void testGetGenderFromDatabase_ValidAccount() {
        // Arrange
        String validAccountNumber = "07913610992884713357";

        // Act
        String gender = query.getGenderFromDatabase(validAccountNumber);

        // Assert
        assertEquals("Female", gender, "Gender should be 'Male' for a valid account.");
    }

    // Teste para verificar se o método retorna null para um número de conta inválido
    @DisplayName("Test: Get Gender - Invalid Account")
    @Test
    void testGetGenderFromDatabase_InvalidAccount() {
        // Arrange
        String invalidAccountNumber = "2222255513357";

        // Act
        String gender = query.getGenderFromDatabase(invalidAccountNumber);

        // Assert
        assertNull(gender, "Gender should be null for an invalid account.");
    }

    // Teste para verificar se o método retorna null para um número de conta inexistente
    @DisplayName("Test: Get Gender - Nonexistent Account")
    @Test
    void testGetGenderFromDatabase_NonexistentAccount() {
        // Arrange
        String nonexistentAccountNumber = "11111610992884713357";

        // Act
        String gender = query.getGenderFromDatabase(nonexistentAccountNumber);

        // Assert
        assertNull(gender, "Gender should be null for a nonexistent account.");
    }

    // Teste para verificar se o método retorna null para um número de conta nulo
    @DisplayName("Test: Get Gender - Null Account Number")
    @Test
    void testGetGenderFromDatabase_NullAccountNumber() {
        // Arrange
        String nullAccountNumber = null;

        // Act
        String gender = query.getGenderFromDatabase(nullAccountNumber);

        // Assert
        assertNull(gender, "Gender should be null for a null account number.");
    }

    // Teste para verificar se o método retorna null para um número de conta vazio
    @DisplayName("Test: Get Gender - Empty Account Number")
    @Test
    void testGetGenderFromDatabase_EmptyAccountNumber() {
        // Arrange
        String emptyAccountNumber = "";

        // Act
        String gender = query.getGenderFromDatabase(emptyAccountNumber);

        // Assert
        assertNull(gender, "Gender should be null for an empty account number.");
    }

    @DisplayName("Test: Verify Card Info - Valid Info")
    @Test
    void testVerifyCardInfo_ValidInfo() {
        // Arrange
        String validCardNumber = "4744698153";
        String validPIN = "2444";

        // Act
        boolean isValid = query.verifyCardInfo(validCardNumber, validPIN);

        // Assert
        assertTrue(isValid, "Card information should be valid.");
    }

    @DisplayName("Test: Verify Card Info - Invalid Card Number")
    @Test
    void testVerifyCardInfo_InvalidCardNumber() {
        // Arrange
        String invalidCardNumber = "invalidCardNumber";
        String validPIN = "1234";

        // Act
        boolean isValid = query.verifyCardInfo(invalidCardNumber, validPIN);

        // Assert
        assertFalse(isValid, "Card information should be invalid for an invalid card number.");
    }

    @DisplayName("Test: Verify Card Info - Invalid PIN")
    @Test
    void testVerifyCardInfo_InvalidPIN() {
        // Arrange
        String validCardNumber = "3623515985";
        String invalidPIN = "invalidPIN";

        // Act
        boolean isValid = query.verifyCardInfo(validCardNumber, invalidPIN);

        // Assert
        assertFalse(isValid, "Card information should be invalid for an invalid PIN.");
    }

    @DisplayName("Test: Verify Card Info - Nonexistent Card")
    @Test
    void testVerifyCardInfo_NonexistentCard() {
        // Arrange
        String nonexistentCardNumber = "1111111111";
        String validPIN = "1234";

        // Act
        boolean isValid = query.verifyCardInfo(nonexistentCardNumber, validPIN);

        // Assert
        assertFalse(isValid, "Card information should be invalid for a nonexistent card.");
    }

    @DisplayName("Test: Does Phone Number Exist - Existing Number")
    @Test
    void testDoesPhoneNumberExist_ExistingNumber() {
        // Arrange
        String existingPhoneNumber = "123456789";

        // Act
        boolean exists = false;
        try {
            exists = query.doesPhoneNumberExist(existingPhoneNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Assert
        assertTrue(exists, "Phone number should exist in the database.");
    }

    @DisplayName("Test: Does Phone Number Exist - Nonexistent Number")
    @Test
    void testDoesPhoneNumberExist_NonexistentNumber() {
        // Arrange
        String nonexistentPhoneNumber = "911111111";

        // Act
        boolean exists = false;
        try {
            exists = query.doesPhoneNumberExist(nonexistentPhoneNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Assert
        assertFalse(exists, "Phone number should not exist in the database.");
    }

    @DisplayName("Test: Change PIN in Database - Valid PIN Change")
    @Test
    void testChangePINInDatabase_ValidPINChange() {
        // Arrange
        String cardNumber = "3623515985";
        String currentPIN = "6121";
        String newPIN = "5678";

        // Act
        boolean success = query.changePINInDatabase(cardNumber, currentPIN, newPIN);

        // Assert
        assertTrue(success, "PIN should be changed successfully.");
    }

    @DisplayName("Test: Change PIN in Database - Invalid Current PIN")
    @Test
    void testChangePINInDatabase_InvalidCurrentPIN() {
        // Arrange
        String cardNumber = "3623515985";
        String currentPIN = "0000";
        String newPIN = "5678";

        // Act
        boolean success = query.changePINInDatabase(cardNumber, currentPIN, newPIN);

        // Assert
        assertFalse(success, "PIN should not be changed with invalid current PIN.");
    }

    //Input partition test
    @DisplayName("Test: Client Card Number - Existing Account")
    @Test
    void testGetClientCardNumber_ExistingAccount() {
        assertNotNull(query.getClientCardNumber("64254753326407403316"),
                "Should retrieve a card number for an existing account.");
    }

    //Input partition test
    @DisplayName("Test: Client Card Number - Non-Existing Account")
    @Test
    void testGetClientCardNumber_NonExistingAccount() {
        assertNull(query.getClientCardNumber("NonExistingAccountNumber"),
                "Should return null for a non-existing account.");
    }

    // Structural test
    @DisplayName("Test: Client Card Number - Null Account Number")
    @Test
    void testGetClientCardNumber_NullAccountNumber() {
        assertNull(query.getClientCardNumber(null),
                "Should return null for null account number.");
    }

    // Structural test
    @DisplayName("Test: Client Card Number - Empty Account Number")
    @Test
    void testGetClientCardNumber_EmptyAccountNumber() {
        assertNull(query.getClientCardNumber(""),
                "Should return null for empty account number.");
    }

    //Input partition test
    @DisplayName("Test: Client Email - Existing Account")
    @Test
    void testGetClientEmail_ExistingAccount() {
        assertNotNull(query.getClientEmail("64254753326407403316"),
                "Should retrieve an email for an existing account.");
    }

    //Input partition test
    @DisplayName("Test: Client Email - Non-Existing Account")
    @Test
    void testGetClientEmail_NonExistingAccount() {
        assertNull(query.getClientEmail("NonExistingAccountNumber"),
                "Should return null for a non-existing account.");
    }

    //Structural test
    @DisplayName("Test: Client Email - Null Account Number")
    @Test
    void testGetClientEmail_NullAccountNumber() {
        assertNull(query.getClientEmail(null),
                "Should return null for null account number.");
    }

    //Structural test
    @DisplayName("Test: Client Email - Empty Account Number")
    @Test
    void testGetClientEmail_EmptyAccountNumber() {
        assertNull(query.getClientEmail(""),
                "Should return null for empty account number.");
    }


    @Test
    @DisplayName("Test: Load Mini Statement - Existing Account With Transactions")
    public void testLoadMiniStatement_ExistingAccountWithTransactions() {
        String clientAccountNumber = "34600481445834244594"; // Existing account with 15 transactions
        StringBuilder result = query.loadMiniStatement(clientAccountNumber);
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    @DisplayName("Test: Load Mini Statement - Existing Account Without Transactions")
    public void testLoadMiniStatement_ExistingAccountWithoutTransactions() {
        String clientAccountNumber = "987654321"; // Existing account without transactions
        StringBuilder result = query.loadMiniStatement(clientAccountNumber);
        assertNotNull(result);
        assertEquals(0, result.length());
    }

    @Test
    @DisplayName("Test: Load Mini Statement - Less Than 15 Transactions")
    public void testLoadMiniStatement_LessThan15Transactions() {
        String clientAccountNumber = "94953640899477091010"; // Account with less than 15 transactions
        StringBuilder result = query.loadMiniStatement(clientAccountNumber);
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    @DisplayName("Test: Load Mini Statement - Invalid Account Number")
    public void testLoadMiniStatement_InvalidAccountNumber() {
        String clientAccountNumber = "invalid"; // Invalid account number
        StringBuilder result = query.loadMiniStatement(clientAccountNumber);
        assertNotNull(result);
        assertEquals(0, result.length());
    }

    @Test
    @DisplayName("Test: Get Stored PIN - Existing Card")
    public void testGetStoredPIN_ExistingCard() {
        String cardNumber = "8438960862"; // Existing card number
        String storedPIN = query.getStoredPIN(cardNumber);
        assertNotNull(storedPIN);
        assertFalse(storedPIN.isEmpty());
    }

    @Test
    @DisplayName("Test: Get Stored PIN - Nonexistent Card")
    public void testGetStoredPIN_NonexistentCard() {
        String cardNumber = "0000000000000000"; // Nonexistent card number
        String storedPIN = query.getStoredPIN(cardNumber);
        assertNotNull(storedPIN);
        assertTrue(storedPIN.isEmpty());
    }

    @Test
    @DisplayName("Test: Get Stored PIN - Card With Empty PIN")
    public void testGetStoredPIN_CardWithEmptyPIN() {
        String cardNumber = "9876543210987654"; // Card with empty PIN
        String storedPIN = query.getStoredPIN(cardNumber);
        assertNotNull(storedPIN);
        assertTrue(storedPIN.isEmpty());
    }

    @Test
    @DisplayName("Test: Get Stored PIN - Card With Null PIN")
    public void testGetStoredPIN_CardWithNullPIN() {
        String cardNumber = "1111222233334444"; // Card with null PIN
        String storedPIN = query.getStoredPIN(cardNumber);
        assertNotNull(storedPIN);
        assertTrue(storedPIN.isEmpty());
    }

    @Test
    @DisplayName("Test: Generate Account Number - Unique and Nonexistent")
    public void testGenerateAccountNumber_UniqueAndNonexistent() throws SQLException {
        Query query = new Query();

        String accountNumber = query.generateAccountNumber();

        assertNotNull(accountNumber);
        assertFalse(query.isAccountNumberExists(accountNumber));
    }

    @Test
    @DisplayName("Test: Generate Account Number - Multiple Unique Numbers")
    public void testGenerateAccountNumber_MultipleUniqueNumbers() throws SQLException {
        Query query = new Query();

        String accountNumber1 = query.generateAccountNumber();
        String accountNumber2 = query.generateAccountNumber();

        assertNotNull(accountNumber1);
        assertNotNull(accountNumber2);
        assertNotEquals(accountNumber1, accountNumber2);
        assertFalse(query.isAccountNumberExists(accountNumber1));
        assertFalse(query.isAccountNumberExists(accountNumber2));
    }

    @Test
    @DisplayName("Test: Generate Account Number - Maximum Length")
    public void testGenerateAccountNumber_MaxLength() throws SQLException {
        Query query = new Query();

        String accountNumber = query.generateAccountNumber();

        assertNotNull(accountNumber);
        assertTrue(accountNumber.length() <= 20);
        assertFalse(query.isAccountNumberExists(accountNumber));
    }

    @Test
    @DisplayName("Test: Generate Account Number - Null Check")
    public void testGenerateAccountNumber_NullCheck() throws SQLException {
        Query query = new Query();

        assertDoesNotThrow(() -> {
            String accountNumber = query.generateAccountNumber();
            assertNotNull(accountNumber);
        });
    }



    @Test
    @DisplayName("Test: Is Account Number Exists - Existing Account")
    void testIsAccountNumberExists_ExistingAccount() {
        String existingAccountNumber = "34600481445834244594";
        try {
            boolean exists = query.isAccountNumberExists(existingAccountNumber);
            assertTrue(exists);
        } catch (SQLException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test: Is Account Number Exists - Nonexistent Account")
    void testIsAccountNumberExists_NonexistentAccount() {
        String nonexistentAccountNumber = "987654321";
        try {
            boolean exists = query.isAccountNumberExists(nonexistentAccountNumber);
            assertFalse(exists);
        } catch (SQLException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test: Generate Random Number - Valid Length")
    void testGenerateRandomNumber_ValidLength() {
        int length = 8;
        String randomNumber = query.generateRandomNumber(length);
        assertNotNull(randomNumber);
        assertEquals(length, randomNumber.length());
    }

    @Test
    @DisplayName("Test: Generate Random Number - Zero Length")
    void testGenerateRandomNumber_ZeroLength() {
        int length = 0;
        String randomNumber = query.generateRandomNumber(length);
        assertNotNull(randomNumber);
        assertEquals(0, randomNumber.length());
    }

    @Test
    @DisplayName("Test: Generate Random Number - Negative Length")
    void testGenerateRandomNumber_NegativeLength() {
        int length = -5;
        String randomNumber = query.generateRandomNumber(length);
        assertNotNull(randomNumber);
        assertEquals(0, randomNumber.length());
    }

    @Test
    @DisplayName("Test: Generate Random Number - Is Number")
    void testGenerateRandomNumber_IsNumber() {
        int length = 10;
        String randomNumber = query.generateRandomNumber(length);
        assertNotNull(randomNumber);
        assertTrue(randomNumber.matches("[0-9]+"));
    }

    @Test
    @DisplayName("Test: Generate Card Number - Unique and Nonexistent")
    public void testGenerateCardNumber_UniqueAndNonexistent() throws SQLException {
        Query query = new Query();

        String cardNumber = query.generateCardNumber();

        assertNotNull(cardNumber);
        assertFalse(query.isCardNumberExists(cardNumber));
    }

    @Test
    @DisplayName("Test: Generate Card Number - Multiple Unique Numbers")
    public void testGenerateCardNumber_MultipleUniqueNumbers() throws SQLException {
        Query query = new Query();

        String cardNumber1 = query.generateCardNumber();
        String cardNumber2 = query.generateCardNumber();

        assertNotNull(cardNumber1);
        assertNotNull(cardNumber2);
        assertNotEquals(cardNumber1, cardNumber2);
        assertFalse(query.isCardNumberExists(cardNumber1));
        assertFalse(query.isCardNumberExists(cardNumber2));
    }

    @Test
    @DisplayName("Test: Generate Card Number - Maximum Length")
    public void testGenerateCardNumber_MaxLength() throws SQLException {
        Query query = new Query();

        String cardNumber = query.generateCardNumber();

        assertNotNull(cardNumber);
        assertTrue(cardNumber.length() <= 10);
        assertFalse(query.isCardNumberExists(cardNumber));
    }

    @Test
    @DisplayName("Test: Generate Card Number - Null Check")
    public void testGenerateCardNumber_NullCheck() throws SQLException {
        Query query = new Query();

        assertDoesNotThrow(() -> {
            String cardNumber = query.generateCardNumber();
            assertNotNull(cardNumber);
        });
    }

    @Test
    @DisplayName("Test: Generate Card Number - Leading Zeros")
    public void testGenerateCardNumber_LeadingZeros() throws SQLException {
        Query query = new Query();

        String cardNumber = query.generateCardNumber();

        assertNotNull(cardNumber);
        assertFalse(cardNumber.matches("^0+.*$")); // Ensure no leading zeros
    }

    @Test
    @DisplayName("Test generateCardPIN: Valid Length")
    public void testGenerateCardPINValidLength() {
        Query query = new Query();
        String cardPIN = query.generateCardPIN();

        assertNotNull(cardPIN);
        assertEquals(4, cardPIN.length());
    }

    @Test
    @DisplayName("Test generateCardPIN: ContainsOnlyDigits")
    public void testGenerateCardPINContainsOnlyDigits() {
        Query query = new Query();
        String cardPIN = query.generateCardPIN();

        assertTrue(cardPIN.matches("\\d+"));
    }

    @Test
    @DisplayName("Card Number Exists - Positive Case")
    public void testIsCardNumberExists_PositiveCase() throws SQLException {
        // Arrange
        Query query = new Query();
        String existingCardNumber = "3623515985"; // Replace with an existing card number in your database

        // Act
        boolean exists = query.isCardNumberExists(existingCardNumber);

        // Assert
        assertTrue(exists, "Card number should exist in the database.");
    }

    @Test
    @DisplayName("Card Number Does Not Exist - Negative Case")
    public void testIsCardNumberExists_NegativeCase() throws SQLException {
        // Arrange
        Query query = new Query();
        String nonExistingCardNumber = "8192847110"; // Replace with a non-existing card number

        // Act
        boolean exists = query.isCardNumberExists(nonExistingCardNumber);

        // Assert
        assertFalse(exists, "Card number should not exist in the database.");
    }
}
