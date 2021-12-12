-- auto-generated definition
create table if not exists account
(
    id            bigint primary key,
    date_created  timestamp not null,
    is_active     boolean   not null,
    date_modified timestamp not null
);

-- auto-generated definition
create table if not exists operation_type
(
    id          bigint primary key,
    description varchar(4096),
    type        varchar(255) not null
);


create table if not exists operation
(
    id                bigint primary key,
    date_time         timestamp      not null,
    initiator         bigint         not null,
    sum_payment       numeric(19, 2) not null,
    credit_account_id bigint         not null references account,
    debit_account_id  bigint         not null references account,
    operation_type    bigint         not null references operation_type
);


-- auto-generated definition
create table if not exists payment
(
    id           bigint primary key,
    date_time    timestamp      not null,
    sum_payment  numeric(19, 2) not null,
    account_id   bigint         not null references account,
    operation_id bigint         not null references operation
);

