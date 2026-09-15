UPDATE tasks
SET group_id = (
    SELECT id
    FROM task_group
    WHERE name = 'DEFAULT_GROUP'
      AND created_by = 'admin'
)
WHERE group_id IS NULL;

ALTER TABLE tasks
    ADD CONSTRAINT FK_task_group
        FOREIGN KEY (group_id)
            REFERENCES task_group(id);