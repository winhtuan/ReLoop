INSERT INTO users (user_id, FullName, role, Address, PhoneNumber, email, is_premium, premium_expiry, balance) VALUES
('US00001', 'Minh Tuan', 'admin', '123 Main Street, New York', '1234567890', 'johnsmith@example.com', 0, NULL, 1000.00),
('US00002', 'Alice Johnson', 'shopkeeper', '456 Oak Avenue, San Francisco', '9876543210', 'alicejohnson@example.com', 0, NULL, 500.00),
('US00003', 'Bob Williams', 'supporter', '789 Pine Road, Chicago', '5555555555', 'bobwilliams@example.com', 0, NULL, 0.00);
INSERT INTO categories (category_id, name, parent_id) VALUES
(1, 'Electronics', NULL),
(2, 'Vehicles', NULL),
(3, 'Home Appliances', NULL),
(4, 'Furniture', NULL),
(5, 'Fashion', NULL),
(6, 'Jewelry', NULL),
(7, 'Pets', NULL),
(8, 'Digital Goods', NULL),
(9, 'Books & Documents', NULL);
INSERT INTO vouchers (voucher_id, code, description, discount_type, discount_value, max_discount, min_order_amount, start_date, end_date, usage_limit, used_count, is_active) VALUES
('VOU001', 'WELCOME10', '10% off for new customers', 'percent', 10.00, 50.00, 100.00, '2025-06-01 00:00:00', '2025-12-31 23:59:59', 100, 0, 1),
('VOU002', 'SAVE20', 'Save $20 on orders above $200', 'fixed', 20.00, 20.00, 200.00, '2025-06-01 00:00:00', '2025-12-31 23:59:59', 50, 0, 1);
INSERT INTO shipping_methods (id, name, description, cost, estimated_days, is_active) VALUES
(1, 'Standard Shipping', 'Delivery within 5-7 business days', 5.00, 7, 1),
(2, 'Express Shipping', 'Fast delivery within 2-3 business days', 15.00, 3, 1);
INSERT INTO Account (acc_id, password, email, regisDate, user_id, verification_token, is_verified) VALUES 
('ACC0001', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'thanhloc20092004@gmail.com', '2025-05-25 00:00:00', 'US00001', '10b249bc-f42d-4df0-95c0-daa80cb2ad3c', 1);
-- pass 12345 
INSERT INTO conversation (conversation_id, sender_id, receiver_id, product_id)
VALUES
('CONV001', 'US00001', 'US00002', 'PROD001');
-- Insert messages
INSERT INTO Messages (message_id, conversation_id, sender_id, content, isRead, type, SentAt, is_recall)
VALUES
('MSG0001', 'CONV001', 'US00001', 'Hello, is this still available?', 0, 'text', NOW(), 0),
('MSG0002', 'CONV001', 'US00002', 'Yes, it is.', 0, 'text', NOW(), 0);
-- Insert paid_service
INSERT INTO paid_service (paid_id, service_name, price, start_date, usage_time) VALUES
('PRO0001', 'Premium', 200000, NOW(), 30),
('PRO0002', 'Priority Post', 100000, NOW(), 7);

INSERT INTO product (product_id, user_id, category_id, title, description, price, location, status, is_priority, created_at, updated_at) VALUES
('PROD001', 'US00002', NULL, 'Men\'s T-Shirt', 'High-quality cotton men\'s t-shirt', 15.00, 'New York', 'active', 0, NOW(), NOW()),
('PROD002', 'US00002', NULL, 'Sneaker Shoes', 'Trendy sports sneakers', 50.00, 'San Francisco', 'active', 1, NOW(), NOW()),
('PROD003', 'US00003', NULL, 'Travel Backpack', 'Waterproof travel backpack', 30.00, 'Chicago', 'active', 0, NOW(), NOW());
