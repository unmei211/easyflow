CREATE TABLE refresh_tokens(
    id varchar primary key,
    user_id varchar unique references users(id) on delete cascade,
    refresh_token varchar unique
);