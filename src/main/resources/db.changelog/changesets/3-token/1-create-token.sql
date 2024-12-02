-- liquibase formatted sql

-- changeset amritkthapa:1
-- preconditions onFail:CONTINUE orError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'token'
CREATE TABLE IF NOT EXISTS token (
    id              BIGINT AUTO_INCREMENT   PRIMARY KEY NOT NULL,
    version         BIGINT                  NOT NULL,
    token           VARCHAR(255)            NOT NULL,
    is_logged_out   BOOLEAN                 NOT NULL,
    app_user_id     BIGINT                  NOT NULL,
    FOREIGN KEY (app_user_id) REFERENCES app_user(id)
);