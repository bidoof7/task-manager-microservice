ALTER TABLE outbox_events
    ADD event_id UNIQUEIDENTIFIER NULL;
GO

UPDATE outbox_events
SET event_id = NEWID()
WHERE event_id IS NULL;
GO

ALTER TABLE outbox_events
ALTER COLUMN event_id UNIQUEIDENTIFIER NOT NULL;
GO

ALTER TABLE outbox_events
    ADD CONSTRAINT uq_outbox_events_event_id
        UNIQUE (event_id);
GO