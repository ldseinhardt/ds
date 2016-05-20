-- Script de criação do banco de dados
CREATE DATABASE oraculo/*
        DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci*/;
        /*SQL_Latin1_General_CP1_CI_AI*/
USE oraculo;

CREATE TABLE Cidade (
    idCidade         INT             NOT NULL AUTO_INCREMENT,
    nomeCidade       VARCHAR(255)    NOT NULL,
    PRIMARY KEY (idCidade)
);

CREATE TABLE Estado (
    idEstado         INT             NOT NULL AUTO_INCREMENT,
    nomeEstado       VARCHAR(255)    NOT NULL,
    PRIMARY KEY (idEstado)
);

CREATE TABLE Pais (
    idPais           INT             NOT NULL AUTO_INCREMENT,
    nomePais         VARCHAR(255)    NOT NULL,
    PRIMARY KEY (idPais)
);

CREATE TABLE NivelAcesso (
    nivel            SMALLINT        NOT NULL,
    nomeNivel        VARCHAR(255),
    PRIMARY KEY (nivel)
);

CREATE TABLE Usuario (
    email            VARCHAR(255)    NOT NULL,
    senha            VARCHAR(255)    NOT NULL,
    profissao        VARCHAR(255),
    dataNasc         DATE,
    nivel            SMALLINT        NOT NULL DEFAULT 1,
    cidade           INT,
    estado           INT,
    pais             INT,
    PRIMARY KEY (email),
    FOREIGN KEY (nivel) REFERENCES NivelAcesso(nivel),
    FOREIGN KEY (cidade) REFERENCES Cidade(idCidade),
    FOREIGN KEY (estado) REFERENCES Estado(idEstado),
    FOREIGN KEY (pais) REFERENCES Pais(idPais)
) ENGINE = InnoDB;

CREATE TABLE Categoria (
    idCategoria      INT             NOT NULL AUTO_INCREMENT,
    tipoCategoria    ENUM('Receita',
	                      'Despesa') NOT NULL,
    nomeCategoria    VARCHAR(255)    NOT NULL,
    PRIMARY KEY (idCategoria)
);

CREATE TABLE Transacao (
    email            VARCHAR(255)    NOT NULL,
    idTransacao      BIGINT          NOT NULL AUTO_INCREMENT,
    dataTransacao    DATE            NOT NULL,
    valor            DOUBLE(11,2)    NOT NULL,
    categoria        INT             NOT NULL,
    PRIMARY KEY (email, idTransacao),
    FOREIGN KEY (email) REFERENCES Usuario(email)
        ON DELETE CASCADE,
    FOREIGN KEY (categoria) REFERENCES Categoria(idCategoria)
        ON UPDATE CASCADE
) ENGINE = MyISAM;
