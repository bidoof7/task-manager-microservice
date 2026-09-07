CREATE TABLE outbox_events
(
    id             BIGINT IDENTITY(1,1) PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id   VARCHAR(100) NOT NULL,
    event_type     VARCHAR(100) NOT NULL,
    payload        NVARCHAR(MAX) NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    created_at     DATETIME2    NOT NULL,
    published_at   DATETIME2 NULL
);