-- auto-generated definition
create table payment
(
    id           bigint not null
        constraint payments_pk
            primary key,
    account_id   bigint not null
        constraint payments_accounts_account_id_fk
            references account,
    sum_payment  money  not null,
    operation_id bigint not null
        constraint payments_operations_operation_id_fk
            references operation
);

comment on table payment is 'Операции со счетом/кошельком';

comment on column payment.account_id is 'Счет по которому проводится проводка';

comment on column payment.sum_payment is 'Сумма операции ("+" для пополнения, "-" для списания)';

comment on column payment.operation_id is 'Ссылка на базовую операцию';

alter table payment
    owner to postgres;

create unique index payments_id_uindex
    on payment (id);

