drop table if exists users cascade;
drop table if exists files cascade;
drop table if exists users_user_files cascade;

CREATE TABLE users (
    username varchar(255) primary key not null,
    password varchar(255) not null
);

CREATE TABLE files (
    filename      varchar(255) primary key not null,
    date          timestamp not null,
    file_content  bytea not null,
    size          bigint not null,
    user_username varchar(255),
    FOREIGN KEY (user_username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE users_user_files (
    user_username VARCHAR(255) NOT NULL,
    user_files_filename VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_username, user_files_filename),
    FOREIGN KEY (user_username) REFERENCES users(username) ON DELETE CASCADE,
    FOREIGN KEY (user_files_filename) REFERENCES files(filename) ON DELETE CASCADE
);

insert into users (username, password) values ('asd', 'asd');