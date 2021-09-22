create table meetings
(
    id            bigserial    not null
        constraint meetings_pk
            primary key,
    uuid          uuid         not null,
    start_date    timestamp    not null,
    end_date      timestamp    not null,
    duration      int          not null,
    duration_type varchar(4)   not null,
    description   text         not null,
    time_zone     text         not null,
    organizer     varchar(255) not null,
    location      text         not null,
    meetingTitle  varchar(255) not null,
    invitation_id varchar(50)  not null,
    participants  text[],
    cc            text[],
    bcc           text[]

);

create index meetings_organizer_index
    on meetings (organizer);

create unique index meetings_invitation_id_uindex
    on meetings (invitation_id);

create unique index meetings_uuid_uindex
    on meetings (uuid);

