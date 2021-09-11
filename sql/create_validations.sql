create table validations
(
    id      bigserial    not null
        constraint validations_pk
            primary key,
    user_id bigint       not null,
    mail    varchar(255) not null,
    code    varchar(50)  not null,
    type    varchar(10)  not null,
    date    timestamp    not null
);

create
    index validations_date_index
    on validations (date);

create
    index validations_user_id_index
    on validations (user_id);

create
    unique index validations_user_id_type_index
    on validations (user_id, type);
