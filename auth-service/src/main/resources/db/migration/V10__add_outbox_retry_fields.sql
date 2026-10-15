ALTER TABLE outbox_events
    ADD retry_count INT NOT NULL DEFAULT 0;

ALTER TABLE outbox_events
    ADD next_retry_at DATETIME2 NULL;

ALTER TABLE outbox_events
    ADD last_error NVARCHAR(2000) NULL;