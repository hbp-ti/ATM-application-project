DELIMITER //
CREATE PROCEDURE criarTabelas()
    BEGIN
        CREATE TABLE Holder (
            holderNIF INT(9) NOT NULL PRIMARY KEY,
            holderClientName VARCHAR(40) NOT NULL,
            holderAddress VARCHAR(50) NOT NULL,
            holderZipCode VARCHAR(10) NOT NULL,
            holderPhoneNumber INT(9) NOT NULL,
            holderEmail VARCHAR(50),
            holderBirthDate DATE NOT NULL,
            holderMaritalStatus VARCHAR(10) NOT NULL,
            holderGender VARCHAR(10) NOT NULL
            );

        CREATE TABLE BankAccount (
            accountNumber VARCHAR(20) NOT NULL PRIMARY KEY,
            accountBalance DECIMAL(15,2) DEFAULT 0,
            clientName VARCHAR(40) NOT NULL,
            NIF INT(9) NOT NULL,
            address VARCHAR(50) NOT NULL,
            zipCode VARCHAR(10) NOT NULL,
            phoneNumber INT(9) NOT NULL,
            email VARCHAR(50),
            birthDate DATE NOT NULL,
            maritalStatus VARCHAR(10) NOT NULL,
            gender VARCHAR(10) NOT NULL
            );

        CREATE TABLE HolderAccount (
            NIFholder INT(9),
            accountNumberHolder VARCHAR(20),
            holderType VARCHAR(10),
            cardNumber VARCHAR(10),

            PRIMARY KEY (NIFholder, accountNumberHolder),
            FOREIGN KEY (cardNumber) REFERENCES Card(cardPIN),
            FOREIGN KEY (NIFholder) REFERENCES Holder(holderNIF),
            FOREIGN KEY (accountNumberHolder) REFERENCES BankAccount(accountNumber)
        );

        CREATE TABLE Card (
            cardNumber VARCHAR(10) NOT NULL PRIMARY KEY,
            accountNumber VARCHAR(20) UNIQUE NOT NULL,
            cardPIN VARCHAR(4) NOT NULL,

            FOREIGN KEY (accountNumber) REFERENCES BankAccount(accountNumber)
        );

        CREATE TABLE Movement (
            movementID INT AUTO_INCREMENT PRIMARY KEY,
            accountNumber VARCHAR(20) NOT NULL,
            movementDate DATETIME NOT NULL,
            movementType VARCHAR(30) NOT NULL,
            movementValue DECIMAL(8,2),
            movementDescription VARCHAR(40) NOT NULL,

            FOREIGN KEY (accountNumber) REFERENCES BankAccount(accountNumber)
            );
    END; //
DELIMITER ;

CALL criarTabelas();