create table questions
(
    id         bigserial   not null
        constraint questions_pk
            primary key,
    text       text        not null,
    type       varchar(50) not null,
    values     text,
    length_min int,
    length_max int,
    event_id   bigint      not null
);

create index questions_event_id_index
    on questions (event_id);

