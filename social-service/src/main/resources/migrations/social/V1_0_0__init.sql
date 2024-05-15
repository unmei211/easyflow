CREATE TABLE social_users
(
    id varchar primary key
);

CREATE TABLE friend
(
    user_id   varchar references social_users (id) not null,
    friend_id varchar references social_users (id) not null,
    unique (user_id, friend_id)
);

CREATE TABLE friend_request
(
    user_id   varchar references social_users (id) ON DELETE CASCADE not null,
    sender_id varchar references social_users (id) ON DELETE CASCADE not null,
    unique (user_id, sender_id)
);

CREATE TABLE user_policies
(
    id                        varchar primary key,
    user_id                   varchar unique references social_users (id) ON DELETE CASCADE not null,
    allowed_to_friend_request boolean                                                       not null,
    allowed_send_task         boolean                                                       not null
);

CREATE TABLE contract
(
    id             varchar primary key,
    assigmenter_id varchar references social_users (id) ON DELETE CASCADE not null,
    holder_id      varchar references social_users (id) ON DELETE CASCADE not null,
    unique (assigmenter_id, holder_id)
);

CREATE TABLE social_assigment
(
    id          varchar primary key,
    contract_id varchar references contract (id) unique not null
);