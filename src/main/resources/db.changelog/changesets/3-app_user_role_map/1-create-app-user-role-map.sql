-- liquibase formatted sql

-- changeset amritkthapa:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = (SELECT DATABASE()) AND table_name = 'app_user_role_map'
CREATE TABLE IF NOT EXISTS app_user_role_map
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    version     BIGINT                NULL,
    app_user_id BIGINT                NOT NULL,
    role_id     BIGINT                NOT NULL,
    CONSTRAINT pk_app_user_role_map PRIMARY KEY (id),
    CONSTRAINT fk_app_user_role_map_app_user FOREIGN KEY (app_user_id) REFERENCES app_user (id),
    CONSTRAINT fk_app_user_role_map_role FOREIGN KEY (role_id) REFERENCES roles (id)
);