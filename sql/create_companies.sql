create table companies
(
    id           bigserial not null
        constraint companies_pk
            primary key,
    max_users    int       not null,
    active_users int       not null
);

