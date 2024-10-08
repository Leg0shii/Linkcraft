CREATE TABLE IF NOT EXISTS `lc_players`
(
    `user_id`    VARCHAR(36) NOT NULL,
    `rank`       TINYINT     NOT NULL DEFAULT (1),
    `bonus_rank` TINYINT     NOT NULL DEFAULT (1),
    `maze_rank`  TINYINT     NOT NULL DEFAULT (1),
    `tag_id`     MEDIUMINT   NOT NULL DEFAULT (1),
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
/* /prac usages,
   x practice slime ball usages,
   x last time played,
   x time played,
   x date started,
   x jumps,
   x map,
   x position
*/
CREATE TABLE IF NOT EXISTS `lc_play_through`
(
    `id`                 INT AUTO_INCREMENT NOT NULL,
    `map_id`             MEDIUMINT          NOT NULL,
    `user_id`            VARCHAR(36)        NOT NULL,
    `completion`         BOOL   DEFAULT (FALSE),
    `completion_date`    DATE   DEFAULT NULL,
    `prac_usage`         INT    DEFAULT 0,
    `join_date`          DATE   DEFAULT (CURRENT_DATE),
    `time_played_normal` BIGINT DEFAULT 0,
    `time_played_prac`   BIGINT DEFAULT 0,
    `current_jumps`      INT    DEFAULT 0,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `lc_players` (`user_id`),
    FOREIGN KEY (`map_id`) REFERENCES `lc_maps` (`id`)
);

CREATE TABLE IF NOT EXISTS `lc_saves`
(
    `id`              INT AUTO_INCREMENT NOT NULL,
    `play_through_id` INT                NOT NULL,
    `save_name`       VARCHAR(64) DEFAULT ('Save-State'),
    `block_type_name` VARCHAR(64) DEFAULT ('STONE'),
    `quit_date`       DATE               NOT NULL,
    `location_id`     MEDIUMINT          NOT NULL,
    `loaded`          BOOLEAN     DEFAULT FALSE,
    `imported`        BOOLEAN     DEFAULT FALSE,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`location_id`) REFERENCES `lc_locations` (`id`),
    FOREIGN KEY (`play_through_id`) REFERENCES `lc_play_through` (`id`)
);

CREATE TABLE IF NOT EXISTS `lc_command_cooldowns`
(
    `command`  VARCHAR(100) NOT NULL,
    `message`  VARCHAR(100) NOT NULL,
    `cooldown` INT          NOT NULL,
    PRIMARY KEY (`command`)
);

CREATE TABLE IF NOT EXISTS `lc_practice`
(
    `user_id`     VARCHAR(36) NOT NULL,
    `location_id` MEDIUMINT   NOT NULL,
    PRIMARY KEY (`user_id`),
    FOREIGN KEY (`location_id`) REFERENCES `lc_locations` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `lc_players` (`user_id`) ON DELETE CASCADE
);

-- working example for longer cooldown commands
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("gg", 45000);
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("gl", 45000);
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("rip", 45000);
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("ham", 45000);
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("bacon", 45000);
-- INSERT IGNORE INTO `lc_command_cooldowns` (command, cooldown) VALUES ("eggs", 45000);
