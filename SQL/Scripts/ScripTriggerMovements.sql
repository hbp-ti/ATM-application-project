DELIMITER $$
CREATE TRIGGER calcular_saldo
AFTER INSERT ON Movement
FOR EACH ROW
BEGIN
    -- Verificar o tipo de movimento
    IF (NEW.movementType = 'Credit') THEN
        -- Atualizar o saldo da conta
        UPDATE BankAccount
        SET accountBalance = accountBalance + NEW.movementValue
        WHERE accountNumber = NEW.accountNumber;
    ELSEIF (NEW.movementType = 'Debit') THEN
        -- Atualizar o saldo da conta
        UPDATE BankAccount
        SET accountBalance = accountBalance - NEW.movementValue
        WHERE accountNumber = NEW.accountNumber;
    END IF;
END $$
DELIMITER ;