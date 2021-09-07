create table licences
(
    id              bigserial   not null
        constraint licences_pk
            primary key,
    licence_key     text        not null,
    activation_date timestamp   not null,
    end_date timestamp   not null,
    type            varchar(10) not null,
    company_id      bigint,
    user_id         bigint      not null

);

create
index licences_company_id_index
	on licences (company_id);

create
unique index licences_licence_key_uindex
	on licences (licence_key);

create
    unique index licences_user_id_uindex
    on licences (user_id);