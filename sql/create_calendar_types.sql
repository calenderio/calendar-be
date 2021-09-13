create table calendar_types
(
    id            bigserial   not null
        constraint calendar_types_pk
            primary key,
    user_id       bigint      not null,
    name          varchar(50) not null,
    description   text        not null,
    time_zone     varchar(35) not null,
    file_required bool        not null default false,
    start_date    date        not null,
    end_date      date        not null,
    duration      int         not null,
    duration_type varchar(4)  not null,
    scheduler_id  bigint      not null

);

create
    index calendar_types_user_id_index
    on calendar_types (user_id);

create
    index calendar_types_scheduler_id_index
    on calendar_types (scheduler_id);
