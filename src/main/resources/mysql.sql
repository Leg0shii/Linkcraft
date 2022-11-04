CREATE TABLE IF NOT EXISTS `{p}players` (
  `uuid`      VARCHAR(36) PRIMARY KEY
);

-- Chose MEDIUMINT because I doubt we will ever go over 8 million signs lol (feel free to change if you would like)
-- This table will store signs/heads with right click events (rankup signs/teleport heads/segmented cps)
-- Will likely have a foreign key to an effects table
CREATE TABLE IF NOT EXISTS `{p}effect_blocks` (
  `id`    MEDIUMINT AUTO_INCREMENT NOT NULL,
  `world` CHAR(36)                 NOT NULL,
  `x`     INT                      NOT NULL,
  `y`     INT                      NOT NULL,
  `z`     INT                      NOT NULL
);