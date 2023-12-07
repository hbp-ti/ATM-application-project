INSERT INTO BankAccount (accountNumber, accountBalance, clientName, NIF, address, zipCode, phoneNumber, email, birthDate, maritalStatus, gender)
	VALUES (18738053, 18645.74, 'João Alvalade Ferdinándes', 123287529, 'Rua agueda outubro de natal, 12','7620-592', 987654321, 'aguedaenatalnaprimavera@gmail.com', '2020-04-12', 'Casado', 'BinarioPDF'),
		   (12345678, 35421.54, 'Maria da Silva', 587349201, 'Rua Principal, 123', '12345-678', 987654321, 'maria.silva@email.com', '1985-09-22', 'Casado', 'Feminino'),
		   (98765432, 23000.87, 'João Oliveira', 987654321, 'Avenida Central, 456', '54321-098', 123456789, 'joao.oliveira@email.com', '1990-05-15', 'Solteiro', 'Masculino'),
		   (56789012, 1765.32, 'Ana Santos', 345678901, 'Praça dos Sonhos, 789', '67890-123', 987612345, 'ana.santos@email.com', '1988-11-30', 'Divorciado', 'Feminino'),
		   (34567890, 43000.21, 'Pedro Ferreira', 567890123, 'Rua da Amizade, 456', '45678-901', 876543210, 'pedro.ferreira@email.com', '1977-03-18', 'Casado', 'Masculino'),
		   (90123456, 825.99, 'Marta Costa', 123456789, 'Avenida dos Girassóis, 789', '78901-234', 765432109, 'marta.costa@email.com', '2000-08-25', 'Solteiro', 'Feminino'),
           (24681357, 48239.21, 'Rui Santos', 876543210, 'Rua da Liberdade, 567', '23456-789', 654321098, 'rui.santos@email.com', '1974-12-08', 'Casado', 'Masculino'),
		   (13579246, 15327.45, 'Sofia Oliveira', 234567890, 'Avenida Central, 789', '34567-890', 543210987, 'sofia.oliveira@email.com', '1989-06-17', 'Solteiro', 'Feminino'),
		   (98765313, 90000.67, 'Carlos Pereira', 654321098, 'Rua dos Pássaros, 234', '45678-901', 432109876, 'carlos.pereira@email.com', '1980-04-25', 'Casado', 'Masculino'),
		   (65432109, 763.88, 'Mariana Costa', 765432109, 'Avenida das Flores, 876', '56789-012', 321098765, 'mariana.costa@email.com', '2002-10-30', 'Solteiro', 'Feminino'),
		   (78901234, 6725.50, 'Jorge Ferreira', 321098765, 'Praça da Paz, 345', '67890-123', 210987654, 'jorge.ferreira@email.com', '1995-02-14', 'Casado', 'Masculino');


INSERT INTO Card (cardNumber, accountNumber, cardPIN)
SELECT
    FLOOR(RAND() * 100000000) AS cardNumber, accountNumber, LPAD(FLOOR(RAND() * 10000), 4, '0') AS cardPIN
FROM BankAccount;

INSERT INTO Movement (movementID, cardNumber, movementDate, movementType, movementValue)
	SELECT FLOOR(RAND() * 100000000) AS movementID,
	       cardNumber,
	       DATE_ADD('2020-01-01', INTERVAL FLOOR(RAND() * 1096) DAY) AS movementDate,
	       'Levantamento' AS movementType,
	       ROUND(RAND() * 1000, 2) AS movementValue
	FROM Card;
