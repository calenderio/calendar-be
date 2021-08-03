create table users
(
    id         bigserial             not null
        constraint users_pk
            primary key,
    name       varchar(255)          not null,
    email      varchar(255)          not null,
    licence_id bigint,
    company_id bigint,
    is_company boolean default false not null,
    password   varchar(512)          not null
);

alter table users owner to fmuser;

create
index users_company_id_index
	on users using ??? (company_id);

create
unique index users_email_uindex
	on users using ??? (email);

create
unique index users_licence_id_uindex
	on users using ??? (licence_id);

