create table invitations
(
    id            bigserial    not null
        constraint invitations_pk
            primary key,
    event_id      bigint       not null,
    user_email    varchar(255) not null,
    name          varchar(255) not null,
    user_id       bigint       not null,
    scheduled     bool         not null default false,
    invitation_id varchar(50),
    title         varchar(255),
    cc_list       text[],
    bcc_list      text[]

);

create unique index invitations_invitation_id_uindex
    on invitations (invitation_id);

create index invitations_user_id_index
    on invitations (user_id);

create index invitations_user_email_index
    on invitations (user_email);

create index invitations_event_id_index
    on invitations (event_id);

CREATE UNIQUE INDEX invitations_user_email_condition_index
    ON invitations (user_email, event_id) WHERE (scheduled is false);

