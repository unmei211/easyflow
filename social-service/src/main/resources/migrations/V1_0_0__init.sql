CREATE TABLE users(
    id varchar primary key,
    login varchar unique not null,
    email varchar unique not null,
    password varchar not null,
    enabled boolean not null
);

CREATE TABLE friend(
    user_id varchar references users(id) not null,
    friend_id varchar references users(id) not null,
    unique (user_id, friend_id)
);

CREATE TABLE role(
    id varchar primary key,
    role varchar unique
);

INSERT INTO role VALUES ('9411ad92-a563-4859-83c9-50e159727085', 'USER');
INSERT INTO role VALUES ('accfb47d-6078-4346-abe2-623fa7ab8305', 'ADMIN');

CREATE TABLE user_role(
    user_id varchar references users(id),
    role_id varchar references role(id),
    unique (user_id, role_id)
);