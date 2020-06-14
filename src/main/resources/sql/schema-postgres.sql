DROP TABLE IF EXISTS soulpatches;
CREATE TABLE soulpatches(
    id serial PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    soulFileName VARCHAR(255),
    soulFileContent TEXT,
    soulpatchFileName VARCHAR(255),
    soulpatchFileContent TEXT,
    offsetDateTime TIMESTAMP,
    author VARCHAR(255),
    noServings INT);
