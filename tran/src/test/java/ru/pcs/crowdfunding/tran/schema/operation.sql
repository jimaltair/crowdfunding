-- auto-generated definition
create table operation
(
    id                bigint    not null
        constraint operations_pk
            primary key,
    initiator         bigint    not null,
    date_time         timestamp not null,
    operation_type    integer
        constraint operation_operation_type_id_fk
            references operation_type,
    debit_account_id  bigint    not null
        constraint operation_account_account_id_fk
            references account,
    credit_account_id bigint    not null
        constraint operation_account_account_id_fk_2
            references account,
    sum_payment       money     not null
);

comment on column operation.initiator is 'Инициатор операции';

comment on column operation.date_time is 'Дата, время операции';

comment on column operation.operation_type is 'Тип операции';

comment on column operation.debit_account_id is 'Ссылка на счет, на котором остаток будет увеличиваться';

comment on column operation.credit_account_id is 'Ссылка на счет, на котором остаток будет уменьшаться';

comment on column operation.sum_payment is 'Сумма операции';

alter table operation
    owner to postgres;

create unique index operations_operation_id_uindex
    on operation (id);

