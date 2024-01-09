package SQL;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

/**
 * Classe que fornece métodos para executar consultas no banco de dados relacionadas a operações bancárias.
 */
public class Query {
    /**
     * Declaração preparada utilizada para executar consultas parametrizadas.
     */
    private PreparedStatement preparedStatement;

    /**
     * Resultado de uma consulta SQL.
     */
    private ResultSet rs;

    /**
     * Resultado de uma consulta SQL para obter e-mails.
     */
    private ResultSet rsEmail;

    /**
     * Resultado de uma consulta SQL para obter nomes.
     */
    private ResultSet rsName;

    /**
     * Resultado de uma consulta SQL para obter número de cartões.
     */
    private ResultSet rsCard;

    /**
     * Resultado de uma consulta SQL para obter numero da conta.
     */
    private ResultSet rsAccount;


    /**
     * Conexão com o banco de dados obtida da classe Conn.
     */
    private Connection connection = Conn.getConnection();

    /**
     * Obtém o saldo disponível para uma determinada conta.
     *
     * @param clientAccountNumber Número da conta do cliente.
     * @return O saldo disponível da conta.
     */
    public float getAvailableBalance(String clientAccountNumber) {
        try {
            String query = "SELECT accountBalance FROM BankAccount WHERE accountNumber = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientAccountNumber);
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


    /**
     * Verifica o saldo de uma determinada conta.
     *
     * @param clientAccountNumber Número da conta do cliente.
     * @return O saldo da conta.
     */
    public BigDecimal checkBalance(String clientAccountNumber) {
        try {
            String query = "SELECT accountBalance FROM BankAccount WHERE accountNumber = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientAccountNumber);
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

    /**
     * Registra uma operação de movimentação na conta do cliente.
     *
     * @param clientAccountNumber Número da conta do cliente.
     * @param type             Tipo de movimentação (por exemplo, "Débito" ou "Crédito").
     * @param value            Valor da movimentação.
     * @param description      Descrição da movimentação.
     * @return True se a movimentação for registrada com sucesso; false, caso contrário.
     * @throws SQLException Exceção lançada se houver um problema durante a execução da consulta SQL.
     */
    public boolean movement(String clientAccountNumber, String type, float value, String description) throws SQLException {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO Movement (accountNumber, movementDate, movementType, movementValue, movementDescription) VALUES (?, NOW(), ?, ?, ?)");
            preparedStatement.setString(1, clientAccountNumber);
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

    /**
     * Registra uma operação de movimentação associada a um número de telefone.
     *
     * @param phoneNumber      Número de telefone do cliente.
     * @param clientAccountNumber Número da conta do cliente.
     * @param type             Tipo de movimentação (por exemplo, "Débito" ou "Crédito").
     * @param value            Valor da movimentação.
     * @param description      Descrição da movimentação.
     * @return True se a movimentação for registrada com sucesso; false, caso contrário.
     * @throws SQLException Exceção lançada se houver um problema durante a execução da consulta SQL.
     */
    public boolean movementPhone(String phoneNumber, String clientAccountNumber, String type, float value, String description) throws SQLException {
        if(doesPhoneNumberExist(phoneNumber)) {
            try {
                preparedStatement = connection.prepareStatement("INSERT INTO Movement (accountNumber, movementDate, movementType, movementValue, movementDescription) VALUES (?, NOW(), ?, ?, ?)");
                preparedStatement.setString(1, clientAccountNumber);
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

    /**
     * Verifica se um número de conta existe na tabela BankAccount do banco de dados.
     *
     * @param accountNumberClient Número da conta a ser verificada.
     * @return true se o número do cartão existe na tabela Card, false caso contrário.
     */
    public boolean doesAccountExist(String accountNumberClient) {
        // Lógica para verificar se o número da conta existe na tabela BankAccount
        String sql = "SELECT COUNT(*) FROM BankAccount WHERE accountNumber = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, accountNumberClient);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Retorna true se a conta existe
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Trate adequadamente a exceção
        }

        return false; // Retorna false se houver um erro ou conta não existir
    }

    /**
     * Obtém o número do cartão do cliente associado a um número de conta.
     *
     * @param clientAccountNumber Número da conta do cliente.
     * @return O número do cartão do cliente associado ao número da conta.
     */
    public String getClientCardNumber(String clientAccountNumber) {
        try {
            String query = "SELECT cardNumber FROM Card WHERE accountNumber = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientAccountNumber);
            rsCard = preparedStatement.executeQuery();

            if (rsCard.next()) {
                return rsCard.getString("cardNumber");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsCard != null) {
                    rsCard.close();
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

    /**
     * Obtém o nome do cliente associado a um número de conta.
     *
     * @param clientAccountNumber Número da conta do cliente.
     * @return O nome do cliente associado ao número da conta.
     */
    public String getClientName(String clientAccountNumber) {
        try {
            String query = "SELECT clientName FROM BankAccount WHERE accountNumber = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientAccountNumber);
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

    /**
     * Obtém o endereço de e-mail do cliente associado a um número de conta.
     *
     * @param clientAccountNumber Número da conta do cliente.
     * @return O endereço de e-mail do cliente associado ao número da conta.
     */
    public String getClientEmail(String clientAccountNumber) {
        try {
            String query = "SELECT email FROM BankAccount WHERE accountNumber = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientAccountNumber);
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

    /**
     * Insere dados de uma nova conta bancária no banco de dados.
     *
     * @param name    Nome do cliente.
     * @param NIF     NIF do cliente.
     * @param address Endereço do cliente.
     * @param zipCode Código postal do cliente.
     * @param phone   Número de telefone do cliente.
     * @param email   Endereço de e-mail do cliente.
     * @param date    Data de nascimento do cliente.
     * @param marital Estado civil do cliente.
     * @param gender  Gênero do cliente.
     * @return O número da conta bancária gerado.
     * @throws SQLException Exceção lançada se houver um problema durante a execução da consulta SQL.
     */
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

    /**
     * Insere dados de um novo cartão no banco de dados.
     *
     * @param accountNumber Número da conta associada ao cartão.
     * @return Um array contendo o número do cartão e o PIN gerados.
     * @throws SQLException Exceção lançada se houver um problema durante a execução da consulta SQL.
     */
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

    /**
     * Obtém o número da conta do cliente associado a um número de cartão.
     *
     * @param clientCardNumber Número do cartão do cliente.
     * @return O número da conta do cliente associado ao número do cartão.
     */
    public String getAccountNumber(String clientCardNumber) {
        try {
            String query = "SELECT accountNumber FROM Card WHERE cardNumber = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientCardNumber);
            rsAccount = preparedStatement.executeQuery();

            if (rsAccount.next()) {
                return rsAccount.getString("accountNumber");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsAccount != null) {
                    rsAccount.close();
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

    /**
     * Carrega um extrato bancário resumido para um determinado cliente.
     *
     * @param clientAccountNumber Número da conta do cliente.
     * @return Um StringBuilder contendo as últimas 15 movimentações da conta do cliente.
     */
    public StringBuilder loadMiniStatement(String clientAccountNumber) {
        StringBuilder miniStatement = new StringBuilder();
        try {
            String query = "SELECT * FROM Movement WHERE accountNumber = ? ORDER BY movementDate DESC LIMIT 15";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, clientAccountNumber);
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

    /**
     * Obtém o gênero associado a um número de conta.
     *
     * @param clientAccountNumber Número da conta para o qual se deseja obter o gênero.
     * @return O gênero associado à conta bancária vinculada ao número da conta.
     */
    public String getGenderFromDatabase(String clientAccountNumber) {
        String gender = null;
        // Obtém o gênero usando o número da conta
        String getGenderQuery = "SELECT gender FROM BankAccount WHERE accountNumber = ?";
        try (PreparedStatement genderStatement = connection.prepareStatement(getGenderQuery)) {
            genderStatement.setString(1, clientAccountNumber);
            ResultSet genderResultSet = genderStatement.executeQuery();

            if (genderResultSet.next()) {
                gender = genderResultSet.getString("gender");
            }
        } catch (SQLException e) {
            System.out.println("Erro a obter género da base de dados: " + e.getMessage());
        }
        return gender;
    }

    /**
     * Verifica se as informações do cartão são válidas.
     *
     * @param clientCardNumber Número do cartão do cliente.
     * @param password         PIN do cartão.
     * @return True se as informações do cartão forem válidas; false, caso contrário.
     */
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

    /**
     * Verifica se um número de telefone já existe no banco de dados.
     *
     * @param phoneNumber Número de telefone a ser verificado.
     * @return True se o número de telefone já existir; false, caso contrário.
     * @throws SQLException Exceção lançada se houver um problema durante a execução da consulta SQL.
     */
    public boolean doesPhoneNumberExist(String phoneNumber) throws SQLException {
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

    /**
     * Altera o PIN de um cartão no banco de dados.
     *
     * @param cardNumber  Número do cartão.
     * @param currentPIN  PIN atual do cartão.
     * @param newPIN      Novo PIN desejado.
     * @return True se o PIN for alterado com sucesso; false, caso contrário.
     */
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

    /**
     * Obtém o PIN armazenado no banco de dados associado a um número de cartão.
     *
     * @param cardNumber Número do cartão.
     * @return O PIN associado ao número do cartão.
     */
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

    /**
     * Gera um número de conta único e não existente no banco de dados.
     *
     * @return O número de conta gerado.
     * @throws SQLException Exceção lançada se houver um problema durante a execução da consulta SQL.
     */
    public String generateAccountNumber() throws SQLException {
        while (true) {
            String accountNumber = generateRandomNumber(20);
            if (!isAccountNumberExists(accountNumber)) {
                return accountNumber;
            }
        }
    }

    /**
     * Verifica se um número de conta já existe no banco de dados.
     *
     * @param accountNumber Número de conta a ser verificado.
     * @return True se o número de conta já existir; false, caso contrário.
     * @throws SQLException Exceção lançada se houver um problema durante a execução da consulta SQL.
     */
    boolean isAccountNumberExists(String accountNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM BankAccount WHERE accountNumber = ?");
        preparedStatement.setString(1, accountNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count > 0;
    }

    /**
     * Gera um número aleatório com o comprimento especificado.
     *
     * @param length Comprimento do número gerado.
     * @return O número aleatório gerado.
     */
    public String generateRandomNumber(int length) {
        StringBuilder number = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            number.append(digit);
        }
        return number.toString();
    }

    /**
     * Gera um número de cartão único e não existente no banco de dados.
     *
     * @return O número de cartão gerado.
     * @throws SQLException Exceção lançada se houver um problema durante a execução da consulta SQL.
     */
    public String generateCardNumber() throws SQLException {
        while (true) {
            String cardNumber = generateRandomNumber(10);
            if (!isCardNumberExists(cardNumber)) {
                return cardNumber;
            }
        }
    }

    /**
     * Gera um PIN de cartão aleatório.
     *
     * @return O PIN de cartão gerado.
     */
    public String generateCardPIN() {
        StringBuilder cardPIN = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10);
            cardPIN.append(digit);
        }
        return cardPIN.toString();
    }

    /**
     * Verifica se um número de cartão já existe no banco de dados.
     *
     * @param cardNumber Número de cartão a ser verificado.
     * @return True se o número de cartão já existir; false, caso contrário.
     * @throws SQLException Exceção lançada se houver um problema durante a execução da consulta SQL.
     */
    public boolean isCardNumberExists(String cardNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM Card WHERE cardNumber = ?");
        preparedStatement.setString(1, cardNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count > 0;
    }
}
