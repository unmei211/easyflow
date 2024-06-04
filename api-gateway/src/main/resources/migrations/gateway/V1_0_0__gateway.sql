CREATE TABLE users_auth
(
    id      varchar primary key,
    enabled boolean not null default true
);

CREATE TABLE user_password
(
    user_id  varchar primary key references users_auth (id) on delete cascade,
    password varchar not null
);

CREATE TABLE role
(
    role varchar primary key
);

INSERT INTO role
VALUES ('USER');
INSERT INTO role
VALUES ('ADMIN');

CREATE TABLE user_role
(
    user_id varchar references users_auth (id) not null,
    role_id varchar references role (role)  not null,
    unique (user_id, role_id)
);

CREATE TABLE refresh_tokens
(
    user_id       varchar unique references users_auth (id) on delete cascade not null,
    refresh_token varchar primary key
);