drop table if exists users cascade;
drop table if exists files cascade;
drop table if exists authorities cascade;
drop table if exists user_authority cascade;

CREATE TABLE users (
    id SERIAL PRIMARY KEY NOT NULL,
    password VARCHAR,
    username VARCHAR,
    token VARCHAR
);

CREATE TABLE files (
    name VARCHAR(20) PRIMARY KEY,
    user_id INT REFERENCES users (id)
);

CREATE TABLE authorities (
    id SERIAL PRIMARY KEY NOT NULL,
    authority VARCHAR
);

CREATE TABLE user_authority (
    user_id INT,
    authority_id INT
);

insert into authorities values (1, 'full');
insert into users (id, password, username) values (1, 'asd', 'asd');
insert into user_authority values (1, 1);