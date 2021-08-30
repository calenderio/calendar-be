create table user_calendars
(
    user_id bigint,
    calendar_id bigint,
    constraint user_calendars_pk
        primary key (user_id, calendar_id)
);

