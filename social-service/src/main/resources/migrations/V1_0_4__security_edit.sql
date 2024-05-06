ALTER TABLE users
    DROP COLUMN password;

CREATE TABLE user_password
(
    id       varchar primary key,
    user_id  varchar references users (id) not null unique,
    password varchar                       not null
);

ALTER TABLE users
    DROP COLUMN enabled;

CREATE TABLE user_enabled
(
    id      varchar primary key,
    enabled boolean                              not null,
    user_id varchar references users (id) unique not null
);