-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bank_atm_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bank_atm_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bank_atm_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `bank_atm_db` ;

-- -----------------------------------------------------
-- Table `bank_atm_db`.`accounts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bank_atm_db`.`accounts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `account_number` VARCHAR(50) NOT NULL,
  `balance` DECIMAL(15,2) NOT NULL DEFAULT '0.00',
  `currency` VARCHAR(3) NOT NULL DEFAULT 'USD',
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `account_number` (`account_number` ASC) VISIBLE,
  INDEX `idx_account_number` (`account_number` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 53
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bank_atm_db`.`atms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bank_atm_db`.`atms` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `location` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `is_active` TINYINT(1) NULL DEFAULT '1',
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_location` (`location` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bank_atm_db`.`atm_banknotes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bank_atm_db`.`atm_banknotes` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `atm_id` BIGINT NULL DEFAULT NULL,
  `currency` VARCHAR(3) NOT NULL,
  `denomination` DECIMAL(10,2) NOT NULL,
  `quantity` INT NOT NULL DEFAULT '0',
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `unique_atm_currency_denomination` (`atm_id` ASC, `currency` ASC, `denomination` ASC) VISIBLE,
  INDEX `idx_currency` (`currency` ASC) VISIBLE,
  INDEX `idx_atm` (`atm_id` ASC) VISIBLE,
  CONSTRAINT `atm_banknotes_ibfk_1`
    FOREIGN KEY (`atm_id`)
    REFERENCES `bank_atm_db`.`atms` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bank_atm_db`.`transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bank_atm_db`.`transactions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `from_account_id` BIGINT NULL DEFAULT NULL,
  `to_account_id` BIGINT NULL DEFAULT NULL,
  `amount` DECIMAL(15,2) NOT NULL,
  `currency` VARCHAR(3) NOT NULL,
  `transaction_type` ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') NOT NULL,
  `status` ENUM('PENDING', 'COMPLETED', 'FAILED', 'ROLLED_BACK') NOT NULL DEFAULT 'PENDING',
  `processed_at` TIMESTAMP NULL DEFAULT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_from_account` (`from_account_id` ASC) VISIBLE,
  INDEX `idx_to_account` (`to_account_id` ASC) VISIBLE,
  INDEX `idx_created_at` (`created_at` ASC) VISIBLE,
  CONSTRAINT `transactions_ibfk_1`
    FOREIGN KEY (`from_account_id`)
    REFERENCES `bank_atm_db`.`accounts` (`id`)
    ON DELETE SET NULL,
  CONSTRAINT `transactions_ibfk_2`
    FOREIGN KEY (`to_account_id`)
    REFERENCES `bank_atm_db`.`accounts` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
AUTO_INCREMENT = 56
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bank_atm_db`.`deposits`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bank_atm_db`.`deposits` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `transaction_id` BIGINT NOT NULL,
  `atm_id` BIGINT NULL DEFAULT NULL,
  `currency` VARCHAR(3) NOT NULL,
  `total_amount` DECIMAL(15,2) NOT NULL,
  `processed_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_transaction` (`transaction_id` ASC) VISIBLE,
  INDEX `idx_atm` (`atm_id` ASC) VISIBLE,
  CONSTRAINT `deposits_ibfk_1`
    FOREIGN KEY (`transaction_id`)
    REFERENCES `bank_atm_db`.`transactions` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `deposits_ibfk_2`
    FOREIGN KEY (`atm_id`)
    REFERENCES `bank_atm_db`.`atms` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
AUTO_INCREMENT = 38
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bank_atm_db`.`deposit_banknotes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bank_atm_db`.`deposit_banknotes` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `deposit_id` BIGINT NOT NULL,
  `denomination` DECIMAL(10,2) NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_deposit` (`deposit_id` ASC) VISIBLE,
  CONSTRAINT `deposit_banknotes_ibfk_1`
    FOREIGN KEY (`deposit_id`)
    REFERENCES `bank_atm_db`.`deposits` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bank_atm_db`.`payment_cards`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bank_atm_db`.`payment_cards` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `card_number` VARCHAR(50) NOT NULL,
  `account_id` BIGINT NOT NULL,
  `pin_code` VARCHAR(10) NOT NULL,
  `expiry_date` DATE NULL DEFAULT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `card_number` (`card_number` ASC) VISIBLE,
  INDEX `idx_card_number` (`card_number` ASC) VISIBLE,
  INDEX `idx_account` (`account_id` ASC) VISIBLE,
  CONSTRAINT `payment_cards_ibfk_1`
    FOREIGN KEY (`account_id`)
    REFERENCES `bank_atm_db`.`accounts` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bank_atm_db`.`persons`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bank_atm_db`.`persons` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `full_name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `phone_number` VARCHAR(50) NULL DEFAULT NULL,
  `account_id` BIGINT NULL DEFAULT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_account` (`account_id` ASC) VISIBLE,
  INDEX `idx_email` (`email` ASC) VISIBLE,
  CONSTRAINT `persons_ibfk_1`
    FOREIGN KEY (`account_id`)
    REFERENCES `bank_atm_db`.`accounts` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bank_atm_db`.`withdrawals`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bank_atm_db`.`withdrawals` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT NOT NULL,
  `transaction_id` BIGINT NOT NULL,
  `atm_id` BIGINT NULL DEFAULT NULL,
  `currency` VARCHAR(3) NOT NULL,
  `total_amount` DECIMAL(15,2) NOT NULL,
  `processed_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_account` (`account_id` ASC) VISIBLE,
  INDEX `idx_transaction` (`transaction_id` ASC) VISIBLE,
  INDEX `idx_atm` (`atm_id` ASC) VISIBLE,
  CONSTRAINT `withdrawals_ibfk_1`
    FOREIGN KEY (`account_id`)
    REFERENCES `bank_atm_db`.`accounts` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `withdrawals_ibfk_2`
    FOREIGN KEY (`transaction_id`)
    REFERENCES `bank_atm_db`.`transactions` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `withdrawals_ibfk_3`
    FOREIGN KEY (`atm_id`)
    REFERENCES `bank_atm_db`.`atms` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bank_atm_db`.`withdrawal_banknotes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bank_atm_db`.`withdrawal_banknotes` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `withdrawal_id` BIGINT NOT NULL,
  `denomination` DECIMAL(10,2) NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_withdrawal` (`withdrawal_id` ASC) VISIBLE,
  CONSTRAINT `withdrawal_banknotes_ibfk_1`
    FOREIGN KEY (`withdrawal_id`)
    REFERENCES `bank_atm_db`.`withdrawals` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
