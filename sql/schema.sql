drop table if exists user_role;
drop table if exists app_role;
drop table if exists spfiles;
drop table if exists soulpatches;
drop table if exists app_user;
drop table if exists persistent_logins;
drop table if exists soulpatch_ratings;

drop sequence if exists hibernate_sequence;


create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to postgres;

create table app_user
(
    id                 bigint       not null
        constraint app_user_pkey
            primary key,
    user_name          varchar(255) not null unique,
    email              varchar(255) not null unique,
    encrypted_password varchar(255) not null,
    status             varchar(255) not null
);

alter table app_user
    owner to postgres;

create table app_role
(
    id        bigint       not null
        constraint app_role_pkey primary key,
    role_name varchar(255) not null unique
);

alter table app_role
    owner to postgres;

create table user_role
(
    user_id bigint not null
        constraint user_role_user_constraint references app_user,
    role_id bigint not null
        constraint user_role_role_constraint references app_role,
    constraint user_role_pkey primary key (user_id, role_id)
);

alter table user_role
    owner to postgres;

create table persistent_logins
(
    username  varchar(255) not null,
    series    varchar(255) not null,
    token     varchar(255) not null,
    last_used timestamp    not null,
    primary key (series)
);

alter table persistent_logins
    owner to postgres;

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

create table soulpatch_ratings
(
    id           bigint    not null
        constraint soulpatch_ratings_pkey
            primary key,
    soulpatch_id bigint    not null
        constraint ratings_soulpatch_constraint
            references soulpatches,
    app_user_id  bigint    not null
        constraint ratings_app_user_constraint
            references app_user,
    stars        int       not null,
    created_at   timestamp not null,
    updated_at   timestamp not null,
    unique (soulpatch_id, app_user_id)
);

alter table soulpatch_ratings
    owner to postgres;
