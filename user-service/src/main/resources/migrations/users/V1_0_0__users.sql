CREATE TABLE users(
    id varchar primary key,
    login varchar unique not null,
    email varchar unique not null
);