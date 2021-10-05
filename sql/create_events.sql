create table events
(
    id               bigserial   not null
        constraint events_pk
            primary key,
    user_id          bigint      not null,
    name             varchar(50) not null,
    description      text        not null,
    location         text        not null,
    time_zone        varchar(35) not null,
    file_required    bool        not null,
    file_description text,
    start_date       date        not null,
    end_date         date        not null,
    duration         int         not null,
    duration_type    varchar(4)  not null,
    alarms           jsonb       not null,
    scheduler_id     bigint      not null

);

create
    index events_user_id_index
    on events (user_id);

create
    index events_scheduler_id_index
    on events (scheduler_id);
