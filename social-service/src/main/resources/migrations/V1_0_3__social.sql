CREATE TABLE friend_request
(
    user_id   varchar references users (id) ON DELETE CASCADE not null,
    sender_id varchar references users (id) ON DELETE CASCADE not null,
    unique (user_id, sender_id)
);

CREATE TABLE user_policy
(
    id                        varchar primary key,
    user_id                   varchar unique references users (id) ON DELETE CASCADE not null,
    allowed_to_friend_request boolean                                                not null,
    allowed_send_task         boolean                                                not null
);

CREATE TABLE contract
(
    id             varchar primary key,
    assigmenter_id varchar references users (id) ON DELETE CASCADE not null,
    holder_id      varchar references users (id) ON DELETE CASCADE not null,
    unique (assigmenter_id, holder_id)
);

CREATE TABLE assigment(
    id varchar primary key,
    contract_id varchar references contract(id) unique not null,
    task_id varchar references task(id) unique not null
);