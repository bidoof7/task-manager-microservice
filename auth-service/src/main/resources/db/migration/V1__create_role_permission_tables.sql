CREATE TABLE roles
(
    id   BIGINT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE permissions
(
    id   BIGINT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE role_permissions
(
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    PRIMARY KEY (role_id, permission_id),

    CONSTRAINT fk_role_permissions_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id),

    CONSTRAINT fk_role_permissions_permission
        FOREIGN KEY (permission_id)
            REFERENCES permissions (id)
);
