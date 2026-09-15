INSERT INTO task_group (name,
                        created_by,
                        created_at,
                        updated_by,
                        updated_at)
VALUES ('DEFAULT_GROUP',
        'admin',
        SYSDATETIME(),
        'admin',
        SYSDATETIME());
