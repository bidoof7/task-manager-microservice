ALTER TABLE tasks
    ADD
        created_at DATETIME2 NULL,
        updated_by VARCHAR(255) NULL,
        updated_at DATETIME2 NULL;