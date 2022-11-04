CREATE TABLE IF NOT EXISTS `{p}players` (
  `uuid` VARCHAR(36) PRIMARY KEY
);

-- Name and description lengths are arbitrary, change if deemed necessary
-- TODO: Rarities (could just be CHAR(1)?)
CREATE TABLE IF NOT EXISTS `{p}tags` (
  `id`          MEDIUMINT AUTO_INCREMENT NOT NULL,
  `name`        VARCHAR(100)             NOT NULL,
  `description` TEXT,
  PRIMARY KEY (`id`)
);

-- Chose MEDIUMINT because I doubt we will ever go over 8 million signs lol (feel free to change if you would like)
-- This table will store signs/heads with right click events (rankup signs/teleport heads/segmented cps)
-- Will likely have a foreign key to an effects table
CREATE TABLE IF NOT EXISTS `{p}effect_blocks` (
  `id`    MEDIUMINT AUTO_INCREMENT NOT NULL,
  `world` VARCHAR(36)              NOT NULL,
  `x`     INT                      NOT NULL,
  `y`     INT                      NOT NULL,
  `z`     INT                      NOT NULL,
  PRIMARY KEY (`id`)
);