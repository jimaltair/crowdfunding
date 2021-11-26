-- auto-generated definition
create table account
(
    id    bigint default nextval('account_account_id_seq'::regclass) not null
        constraint accounts_pk
            primary key,
    is_active     boolean                                                     not null,
    date_created  timestamp                                                   not null,
    date_modified timestamp                                                   not null
);

comment on table account is 'Счета/кошельки';

comment on column account.is_active is 'Признак рабочего состояния';

comment on column account.date_created is 'Дата создания';

comment on column account.date_modified is 'Дата последнего изменения';

alter table account
    owner to postgres;

create unique index accounts_account_id_uindex
    on account (id);

