create table users
(
    id serial
        constraint users_pk
            primary key,
    username varchar(50),
    password varchar(50) not null
);

insert into users (username, password) values ('pedro', 'caires'), ('daniel', 'caires');

create unique index users_username_uindex
    on users (username);

-- drop table users

create table messages
(
    id serial
        constraint messages_pk
            primary key,
    to_user_id int not null,
    from_user_id int not null,
    message varchar(250) not null,
    delivered bool not null default false,
    read bool not null default false,
    constraint fk_to foreign key(to_user_id) references users(id),
    constraint fk_from foreign key(from_user_id) references users(id)
);
-- drop table messages

create index to_from_message
    on messages (to_user_id, from_user_id);

create index read_to_message
    on messages (to_user_id, read);