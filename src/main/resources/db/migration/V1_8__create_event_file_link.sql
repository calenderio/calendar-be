create table event_file_links
(
    event_id bigint,
    file_id  bigint,
    user_id  bigint,
    constraint event_file_links_pk
        primary key (event_id, file_id, user_id)
);

