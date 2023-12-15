ALTER TABLE HolderAccount
    ADD COLUMN cardNumber VARCHAR(10) NOT NULL,
    ADD CONSTRAINT FOREIGN KEY (cardNumber) REFERENCES Card(cardNumber),
    DROP PRIMARY KEY,
    ADD CONSTRAINT PRIMARY KEY (NIFholder, accountNumberHolder, holderType);


