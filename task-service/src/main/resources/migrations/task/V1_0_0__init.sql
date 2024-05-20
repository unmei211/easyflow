CREATE TABLE workspace
(
    id       varchar primary key,
    name     varchar not null,
    owner_id varchar not null,
    unique (name, owner_id)
);

CREATE TABLE task
(
    id           varchar primary key,
    name         varchar                                             not null,
    description  varchar                                             not null,
    workspace_id varchar references workspace (id) on delete cascade not null,
    owner_id     varchar                                             not null
);

CREATE TABLE task_history
(
    id            bigint primary key,
    task_id       varchar not null unique,
    when_resolved bigint  not null,
    complete      boolean not null
);

CREATE TABLE task_assigments
(
    id          varchar primary key,
    task_id     varchar unique references task (id) on delete CASCADE,
    contract_id varchar not null,
    unique (task_id, contract_id)
);

CREATE TABLE eisenhower_matrix
(
    id           varchar primary key,
    task_id      varchar references task (id) on delete CASCADE unique,
    block_number int not null CHECK ( block_number <= 4 AND block_number > 0 ),
    order_number int not null
);