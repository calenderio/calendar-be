create table linked_calendars
(
    id            bigserial    not null
        constraint linked_calendars_pk
            primary key,
    social_mail   varchar(255) not null,
    type          varchar(25)  not null,
    token         text         not null,
    refresh_token text,
    expire_date   timestamp    not null
);

create
    unique index linked_calendars_social_mail_uindex
    on linked_calendars (social_mail);


