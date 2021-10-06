create table file_links
(
    id        bigserial    not null
        constraint file_links_pk
            primary key,
    user_id   bigint       not null,
    name      varchar(255) not null,
    file_link text         not null,
    file_type varchar(100)

);

create
    index file_links_user_id_index
    on file_links (user_id);