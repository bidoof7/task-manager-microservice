CREATE TABLE processed_events
(
    event_id     UNIQUEIDENTIFIER NOT NULL,
    event_type   VARCHAR(100)     NOT NULL,
    processed_at DATETIME2        NOT NULL,

    CONSTRAINT pk_processed_events
        PRIMARY KEY (event_id)
);