CREATE TABLE tasks
(
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    title       NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    created_by  NVARCHAR(255)
);