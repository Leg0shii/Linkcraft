CREATE TABLE IF NOT EXISTS `{p}players`
(
    `user_id`    VARCHAR(36) NOT NULL,
    `rank`       TINYINT     NOT NULL DEFAULT (1),
    `bonus_rank` TINYINT     NOT NULL DEFAULT (1),
    `maze_rank`  TINYINT     NOT NULL DEFAULT (1),
    `tag_id`     MEDIUMINT   NOT NULL DEFAULT (1),
    PRIMARY KEY (`user_id`)
);

CREATE TABLE IF NOT EXISTS `{p}tags`
(
    `tag_id`      MEDIUMINT AUTO_INCREMENT NOT NULL,
    `name`        VARCHAR(100)             NOT NULL,
    `rarity`      TINYINT                  NOT NULL DEFAULT (1),
    `description` TEXT,
    PRIMARY KEY (`tag_id`)
);

CREATE TABLE IF NOT EXISTS `{p}player_tags`
(
    `user_id` VARCHAR(36) NOT NULL,
    `tag_id`  MEDIUMINT   NOT NULL DEFAULT (1),
    `date`    DATE                 DEFAULT (CURRENT_DATE),
    PRIMARY KEY (`user_id`, `tag_id`),
    FOREIGN KEY (`user_id`) REFERENCES `{p}players` (`user_id`),
    FOREIGN KEY (`tag_id`) REFERENCES `{p}tags` (`tag_id`)
);

CREATE TABLE IF NOT EXISTS `{p}command_cooldowns`
(
    `command`  VARCHAR(100) NOT NULL,
    `message`  VARCHAR(100) NOT NULL,
    `cooldown` INT          NOT NULL,
    PRIMARY KEY (`command`)
);

CREATE TABLE IF NOT EXISTS `{p}effect_sets`
(
    `id` VARCHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `{p}locations`
(
    `id`    MEDIUMINT AUTO_INCREMENT NOT NULL,
    `world` VARCHAR(36)              NOT NULL,
    `x`     DOUBLE                   NOT NULL,
    `y`     DOUBLE                   NOT NULL,
    `z`     DOUBLE                   NOT NULL,
    `yaw`   DOUBLE                   NOT NULL,
    `pitch` DOUBLE                   NOT NULL,
    PRIMARY KEY (`id`)
);

-- Chose MEDIUMINT because I doubt we will ever go over 8 million signs lol (feel free to change if you would like)
-- This table will store signs/heads with right click events (rankup signs/teleport heads/segmented cps)
-- Will likely have a foreign key to an effects table
CREATE TABLE IF NOT EXISTS `{p}effect_blocks`
(
    `id`     MEDIUMINT AUTO_INCREMENT NOT NULL,
    `set_id` VARCHAR(36)              NOT NULL,
    `world`  VARCHAR(36)              NOT NULL,
    `x`      INT                      NOT NULL,
    `y`      INT                      NOT NULL,
    `z`      INT                      NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`set_id`) REFERENCES `{p}effect_sets` (`id`)
);

CREATE TABLE IF NOT EXISTS `{p}effects`
(
    `id`     MEDIUMINT AUTO_INCREMENT NOT NULL,
    `set_id` VARCHAR(36)              NOT NULL,
    `type`   VARCHAR(100)             NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`set_id`) REFERENCES `{p}effect_sets` (`id`)
);

CREATE TABLE IF NOT EXISTS `{p}checkpoint_effects`
(
    `id`          MEDIUMINT AUTO_INCREMENT NOT NULL,
    `location_id` MEDIUMINT                NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`id`) REFERENCES `{p}effects` (`id`),
    FOREIGN KEY (`location_id`) REFERENCES `{p}locations` (`id`)
);


-- working example for longer cooldown commands
--INSERT IGNORE INTO `{p}command_cooldowns` (command, cooldown) VALUES ("gg", 45000);
--INSERT IGNORE INTO `{p}command_cooldowns` (command, cooldown) VALUES ("gl", 45000);
--INSERT IGNORE INTO `{p}command_cooldowns` (command, cooldown) VALUES ("rip", 45000);
--INSERT IGNORE INTO `{p}command_cooldowns` (command, cooldown) VALUES ("ham", 45000);
--INSERT IGNORE INTO `{p}command_cooldowns` (command, cooldown) VALUES ("bacon", 45000);
--INSERT IGNORE INTO `{p}command_cooldowns` (command, cooldown) VALUES ("eggs", 45000);
