CREATE TABLE task_group
(
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NULL,
    created_by  NVARCHAR(255) NOT NULL,
    created_at DATETIME2 NULL,
    updated_by NVARCHAR(255) NULL,
    updated_at DATETIME2 NULL
);
