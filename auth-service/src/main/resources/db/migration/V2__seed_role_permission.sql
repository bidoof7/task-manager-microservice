-- Roles
INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER');

-- Permissions
INSERT INTO permissions (name)
VALUES ('TASK_READ'),
       ('TASK_CREATE'),
       ('TASK_UPDATE'),
       ('TASK_DELETE');

-- ADMIN có tất cả permission
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         CROSS JOIN permissions p
WHERE r.name = 'ADMIN';


-- USER chỉ có READ + CREATE + UPDATE
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON p.name IN (
                            'TASK_READ',
                            'TASK_CREATE',
                            'TASK_UPDATE'
                  )
WHERE r.name = 'USER';