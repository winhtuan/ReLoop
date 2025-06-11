INSERT INTO users (user_id, FullName, role, Address, PhoneNumber, email, is_premium, premium_expiry, balance) VALUES
('CUS0001', 'John Smitch', 'shopkeeper', '123 Main Street, New York', '1234567890', 'johnsmith@example.com', 0, NULL, 1000.00),
('CUS0002', 'John Doe', 'user', '123 Main Street, New York', '1234562890', 'johndoe@example.com', 0, NULL, 1000.00),
('CUS0003', 'Alice Johnson', 'supporter', '456 Oak Avenue, San Francisco', '9876543210', 'alicejohnson@example.com', 0, NULL, 500.00),
('CUS0004', 'Bob Williams', 'admin', '789 Pine Road, Chicago', '5555555555', 'bobwilliams@example.com', 0, NULL, 0.00);
INSERT INTO Account (acc_id, password, email, regisDate, user_id, verification_token, is_verified) VALUES
('ACC0001', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'johnsmith@example.com', NOW(), 'CUS0001', 'token1', 1),
('ACC0002', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'johndoe@example.com', NOW(), 'CUS0002', 'token1', 1),
('ACC0003', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'alice@example.com', NOW(), 'CUS0003', 'token2', 0),
('ACC0004', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'bob@example.com', NOW(), 'CUS0004', 'token3', 1);
INSERT INTO categories (category_id, name) VALUES -- chưa update
(1, 'Electronics'),
(2, 'Vehicles'),
(3, 'Home Appliances'),
(4, 'Furniture'),
(5, 'Fashion'),
(6, 'Jewelry'),
(7, 'Pets'),
(8, 'Digital Goods'),
(9, 'Books & Documents');
INSERT INTO shipping_methods (id, name, description, cost, estimated_days, is_active) VALUES
(1, 'Standard Shipping', 'Delivery within 5-7 business days', 5.00, 7, 1),
(2, 'Express Shipping', 'Fast delivery within 2-3 business days', 15.00, 3, 1);
INSERT INTO conversation (conversation_id, sender_id, receiver_id, product_id) VALUES
('CON0001', 'CUS0001', 'CUS0002', 'PRD0001'),
('CON0002', 'CUS0002', 'CUS0003', 'PRD0002'),
('CON0003', 'CUS0003', 'CUS0001', 'PRD0003');
INSERT INTO Messages (message_id, conversation_id, sender_id, content, isRead, type, SentAt, is_recall) VALUES
('MSG0001', 'CON0001', 'CUS0001', 'Hi! Is the product still available?', 0, 'text', NOW(), 0),
('MSG0002', 'CON0002', 'CUS0002', 'Yes, still here.', 0, 'text', NOW(), 0),
('MSG0003', 'CON0003', 'CUS0003', 'Interested in your product.', 0, 'text', NOW(), 0);
