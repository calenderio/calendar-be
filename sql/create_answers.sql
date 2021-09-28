create table answers
(
    id          bigserial not null
        constraint answers_pk primary key,
    text        text[]    not null,
    event_id    bigint    not null,
    question_id bigint    not null,
    invite_id   bigint    not null
);

create index answers_event_id_index
    on answers (event_id);

create index answers_question_id_index
    on answers (question_id);

create index answers_invite_id_index
    on answers (invite_id);