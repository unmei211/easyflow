CREATE TABLE workspace
(
    id       varchar primary key,
    name     varchar                                         not null,
    owner_id varchar references users (id) ON DELETE CASCADE not null,
    unique (name, owner_id)
);

CREATE TABLE task
(
    id           varchar primary key,
    name         varchar not null,
    description  varchar not null,
    workspace_id varchar references workspace (id)
);

CREATE TABLE task_history
(
    id            varchar primary key,
    task_id       varchar not null unique,
    when_resolved bigint  not null,
    complete      boolean not null
);

CREATE TABLE EisenhowerMatrix
(
    id           varchar primary key,
    task_id      varchar references task (id) on delete CASCADE unique,
    block_number int not null CHECK ( block_number <= 4 AND block_number > 0 ),
    order_number int not null
);