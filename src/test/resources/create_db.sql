-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema minha-audocao
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema minha-audocao
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `minha-audocao` DEFAULT CHARACTER SET utf8 ;
USE `minha-audocao` ;

-- -----------------------------------------------------
-- Table `minha-audocao`.`endereco`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `minha-audocao`.`endereco` ;

CREATE TABLE IF NOT EXISTS `minha-audocao`.`endereco` (
  `idendereco` INT NOT NULL AUTO_INCREMENT,
  `cidade` VARCHAR(45) NOT NULL,
  `estado` VARCHAR(45) NOT NULL,
  `logradouro` VARCHAR(45) NOT NULL,
  `numero` INT NULL,
  `cep` INT NOT NULL,
  PRIMARY KEY (`idendereco`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `minha-audocao`.`instituicao`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `minha-audocao`.`instituicao` ;

CREATE TABLE IF NOT EXISTS `minha-audocao`.`instituicao` (
  `idinstituicao` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `descricao` VARCHAR(4000) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `senha` VARCHAR(45) NOT NULL,
  `telefone` VARCHAR(45) NOT NULL,
  `imagem` VARCHAR(45) NOT NULL,
  `idendereco` INT NOT NULL,
  PRIMARY KEY (`idinstituicao`),
  INDEX `fk_instituicao_endereco1_idx` (`idendereco` ASC) VISIBLE,
  CONSTRAINT `fk_instituicao_endereco1`
    FOREIGN KEY (`idendereco`)
    REFERENCES `minha-audocao`.`endereco` (`idendereco`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `minha-audocao`.`pet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `minha-audocao`.`pet` ;

CREATE TABLE IF NOT EXISTS `minha-audocao`.`pet` (
  `idpet` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `especie` VARCHAR(45) NOT NULL,
  `descricao` VARCHAR(4000) NOT NULL,
  `imagem` VARCHAR(45) NULL,
  `adotado` TINYINT NOT NULL,
  `raca` VARCHAR(45) NULL,
  `idade` INT NULL,
  `idinstituicao` INT NOT NULL,
  PRIMARY KEY (`idpet`),
  INDEX `fk_pet_instituicao_idx` (`idinstituicao` ASC) VISIBLE,
  CONSTRAINT `fk_pet_instituicao`
    FOREIGN KEY (`idinstituicao`)
    REFERENCES `minha-audocao`.`instituicao` (`idinstituicao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `minha-audocao`.`evento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `minha-audocao`.`evento` ;

CREATE TABLE IF NOT EXISTS `minha-audocao`.`evento` (
  `idevento` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `descricao` VARCHAR(4000) NOT NULL,
  `idinstituicao` INT NOT NULL,
  `idendereco` INT NOT NULL,
  PRIMARY KEY (`idevento`),
  INDEX `fk_evento_instituicao1_idx` (`idinstituicao` ASC) VISIBLE,
  INDEX `fk_evento_endereco1_idx` (`idendereco` ASC) VISIBLE,
  CONSTRAINT `fk_evento_instituicao1`
    FOREIGN KEY (`idinstituicao`)
    REFERENCES `minha-audocao`.`instituicao` (`idinstituicao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_evento_endereco1`
    FOREIGN KEY (`idendereco`)
    REFERENCES `minha-audocao`.`endereco` (`idendereco`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `minha-audocao`.`data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `minha-audocao`.`data` ;

CREATE TABLE IF NOT EXISTS `minha-audocao`.`data` (
  `iddata` INT NOT NULL AUTO_INCREMENT,
  `idevento` INT NOT NULL,
  `data` DATETIME NOT NULL,
  `hora_inicio` INT NOT NULL,
  `hora_fim` INT NOT NULL,
  PRIMARY KEY (`iddata`),
  INDEX `fk_data_evento1_idx` (`idevento` ASC) VISIBLE,
  CONSTRAINT `fk_data_evento1`
    FOREIGN KEY (`idevento`)
    REFERENCES `minha-audocao`.`evento` (`idevento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `minha-audocao`.`formulario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `minha-audocao`.`formulario` ;

CREATE TABLE IF NOT EXISTS `minha-audocao`.`formulario` (
  `idformulario` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `tipo` VARCHAR(45) NOT NULL,
  `obrigatorio` TINYINT NOT NULL,
  `ordem` INT NOT NULL,
  `idinstituicao` INT NOT NULL,
  PRIMARY KEY (`idformulario`),
  INDEX `fk_formulario_instituicao1_idx` (`idinstituicao` ASC) VISIBLE,
  CONSTRAINT `fk_formulario_instituicao1`
    FOREIGN KEY (`idinstituicao`)
    REFERENCES `minha-audocao`.`instituicao` (`idinstituicao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `minha-audocao`.`pessoa`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `minha-audocao`.`pessoa` ;

CREATE TABLE IF NOT EXISTS `minha-audocao`.`pessoa` (
  `idpessoa` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `sobrenome` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `senha` VARCHAR(45) NOT NULL,
  `imagem` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idpessoa`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
