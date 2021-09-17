create table caches
(
    name        varchar(25)             not null
        constraint caches_pk
            primary key,
    value       varchar(50)             not null,
    update_date timestamp default now() not null
);

INSERT INTO caches
values ('QUESTION_LIMIT_IND', '5', now()),
       ('QUESTION_LIMIT_FREE', '1', now()),
       ('QUESTION_LIMIT_COMMERCIAL', '20', now());
