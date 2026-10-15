CREATE TABLE accounts
(
    id             BIGINT IDENTITY(1,1) PRIMARY KEY,
    email          VARCHAR(100) NOT NULL UNIQUE,
    email_verified BIT          NOT NULL DEFAULT 0,
    password_hash  VARCHAR(255) NULL,
    status         VARCHAR(100) NOT NULL,
    role_id        BIGINT       NOT NULL,
    created_at     DATETIME2    NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT uk_accounts_email UNIQUE (email),

    CONSTRAINT fk_accounts_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
);