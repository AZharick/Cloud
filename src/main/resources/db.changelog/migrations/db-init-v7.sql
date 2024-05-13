CREATE TABLE users (
    id SERIAL PRIMARY KEY NOT NULL ,
    login VARCHAR,
    password VARCHAR,
    enabled BOOLEAN,
    username VARCHAR
);

CREATE TABLE files (
    name VARCHAR(20) PRIMARY KEY,
    user_id INT REFERENCES users (id)
);

CREATE TABLE authorities (
    username VARCHAR,
    authority VARCHAR
);