-- auto-generated definition
create table operation_type
(
    id             serial
        constraint operation_type_pk
            primary key,
    operation_name varchar(25) not null
);

comment on table operation_type is 'Типы операций';

comment on column operation_type.operation_name is 'Наименование операции';

alter table operation_type
    owner to postgres;

create unique index operation_type_id_uindex
    on operation_type (id);

create unique index operation_type_name_uindex
    on operation_type (operation_name);

