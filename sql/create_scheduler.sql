create table schedulers
(
    id              bigserial   not null
        constraint schedulers_pk
            primary key,
    user_id         bigint      not null,
    time_zone       varchar(35) not null,
    name            varchar(50) not null,
    unavailable     text[],
    mon             jsonb,
    tue             jsonb,
    wed             jsonb,
    thu             jsonb,
    fri             jsonb,
    sat             jsonb,
    sun             jsonb,
    additional_time jsonb,
    for_calendar    bool        not null default false
);

create
    index schedulers_user_id_uindex
    on schedulers (user_id);
