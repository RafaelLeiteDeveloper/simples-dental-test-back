CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

INSERT INTO users (name, email, password, role)
VALUES (
    'Admin',
    'contato@simplesdental.com',
    '$2a$10$c8huUUDim/dO9J2N1TG5aui5kLq37qe7VQNbOfb3VBoV9sPU7CkyK',
    'ADMIN'
);
