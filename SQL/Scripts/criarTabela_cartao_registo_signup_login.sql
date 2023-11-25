CREATE TABLE BankAccount (
	accountNumber INT(20) UNIQUE NOT NULL PRIMARY KEY,
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

CREATE TABLE Movement (
	movementID INT(8) UNIQUE NOT NULL PRIMARY KEY,
	cardNumber INT(10) UNIQUE NOT NULL,
    movementDate DATETIME NOT NULL,
    movementType VARCHAR(30) NOT NULL,
    movementValue DECIMAL(8,2),
    
    FOREIGN KEY (cardNumber) REFERENCES Card(cardNumber)
    );
    
CREATE TABLE Card (
	cardNumber INT(10) UNIQUE NOT NULL PRIMARY KEY,
    accountNumber INT(20) UNIQUE NOT NULL,
    cardPIN VARCHAR(100) NOT NULL,
    
    FOREIGN KEY (accountNumber) REFERENCES BankAccount(accountNumber)
    );
    