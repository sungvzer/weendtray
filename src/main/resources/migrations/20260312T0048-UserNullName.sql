ALTER TABLE `user`
ADD COLUMN `name_nullable` TEXT DEFAULT NULL;
UPDATE `user`
SET `name_nullable` = `name`;
ALTER TABLE `user` DROP COLUMN `name`;
ALTER TABLE `user`
    RENAME COLUMN `name_nullable` TO `name`;
ALTER TABLE `user`
ADD COLUMN `surname_nullable` TEXT DEFAULT NULL;
UPDATE `user`
SET `surname_nullable` = `surname`;
ALTER TABLE `user` DROP COLUMN `surname`;
ALTER TABLE `user`
    RENAME COLUMN `surname_nullable` TO `surname`;
