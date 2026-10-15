CREATE TABLE user_info
(
    id         BIGINT IDENTITY(1,1) PRIMARY KEY,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    dob        DATE,
    bio        VARCHAR(1000),
    account_id BIGINT NOT NULL,

    CONSTRAINT fk_user_info_account
        FOREIGN KEY (account_id)
            REFERENCES accounts (id),

    CONSTRAINT uq_user_info_account
        UNIQUE (account_id)
);