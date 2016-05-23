-- Script de criação do banco de dados

DROP DATABASE IF EXISTS oraculo;

CREATE DATABASE oraculo;

USE oraculo;

-- Tabela de países
CREATE TABLE IF NOT EXISTS `countries` (
  `id`      INT         NOT NULL AUTO_INCREMENT,
  `country` VARCHAR(64) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabela de estados/regiões
CREATE TABLE IF NOT EXISTS `states` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `country_id` INT         NOT NULL,
  `state`      VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabela de cidades
CREATE TABLE IF NOT EXISTS `cities` (
  `id`       INT         NOT NULL AUTO_INCREMENT,
  `state_id` INT         NOT NULL,
  `city`     VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`state_id`) REFERENCES `states` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabela com o nome do tipo de usuários do sistema
CREATE TABLE IF NOT EXISTS `levels` (
  `id`    SMALLINT    NOT NULL AUTO_INCREMENT,
  `level` VARCHAR(64) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabela de profissões
CREATE TABLE IF NOT EXISTS `occupations` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `occupation` VARCHAR(64) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabela com o nome de níveis de escolaridade
CREATE TABLE IF NOT EXISTS `educations` (
  `id`        INT         NOT NULL AUTO_INCREMENT,
  `education` VARCHAR(64) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabela de usuários
CREATE TABLE IF NOT EXISTS `users` (
  `email`                  VARCHAR(255) NOT NULL UNIQUE,
  `name`                   VARCHAR(128) NOT NULL,
  `password`               VARCHAR(64)  NOT NULL,
  `city_id`                INT          NOT NULL,
  `level_id`               SMALLINT     NOT NULL DEFAULT 1,
  `active`                 TINYINT(1)   NOT NULL DEFAULT 1,
  `password_token`         VARCHAR(64)  NULL,
  `password_token_expires` TIMESTAMP    NULL,
  `birthyear`              YEAR         NULL,
  `occupation_id`          INT          NULL,
  `education_id`           INT          NULL,
  `created`                TIMESTAMP    NULL,
  `modified`               TIMESTAMP    NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`email`),
  FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`),
  FOREIGN KEY (`level_id`) REFERENCES `levels` (`id`),
  FOREIGN KEY (`occupation_id`) REFERENCES `occupations` (`id`),
  FOREIGN KEY (`education_id`) REFERENCES `educations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabela de arquivos
CREATE TABLE IF NOT EXISTS `files` (
  `file`       VARCHAR(255) NOT NULL UNIQUE,
  `user_email` VARCHAR(255) NOT NULL,
  `date`       DATE         NOT NULL,
  PRIMARY KEY (`file`),
  FOREIGN KEY (`user_email`) REFERENCES `users` (`email`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabela de categorias
CREATE TABLE IF NOT EXISTS `categories` (
  `id`       INT                        NOT NULL AUTO_INCREMENT,
  `category` VARCHAR(64)                NOT NULL,
  `type`     ENUM('Despesa', 'Receita') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabela de transações
CREATE TABLE IF NOT EXISTS `transactions` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `user_email`  VARCHAR(255) NOT NULL,
  `category_id` INT          NOT NULL,
  `date`        DATE         NOT NULL,
  `value`       DOUBLE(11,2) NOT NULL,
  PRIMARY KEY (`user_email`, `id`),
  FOREIGN KEY (`user_email`) REFERENCES `users` (`email`)
    ON DELETE CASCADE,
  FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
    ON UPDATE CASCADE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
