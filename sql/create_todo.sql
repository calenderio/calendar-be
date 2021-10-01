create table todos
(
    id           bigserial   not null
        constraint licences_pk
            primary key,
    user_id      bigint      not null,
    description  text        not null,
    created_date timestamp   not null,
    priority     varchar(10) not null,
    is_done boolean default false not null
);
create
    index todos_user_id_uindex
    on todos (user_id);