-- liquibase formatted sql

-- changeset amritkthapa:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM app_user WHERE username = 'amritkthapa'
INSERT INTO app_user_role_map (app_user_id, role_id)
SELECT
    (SELECT id FROM app_user WHERE username = 'amritkthapa') AS app_user_id,
    r.id AS role_id
FROM roles r
WHERE r.id NOT IN
      (SELECT role_id FROM app_user_role_map WHERE app_user_id = (SELECT id FROM app_user WHERE username = 'amritkthapa'));
