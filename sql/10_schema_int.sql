drop table if exists verification_tokens;
drop table if exists user_role;
drop table if exists app_role;
drop table if exists soulpatch_ratings;
drop table if exists spfiles;
drop table if exists soulpatches;
drop table if exists app_user;
drop table if exists persistent_logins;

drop sequence if exists hibernate_sequence;


create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to soulhub;

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
    owner to soulhub;

create table app_role
(
    id        bigint       not null
        constraint app_role_pkey primary key,
    role_name varchar(255) not null unique
);

alter table app_role
    owner to soulhub;

create table user_role
(
    user_id bigint not null
        constraint user_role_user_constraint references app_user,
    role_id bigint not null
        constraint user_role_role_constraint references app_role,
    constraint user_role_pkey primary key (user_id, role_id)
);

alter table user_role
    owner to soulhub;

create table verification_tokens
(
    id          bigint       not null
        constraint verification_tokens_pkey primary key,
    token       varchar(255) not null,
    user_id     bigint       not null
        constraint verification_tokens_user_constraint references app_user,
    expiry_date timestamp    not null
);

alter table verification_tokens
    owner to soulhub;

create table persistent_logins
(
    username  varchar(255) not null,
    series    varchar(255) not null,
    token     varchar(255) not null,
    last_used timestamp    not null,
    primary key (series)
);

alter table persistent_logins
    owner to soulhub;

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

create index soulpatches_author_index
    on soulpatches (author);

create index soulpatches_no_views_index
    on soulpatches (no_views);

create index soulpatches_name_index
    on soulpatches (name);

alter table soulpatches
    owner to soulhub;

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

create index spfiles_soulpatch_id_index
    on spfiles (soulpatch_id);

alter table spfiles
    owner to soulhub;

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

create index soulpatch_ratings_soulpatch_index
    on soulpatch_ratings (soulpatch_id);

alter table soulpatch_ratings
    owner to soulhub;

insert into app_role (id, role_name)
values (nextval('hibernate_sequence'), 'ADMIN');

insert into app_role (id, role_name)
values (nextval('hibernate_sequence'), 'USER');