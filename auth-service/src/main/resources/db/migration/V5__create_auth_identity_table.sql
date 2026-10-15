CREATE TABLE account_identities
(
    id               BIGINT IDENTITY(1,1) PRIMARY KEY,
    account_id       BIGINT NOT NULL,
    provider         VARCHAR(50) NOT NULL,
    provider_subject VARCHAR(255) NOT NULL,

    CONSTRAINT fk_account_identity_account
        FOREIGN KEY (account_id)
            REFERENCES accounts(id),

    CONSTRAINT uk_account_identity_provider_subject
        UNIQUE (provider, provider_subject)
);