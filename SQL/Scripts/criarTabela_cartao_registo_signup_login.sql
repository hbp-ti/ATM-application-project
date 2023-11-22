CREATE TABLE SignUp (
	indice INT NOT NULL PRIMARY KEY,
	nome VARCHAR(20) NOT NULL,
    NIF INT(9) UNIQUE NOT NULL,
    morada VARCHAR(50) NOT NULL,
    telefone INT(9) NOT NULL,
    email VARCHAR(30),
    dataNascimento DATE NOT NULL,
    estadoCivil VARCHAR(10) NOT NULL,
    genero VARCHAR(10) NOT NULL
    );
    
CREATE TABLE LogIn (
	indice INT NOT NULL PRIMARY KEY,
	numeroCartao INT(16) UNIQUE NOT NULL,
    pinCartao INT(4) NOT NULL,
    -- FOREIGN KEY (pinCartao) REFERENCES cartao(pinCartao),
    FOREIGN KEY (numeroCartao) REFERENCES cartao(numeroCartao)
    );

CREATE TABLE Registo (
	numeroConta INT NOT NULL PRIMARY KEY,
    numeroCartao INT(16) UNIQUE NOT NULL,
    dataRegisto date NOT NULL,
    tipoAcao VARCHAR(40) NOT NULL,
    montante FLOAT(8) NOT NULL
    );
    
CREATE TABLE cartao (
	numeroCartao INT(16) UNIQUE NOT NULL PRIMARY KEY,
    numeroConta INT UNIQUE NOT NULL,
    pinCartao INT(4) NOT NULL,
    FOREIGN KEY (numeroConta) REFERENCES Registo(numeroConta)
    );
    
    