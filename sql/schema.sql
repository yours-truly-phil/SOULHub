drop table if exists user_role;
drop table if exists app_role;
drop table if exists spfiles;
drop table if exists soulpatches;
drop table if exists app_user;
drop table if exists persistent_logins;

drop sequence if exists hibernate_sequence;


create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to postgres;

create table app_user
(
    id                 bigint       not null
        constraint app_user_pkey
            primary key,
    user_name          varchar(255) not null unique,
    encrypted_password varchar(255) not null,
    status             varchar(255) not null
);

create table app_role
(
    id        bigint       not null
        constraint app_role_pkey primary key,
    role_name varchar(255) not null unique
);

create table user_role
(
    id      bigint not null
        constraint user_role_pkey primary key,
    user_id bigint not null
        constraint user_role_user_constraint references app_user,
    role_id bigint not null
        constraint user_role_role_constraint references app_role,
    unique (user_id, role_id)
);

create table persistent_logins
(
    username  varchar(255) not null,
    series    varchar(255) not null,
    token     varchar(255) not null,
    last_used timestamp    not null,
    primary key (series)
);

create table soulpatches
(
    id          bigint    not null
        constraint soulpatches_pkey
            primary key,
    created_at  timestamp not null,
    updated_at  timestamp not null,
    author      bigint    not null
        constraint soulpatches_author_user_constraint references app_user,
    description text,
    name        varchar(255),
    no_views    bigint default 0
);

alter table soulpatches
    owner to postgres;

create table spfiles
(
    id           bigint    not null
        constraint spfiles_pkey
            primary key,
    created_at   timestamp not null,
    updated_at   timestamp not null,
    fileType     VARCHAR(255),
    content      text,
    name         varchar(255),
    soulpatch_id bigint    not null
        constraint spfiles_soulpatch_constraint
            references soulpatches
);

alter table spfiles
    owner to postgres;
