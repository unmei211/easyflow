CREATE TABLE users_auth
(
    id      varchar primary key,
    enabled boolean not null default true
);

CREATE TABLE user_password
(
    id       varchar primary key,
    user_id  varchar references users_auth (id) on delete cascade unique not null,
    password varchar                                                     not null
);

CREATE TABLE role
(
    id   varchar primary key,
    role varchar unique not null
);

INSERT INTO role
VALUES ('9411ad92-a563-4859-83c9-50e159727085', 'USER');
INSERT INTO role
VALUES ('accfb47d-6078-4346-abe2-623fa7ab8305', 'ADMIN');

CREATE TABLE user_role
(
    user_id varchar references users_auth (id) not null,
    role_id varchar references role (id)       not null,
    unique (user_id, role_id)
);

CREATE TABLE refresh_tokens
(
    id            varchar primary key,
    user_id       varchar unique references users_auth (id) on delete cascade not null,
    refresh_token varchar unique                                              not null
);