ALTER TABLE `recipient` ADD `removed_at` DATETIME NULL;
ALTER TABLE `recipient` ADD `updated_at` DATETIME NULL;

UPDATE `recipient` SET `updated_at` = `last_update_at`;
UPDATE `recipient` SET `removed_at` = NOW() WHERE `active` = 0;