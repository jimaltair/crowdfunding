INSERT into operation_type (id, description, type) values
(1, 'adding funds to your account', 'TOP_UP'),
(2, 'transfer of funds within the platform', 'PAYMENT'),
(3, 'refund within the platform', 'REFUND'),
(4, 'withdrawal of funds from the platform', 'WITHDRAW')
ON CONFLICT (id) DO NOTHING;

