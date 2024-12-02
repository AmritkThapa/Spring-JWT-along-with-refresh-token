-- liquibase formatted sql

-- changelog amritkthapa:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM app_user WHERE username='amritkthapa'
INSERT INTO app_user (version, name, username, password)
VALUES (0, 'Amrit Thapa', 'amritkthapa', '$2a$12$ohszArwmIDp3RjFuP7z6SO09QAYe3cARZyD6mmJ7XsP5NOmjr6p6i');
