-- liquibase formatted sql

-- changelog amritkthapa:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM app_user WHERE username='amritkthapa'
INSERT INTO app_user (version, name, username, password)
VALUES (0, 'Amrit Thapa', 'amritkthapa', '$2a$10$8dS890IFv8qkWUAWXvjgruLVHTTng0f7CcWaRy6S3iFPkW/JzdvqK');
