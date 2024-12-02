-- liquibase formatted sql

-- changeset amritkthapa:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM token
INSERT INTO token (version, token, is_logged_out, app_user_id)
VALUES
    (0, 'token1', false, 1);