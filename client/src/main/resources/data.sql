INSERT INTO project_status
    (id, description, status)
VALUES
    (1, 'Not confirmed', 'NOT_CONFIRMED'),
    (2, 'Confirmed', 'CONFIRMED'),
    (3, 'Finished', 'FINISHED'),
    (4, 'Canceled', 'CANCELED')
ON CONFLICT (id) DO NOTHING;
