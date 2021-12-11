create table role
(
    role_id bigint not null constraint role_pkey primary key,
    name    varchar(255)
);

create table status
(
    status_id bigint not null constraint status_pkey primary key,
    name      varchar(255)
);

create table authentication_info
(
    user_id          bigint       not null constraint authentication_info_pkey primary key,
    email            varchar(255) not null,
    is_active        boolean      not null,
    password_hash    varchar(255) not null,
    refresh_token    varchar(4096),
    status_status_id bigint references status
);

create table authentication_info_roles
(
    authentication_info_user_id bigint not null references authentication_info,
    roles_role_id               bigint not null references role
);

create table authorization_info
(
    user_id      bigint not null constraint authorization_info_pkey primary key,
    access_token varchar(4096)
);
