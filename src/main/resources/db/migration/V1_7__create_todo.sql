create table todos
(
    id          bigserial   not null
        constraint todo_pk
            primary key,
    user_id     bigint      not null,
    description text        not null,
    create_date timestamp   not null,
    priority    varchar(10) not null,
    item_order  varchar(10) not null,
    done        boolean     not null
);
create
    index todos_user_id_uindex
    on todos (user_id);