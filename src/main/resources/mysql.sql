CREATE TABLE IF NOT EXISTS `lc_players`
(
    `user_id`    VARCHAR(36) NOT NULL,
    `rank`       TINYINT     NOT NULL DEFAULT (1),
    `bonus_rank` TINYINT     NOT NULL DEFAULT (1),
    `maze_rank`  TINYINT     NOT NULL DEFAULT (1),
    `tag_id`     MEDIUMINT   NOT NULL DEFAULT (1),
    `name`       VARCHAR(16) NOT NULL,
    PRIMARY KEY (`user_id`)
);

CREATE TABLE IF NOT EXISTS `lc_locations`
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

CREATE TABLE IF NOT EXISTS `lc_tags`
(
    `tag_id`      MEDIUMINT AUTO_INCREMENT NOT NULL,
    `name`        VARCHAR(100)             NOT NULL,
    `rarity`      TINYINT                  NOT NULL,
    `type`        TINYINT                  NOT NULL,
    `description` TEXT,
    PRIMARY KEY (`tag_id`)
);

CREATE TABLE IF NOT EXISTS `lc_player_tags`
(
    `user_id` VARCHAR(36) NOT NULL,
    `tag_id`  MEDIUMINT   NOT NULL DEFAULT (1),
    `date`    DATE                 DEFAULT (CURRENT_DATE),
    PRIMARY KEY (`user_id`, `tag_id`),
    FOREIGN KEY (`user_id`) REFERENCES `lc_players` (`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `lc_tags` (`tag_id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `lc_maps`
(
    `id`                MEDIUMINT AUTO_INCREMENT NOT NULL,
    `name`              VARCHAR(100)             NOT NULL,
    `type`              TINYINT                  NOT NULL,
    `length`            TINYINT   DEFAULT (1)    NOT NULL,
    `difficulty`        DOUBLE                   NOT NULL,
    `builder_names`     VARCHAR(200)             NOT NULL,
    `release_date`      DATE      DEFAULT (CURRENT_DATE),
    `spawn_location_id` MEDIUMINT DEFAULT (0),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`spawn_location_id`) REFERENCES `lc_locations` (`id`)
);

// rename to map_attempts
// add jump count
// add playtime
// add last time played
CREATE TABLE IF NOT EXISTS `lc_map_completions`
(
    `id`              INT AUTO_INCREMENT NOT NULL,
    `map_id`          MEDIUMINT          NOT NULL,
    `user_id`         VARCHAR(36)        NOT NULL,
    `completion`      BOOL DEFAULT (FALSE),
    `completion_date` DATE DEFAULT (CURRENT_DATE),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `lc_players` (`user_id`),
    FOREIGN KEY (`map_id`) REFERENCES `lc_maps` (`id`)
);

CREATE TABLE IF NOT EXISTS `lc_command_cooldowns`
(
    `command`  VARCHAR(100) NOT NULL,
    `message`  VARCHAR(100) NOT NULL,
    `cooldown` INT          NOT NULL,
    PRIMARY KEY (`command`)
);

CREATE TABLE IF NOT EXISTS `lc_effect_sets`
(
    `id` VARCHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
);



-- Chose MEDIUMINT because I doubt we will ever go over 8 million signs lol (feel free to change if you would like)
-- This table will store signs/heads with right click events (rankup signs/teleport heads/segmented cps)
-- Will likely have a foreign key to an effects table
CREATE TABLE IF NOT EXISTS `lc_effect_blocks`
(
    `id`     MEDIUMINT AUTO_INCREMENT NOT NULL,
    `set_id` VARCHAR(36)              NOT NULL,
    `world`  VARCHAR(36)              NOT NULL,
    `x`      INT                      NOT NULL,
    `y`      INT                      NOT NULL,
    `z`      INT                      NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`set_id`) REFERENCES `lc_effect_sets` (`id`)
);

CREATE TABLE IF NOT EXISTS `lc_effects`
(
    `id`     MEDIUMINT AUTO_INCREMENT NOT NULL,
    `set_id` VARCHAR(36)              NOT NULL,
    `type`   VARCHAR(100)             NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`set_id`) REFERENCES `lc_effect_sets` (`id`)
);

CREATE TABLE IF NOT EXISTS `lc_checkpoint_effects`
(
    `id`          MEDIUMINT AUTO_INCREMENT NOT NULL,
    `location_id` MEDIUMINT                NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`id`) REFERENCES `lc_effects` (`id`),
    FOREIGN KEY (`location_id`) REFERENCES `lc_locations` (`id`)
);


-- working example for longer cooldown commands
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("gg", 45000);
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("gl", 45000);
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("rip", 45000);
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("ham", 45000);
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("bacon", 45000);
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("eggs", 45000);
