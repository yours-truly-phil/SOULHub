drop table if exists spfiles;
drop table if exists soulpatches;

create table soulpatches
(
	id bigint not null
		constraint soulpatches_pkey
			primary key,
	created_at timestamp not null,
	updated_at timestamp not null,
	author varchar(255),
	description text,
	name varchar(255),
	no_servings bigint
);

alter table soulpatches owner to postgres;

create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to postgres;

create table spfiles
(
	id bigint not null
		constraint spfiles_pkey
			primary key,
	created_at timestamp not null,
	updated_at timestamp not null,
	fileType VARCHAR(255),
	content text,
	name varchar(255),
	soulpatch_id bigint not null
		constraint spfiles_soulpatch_constraint
			references soulpatches
);

alter table spfiles owner to postgres;
