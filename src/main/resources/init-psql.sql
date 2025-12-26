CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TRIGGER IF EXISTS update_role_on_verified ON users;
DROP TRIGGER IF EXISTS add_role_user_authority ON users;

DROP FUNCTION IF EXISTS update_role_on_verified_fn();
DROP FUNCTION IF EXISTS add_role_user_authority_fn();

DROP TABLE IF EXISTS hits;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                       username varchar(50) UNIQUE NOT NULL,
                       email varchar(50) UNIQUE NOT NULL,
                       password varchar(256) NOT NULL,
                       enabled boolean NOT NULL,
                       verified boolean NOT NULL
);

CREATE TABLE authorities (
                             id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                             username varchar(50) REFERENCES users(username),
                             authority varchar(50)
);

CREATE TABLE hits (
                      id uuid PRIMARY KEY,
                      user_id uuid NOT NULL REFERENCES users(id),
                      x numeric(38, 2) NOT NULL,
                      y numeric(38, 2) NOT NULL,
                      r numeric(38, 2) NOT NULL,
                      status boolean NOT NULL,
                      date_of_creation timestamp NOT NULL
);

INSERT INTO users (id, username, email, password, enabled, verified)
VALUES (
           '000aed89-3083-4b86-a3cb-6e0a11dd4348'::uuid,
           'dobryak',
           'vovadobryshkin@gmail.com',
           '$2a$10$eJKuoB7EcTqBn827KJmRSuTx1Ap5lzEssRWECLEsoma5bU6k7qe/C',
           true,
           true
       );

INSERT INTO authorities (id, username, authority)
VALUES (
           'a33acb27-f5bf-407b-87f9-cbd450e903cb'::uuid,
           'dobryak',
           'ROLE_ADMIN'
       );

INSERT INTO authorities (id, username, authority)
VALUES (
           '000aed89-3083-4b86-a3cb-6e0a11dd4348'::uuid,
           'dobryak',
           'ROLE_USER'
       );

CREATE OR REPLACE FUNCTION add_role_user_authority_fn()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
INSERT INTO authorities (username, authority)
VALUES (NEW.username, 'ROLE_UNVERIFIED_USER');
RETURN NEW;
END;
$$;

CREATE TRIGGER add_role_user_authority
    AFTER INSERT ON users
    FOR EACH ROW
    EXECUTE FUNCTION add_role_user_authority_fn();

CREATE OR REPLACE FUNCTION update_role_on_verified_fn()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    IF (OLD.verified = false) AND (NEW.verified = true) THEN
UPDATE authorities
SET authority = 'ROLE_USER'
WHERE username = NEW.username
  AND authority = 'ROLE_UNVERIFIED_USER';
END IF;
RETURN NEW;
END;
$$;

CREATE TRIGGER update_role_on_verified
    AFTER UPDATE OF verified ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_role_on_verified_fn();
