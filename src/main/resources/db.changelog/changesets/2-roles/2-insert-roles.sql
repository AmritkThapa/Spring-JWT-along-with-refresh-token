-- liquibase formatted sql

-- changeset amritkthapa:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM roles WHERE name='ADMIN' OR name='USER'
INSERT INTO roles (version, name)
VALUES
    (1, 'ADMIN'),
    (1, 'USER');