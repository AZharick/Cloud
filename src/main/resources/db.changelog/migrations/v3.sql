CREATE TABLE users (
    id SERIAL PRIMARY KEY NOT NULL,
    password VARCHAR,
    username VARCHAR
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