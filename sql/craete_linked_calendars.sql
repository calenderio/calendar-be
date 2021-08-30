create table linked_calendars
(
    id            bigserial    not null
        constraint linked_calendars_pk
            primary key,
    social_mail   varchar(255) not null,
    type          varchar(25)  not null,
    token         text         not null,
    refresh_token text,
    expire_date   timestamp    not null,
    user_id       bigint       not null
);

create
index linked_calendars_user_id_index
	on linked_calendars (user_id);


