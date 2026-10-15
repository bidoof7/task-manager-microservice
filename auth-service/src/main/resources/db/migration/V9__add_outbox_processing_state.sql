ALTER TABLE outbox_events
    ADD claimed_at DATETIME2 NULL;