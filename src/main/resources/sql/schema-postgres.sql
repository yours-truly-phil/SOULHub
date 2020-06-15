DROP TABLE IF EXISTS soulpatches;

CREATE TABLE soulpatches(
    id serial PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    author VARCHAR(255),
    description VARCHAR(255),
    name VARCHAR(255),
    no_servings BIGINT,
    offset_date_time TIMESTAMP,
    soul_file_content TEXT,
    soul_file_name VARCHAR(255),
    soulpatch_file_content TEXT,
    soulpatch_file_name VARCHAR(255));

