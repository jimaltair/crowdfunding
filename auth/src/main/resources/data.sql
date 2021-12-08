INSERT INTO role
    (role_id, name)
VALUES
    (1, 'USER'),
    (2, 'MODERATOR'),
    (3, 'ADMIN')
ON CONFLICT (role_id) DO NOTHING;

INSERT INTO status
    (status_id, name)
VALUES
    (1, 'NOT_CONFIRMED'),
    (2, 'CONFIRMED'),
    (3, 'BANNED'),
    (3, 'DELETED')
ON CONFLICT (status_id) DO NOTHING;
