package SQL;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

public class Query {
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private ResultSet rsEmail;
    private ResultSet rsName;
    private Connection connection = Conn.getConnection();


    public float getAvailableBalance(String clientCardNumber) {
        try {
            String query = "SELECT accountBalance FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber  = ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientCardNumber);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getFloat("accountBalance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return 0.0f;  // Return 0.0 in case of an error
    }

    public BigDecimal checkBalance(String clientCardNumber) {
        try {
            String query = "SELECT accountBalance FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientCardNumber);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("accountBalance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }


    public boolean movement(String clientCardNumber, String type, float value, String description) throws SQLException {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO Movement (cardNumber, movementDate, movementType, movementValue, movementDescription) VALUES (?, NOW(), ?, ?, ?)");
            preparedStatement.setString(1, clientCardNumber);
            preparedStatement.setString(2, type);
            preparedStatement.setFloat(3, value);
            preparedStatement.setString(4, description);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } finally {
            // Certifique-se de fechar os recursos
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public boolean movementPhone(String phoneNumber, String clientCardNumber, String type, float value, String description) throws SQLException {
        if(doesPhoneNumberExist(phoneNumber)) {
            try {
                preparedStatement = connection.prepareStatement("INSERT INTO Movement (cardNumber, movementDate, movementType, movementValue, movementDescription) VALUES (?, NOW(), ?, ?, ?)");
                preparedStatement.setString(1, clientCardNumber);
                preparedStatement.setString(2, type);  // ou qualquer valor padrão para movimentos de depósito
                preparedStatement.setFloat(3, value);
                preparedStatement.setString(4, description);

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0;
            } catch(SQLException e) {
                e.printStackTrace();
                return false;

            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        }
        return false;
    }

    public String getClientName(String clientCardNumber) {
        try {
            String query = "SELECT clientName FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientCardNumber);
            rsName = preparedStatement.executeQuery();

            if (rsName.next()) {
                return rsName.getString("clientName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsName != null) {
                    rsName.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;  // Retorna null se não conseguir obter o clientName
    }

    public String getClientEmail(String clientCardNumber) {
        try {
            String query = "SELECT email FROM BankAccount WHERE accountNumber IN (SELECT accountNumber FROM Card WHERE cardNumber = ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientCardNumber);
            rsEmail = preparedStatement.executeQuery();

            if (rsEmail.next()) {
                return rsEmail.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsEmail != null) {
                    rsEmail.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;  // Retorna null se não conseguir obter o email
    }

    public String insertBankAccountData(String name, String NIF, String address, String zipCode, String phone, String email, LocalDate date, String marital, String gender) throws SQLException {
        PreparedStatement preparedStatementBankAccount = connection.prepareStatement("INSERT INTO BankAccount (accountNumber, clientName, NIF, address, zipcode, phoneNumber, email, birthDate, maritalStatus, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        String accountNumber = generateAccountNumber();
        preparedStatementBankAccount.setString(1, accountNumber);
        preparedStatementBankAccount.setString(2, name);
        preparedStatementBankAccount.setInt(3, Integer.parseInt(NIF));
        preparedStatementBankAccount.setString(4, address);
        preparedStatementBankAccount.setString(5, zipCode);
        preparedStatementBankAccount.setInt(6, Integer.parseInt(phone));
        preparedStatementBankAccount.setString(7, email);
        preparedStatementBankAccount.setDate(8, Date.valueOf(date));
        preparedStatementBankAccount.setString(9, marital);
        preparedStatementBankAccount.setString(10, gender);

        preparedStatementBankAccount.executeUpdate();
        preparedStatementBankAccount.close();

        return accountNumber;
    }


    public String[] insertCardData(String accountNumber) throws SQLException {
        PreparedStatement preparedStatementCard = connection.prepareStatement("INSERT INTO Card (cardNumber, accountNumber, cardPIN) VALUES (?, ?, ?)");

        String cardNumber = generateCardNumber();
        String cardPIN = generateCardPIN();

        preparedStatementCard.setString(1, cardNumber);
        preparedStatementCard.setString(2, accountNumber);
        preparedStatementCard.setString(3, cardPIN);

        preparedStatementCard.executeUpdate();
        preparedStatementCard.close();

        return new String[] { cardNumber, cardPIN };
    }

    public StringBuilder loadMiniStatement(String clientCardNumber) {
        StringBuilder miniStatement = new StringBuilder();
        try {
            String query = "SELECT * FROM Movement WHERE cardNumber = ? ORDER BY movementDate DESC LIMIT 15";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientCardNumber);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String movementDescription = rs.getString("movementDescription");
                String movementDate = rs.getString("movementDate");
                String movementValue = rs.getString("movementValue");

                miniStatement.append(movementDate).append(" - ").append(movementDescription)
                        .append(": ").append(movementValue).append("€\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return miniStatement;
    }

    // Método que obtém o género da conta ssociada
    public String getGenderFromDatabase(String cardNumber) {
        String gender = null;

        // Obtém o número da conta associado ao número do cartão
        String getAccountNumberQuery = "SELECT accountNumber FROM Card WHERE cardNumber = ?";
        try (PreparedStatement accountNumberStatement = connection.prepareStatement(getAccountNumberQuery)) {
            accountNumberStatement.setString(1, cardNumber);
            ResultSet accountNumberResultSet = accountNumberStatement.executeQuery();

            if (accountNumberResultSet.next()) {
                String accountNumber = accountNumberResultSet.getString("accountNumber");

                // Obtém o gênero usando o número da conta
                String getGenderQuery = "SELECT gender FROM BankAccount WHERE accountNumber = ?";
                try (PreparedStatement genderStatement = connection.prepareStatement(getGenderQuery)) {
                    genderStatement.setString(1, accountNumber);
                    ResultSet genderResultSet = genderStatement.executeQuery();

                    if (genderResultSet.next()) {
                        gender = genderResultSet.getString("gender");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro a obter género da base de dados: " + e.getMessage());
        }
        return gender;
    }

    public boolean verifyCardInfo(String clientCardNumber, String password) {
        try {
            connection = Conn.getConnection();
            preparedStatement = connection.prepareStatement("SELECT cardNumber, cardPIN FROM Card WHERE cardNumber = ? AND cardPIN = ?");
            preparedStatement.setString(1, clientCardNumber);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("SQLExeption: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return false;
    }

    private boolean doesPhoneNumberExist(String phoneNumber) throws SQLException {
        String query = "SELECT phoneNumber FROM BankAccount WHERE phoneNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, phoneNumber);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return true;
            }
        }
        return false;
    }

    public boolean changePINInDatabase(String cardNumber, String currentPIN, String newPIN) {
        try {
            // Verifica se o PIN atual corresponde
            if (currentPIN.equals(getStoredPIN(cardNumber))) {
                // Atualiza o PIN no banco de dados
                String updateQuery = "UPDATE Card SET cardPIN = ? WHERE cardNumber = ?";
                preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, newPIN);
                preparedStatement.setString(2, cardNumber);
                preparedStatement.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return false;
    }

    public String getStoredPIN(String cardNumber) {
        String storedPIN = "";
        try {
            String query = "SELECT cardPIN FROM Card WHERE cardNumber = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cardNumber);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                storedPIN = rs.getString("cardPIN").trim();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return storedPIN;
    }

    private String generateAccountNumber() throws SQLException {
        while (true) {
            String accountNumber = generateRandomNumber(20);
            if (!isAccountNumberExists(accountNumber)) {
                return accountNumber;
            }
        }
    }

    private boolean isAccountNumberExists(String accountNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM BankAccount WHERE accountNumber = ?");
        preparedStatement.setString(1, accountNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count > 0;
    }

    private String generateRandomNumber(int length) {
        StringBuilder number = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            number.append(digit);
        }
        return number.toString();
    }

    public String generateCardNumber() throws SQLException {
        while (true) {
            String cardNumber = generateRandomNumber(10);
            if (!isCardNumberExists(cardNumber)) {
                return cardNumber;
            }
        }
    }

    public String generateCardPIN() {
        StringBuilder cardPIN = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10);
            cardPIN.append(digit);
        }
        return cardPIN.toString();
    }

    private boolean isCardNumberExists(String cardNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM Card WHERE cardNumber = ?");
        preparedStatement.setString(1, cardNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count > 0;
    }

}
