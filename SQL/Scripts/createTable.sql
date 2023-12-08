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
            accountNumber INT(20) NOT NULL PRIMARY KEY,
            accountBalance DECIMAL(15,2),
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
            accountNumberHolder INT(20),
            holderType VARCHAR(10),

            PRIMARY KEY (NIFholder, accountNumberHolder),
            FOREIGN KEY (NIFholder) REFERENCES Holder(holderNIF),
            FOREIGN KEY (accountNumberHolder) REFERENCES BankAccount(accountNumber)
        );

        CREATE TABLE Card (
            cardNumber INT(10) UNIQUE NOT NULL PRIMARY KEY,
            accountNumber INT(20) UNIQUE NOT NULL,
            cardPIN VARCHAR(100) NOT NULL,

            FOREIGN KEY (accountNumber) REFERENCES BankAccount(accountNumber)
            );

        CREATE TABLE Movement (
            movementID INT(8) UNIQUE NOT NULL PRIMARY KEY,
            cardNumber INT(10) UNIQUE NOT NULL,
            movementDate DATETIME NOT NULL,
            movementType VARCHAR(30) NOT NULL,
            movementValue DECIMAL(8,2),

            FOREIGN KEY (cardNumber) REFERENCES Card(cardNumber)
            );
    END //
DELIMITER ;

CALL criarTabelas();