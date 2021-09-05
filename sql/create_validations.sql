create table validations
(
    id      bigserial    not null
        constraint validations_pk
            primary key,
    user_id bigint       not null,
    mail    varchar(255) not null,
    code    varchar(6)   not null,
    type    varchar(10)  not null,
    date    timestamp    not null
);

create
index validations_date_index
	on validations (date);
