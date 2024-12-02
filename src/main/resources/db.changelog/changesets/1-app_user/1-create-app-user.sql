-- liquibase formatted sql

-- changeset amritkthapa:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = (SELECT DATABASE()) AND table_name = 'app_user'
CREATE TABLE IF NOT EXISTS app_user
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    version     BIGINT                NOT NULL,
    name        VARCHAR(255)          NOT NULL,
    username    VARCHAR(255)          NOT NULL,
    password    VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);