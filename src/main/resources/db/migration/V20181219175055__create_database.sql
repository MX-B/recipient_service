CREATE TABLE `recipient` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `uuid` VARCHAR(42) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `last_update_at` DATETIME,
  `active` BIT NOT NULL DEFAULT 0,

  `name` VARCHAR(64) NOT NULL,
  `bank_code` VARCHAR(8) NOT NULL,
  `document_number` VARCHAR(16) NOT NULL,
  `document_type` INT(3) NOT NULL,
  `agency` VARCHAR(8) NOT NULL,
  `agency_verification` VARCHAR(2) NOT NULL,
  `bank_account` VARCHAR(8) NOT NULL,
  `bank_account_verification` VARCHAR(2) NOT NULL,
  `bank_account_type` INT(3) NOT NULL,

  `transfer_interval` INT(3) NOT NULL,
  `transfer_day` INT(3) NOT NULL,
  `transfer_enabled` BIT NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_uuid___partner` (`uuid`)
) DEFAULT CHARSET=utf8;

CREATE TABLE `recipient_metadata` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `recipient_id` BIGINT(20) NOT NULL,
  `property` VARCHAR(32) NOT NULL,
  `value` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_plan_metadata___plan_property` (`recipient_id`, `property`),
  CONSTRAINT `FK_plan_metadata___plan` FOREIGN KEY (`recipient_id`) REFERENCES `recipient` (`id`)
) DEFAULT CHARSET=utf8;

CREATE TABLE `bank` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `bank_code` VARCHAR(8) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bank_code___bank` (`bank_code`)
) DEFAULT CHARSET=utf8;