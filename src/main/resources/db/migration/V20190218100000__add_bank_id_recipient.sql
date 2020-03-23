ALTER TABLE `recipient`
  ADD `bank_id` BIGINT(20) NULL AFTER `bank_code`;

ALTER TABLE `recipient`
  ADD CONSTRAINT `FK_recipient___bank` FOREIGN KEY (`bank_id`) REFERENCES `bank` (`id`);

UPDATE `recipient` r
SET r.`bank_id` = (SELECT b.id FROM `bank` b WHERE b.`bank_code` = r.`bank_code`);