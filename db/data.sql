INSERT INTO users (user_id, FullName, role, Address, PhoneNumber, email, is_premium, premium_expiry, balance, img) VALUES
('CUS0001', 'John Smitch', 'shopkeeper', '123 Main Street, New York', '1234567890', 'johnsmith@example.com', 0, NULL, 1000.00, 'https://placehold.co/400'),
('CUS0002', 'John Doe', 'user', '123 Main Street, New York', '1234562890', 'johndoe@example.com', 0, NULL, 1000.00, 'https://placehold.co/400'),
('CUS0003', 'Alice Johnson', 'supporter', '456 Oak Avenue, San Francisco', '9876543210', 'alicejohnson@example.com', 0, NULL, 500.00, 'https://placehold.co/400'),
('CUS0004', 'Bob Williams', 'admin', '789 Pine Road, Chicago', '5555555555', 'bobwilliams@example.com', 0, NULL, 0.00, 'https://placehold.co/400'),
('CUS0005', 'Emma Brown', 'shopkeeper', '112 Maple Drive, Austin', '5551112233', 'emmabrown@example.com', 1, '2025-06-01 00:00:00', 1500.00, 'https://placehold.co/400'),
('CUS0006', 'Liam Harris', 'user', '789 Elm Street, Miami', '5552334455', 'liamharris@example.com', 0, NULL, 1000.00, 'https://placehold.co/400'),
('CUS0007', 'Sophia Davis', 'user', '321 Birch Road, Denver', '5555445566', 'sophiadavis@example.com', 0, NULL, 2000.00, 'https://placehold.co/400'),
('CUS0008', 'Mason Martinez', 'shopkeeper', '654 Pine Avenue, Seattle', '5556677880', 'masonmartinez@example.com', 1, '2025-06-01 00:00:00', 1200.00, 'https://placehold.co/400'),
('CUS0009', 'Olivia Anderson', 'user', '987 Oak Lane, Los Angeles', '5558899000', 'oliviaanderson@example.com', 0, NULL, 300.00, 'https://placehold.co/400'),
('CUS0010', 'James Wilson', 'admin', '543 Cedar Street, Boston', '5551122334', 'jameswilson@example.com', 0, NULL, 0.00, 'https://placehold.co/400'),
('CUS0011', 'Isabella Lee', 'supporter', '678 Maple Drive, Phoenix', '5552233445', 'isabellalee@example.com', 1, '2025-07-01 00:00:00', 800.00, 'https://placehold.co/400'),
('CUS0012', 'Lucas Scott', 'shopkeeper', '321 Elm Street, Dallas', '5553344556', 'lucasscott@example.com', 0, NULL, 1200.00, 'https://placehold.co/400'),
('CUS0013', 'Charlotte Walker', 'user', '432 Pine Avenue, Houston', '5554455667', 'charlottewalker@example.com', 0, NULL, 950.00, 'https://placehold.co/400'),
('CUS0014', 'Henry Adams', 'supporter', '876 Birch Road, Chicago', '5555566778', 'henryadams@example.com', 1, '2025-08-01 00:00:00', 600.00, 'https://placehold.co/400'),
('CUS0015', 'Amelia Taylor', 'admin', '234 Oak Lane, San Francisco', '5556677881', 'ameliataylor@example.com', 0, NULL, 0.00, 'https://placehold.co/400');

INSERT INTO Account (acc_id, password, email, regisDate, user_id, verification_token, is_verified) VALUES
('ACC0001', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'johnsmith@example.com', NOW(), 'CUS0001', 'token1', 1),
('ACC0002', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'johndoe@example.com', NOW(), 'CUS0002', 'token1', 1),
('ACC0003', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'alicejohnson@example.com', NOW(), 'CUS0003', 'token2', 0),
('ACC0004', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'bobwilliams@example.com', NOW(), 'CUS0004', 'token3', 1),
('ACC0005', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'emmabrown@example.com', NOW(), 'CUS0005', 'token4', 1),
('ACC0006', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'liamharris@example.com', NOW(), 'CUS0006', 'token5', 1),
('ACC0007', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'sophiadavis@example.com', NOW(), 'CUS0007', 'token6', 1),
('ACC0008', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'masonmartinez@example.com', NOW(), 'CUS0008', 'token7', 1),
('ACC0009', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'oliviaanderson@example.com', NOW(), 'CUS0009', 'token8', 0),
('ACC0010', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'jameswilson@example.com', NOW(), 'CUS0010', 'token9', 1),
('ACC0011', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'isabellalee@example.com', NOW(), 'CUS0011', 'token10', 1),
('ACC0012', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'lucasscott@example.com', NOW(), 'CUS0012', 'token11', 1),
('ACC0013', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'charlottewalker@example.com', NOW(), 'CUS0013', 'token12', 0),
('ACC0014', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'henryadams@example.com', NOW(), 'CUS0014', 'token13', 1),
('ACC0015', '$2a$10$btBRXfHjhF8byrYchhbmkuUWNbDML73VKTNbeVxtmapZ8LGKyEP2i', 'ameliataylor@example.com', NOW(), 'CUS0015', 'token14', 1);

-- Cấp 1: Các danh mục cha
INSERT INTO categories (category_id, name, parent_id, slug, level) VALUES
(1, 'Electronics', NULL, 'electronics', 1), -- đồ điện tử
(2, 'Vehicles', NULL, 'vehicles', 1), -- xe cộ
(3, 'Home Appliances', NULL, 'home-appliances', 1), -- thiết bị gia dụng
(4, 'Furniture', NULL, 'furniture', 1), -- nội thất
(5, 'Fashion', NULL, 'fashion', 1), -- thời trang
(6, 'Jewelry', NULL, 'jewelry', 1), -- trang sức
(7, 'Pets', NULL, 'pets', 1), -- thú cưng
(8, 'Digital Goods', NULL, 'digital-goods', 1), -- sản phẩm điện tử
(9, 'Books & Documents', NULL, 'books-documents', 1); -- sách & tài liệu

-- Cấp 2: Các danh mục con với `parent_id` tham chiếu đến các danh mục cha
-- Electronics
INSERT INTO categories (category_id, name, parent_id, slug, level) VALUES
(10, 'Laptop', 1, 'laptop', 2),
(11, 'Phone', 1, 'phone', 2),
(12, 'Camera', 1, 'camera', 2),
(13, 'TV', 1, 'tv', 2),
(14, 'Accessory', 1, 'accessory', 2);

-- Vehicles
INSERT INTO categories (category_id, name, parent_id, slug, level) VALUES
(15, 'Car', 2, 'car', 2),
(16, 'Mortorbike', 2, 'mortorbike', 2),
(17, 'Tram', 2, 'tram', 2),
(18, 'Components', 2, 'components', 2);

-- Home Appliances
INSERT INTO categories (category_id, name, parent_id, slug, level) VALUES
(19, 'Refrigerators', 3, 'refrigerators', 2),
(20, 'Washing Machines', 3, 'washing-machines', 2),
(21, 'Kitchen Appliances', 3, 'kitchen-appliances', 2),
(22, 'Air-Conditioners', 3, 'air-conditioners', 2),
(23, 'Blenders', 3, 'blenders', 2);

-- Furniture
INSERT INTO categories (category_id, name, parent_id, slug, level) VALUES
(24, 'Cabinets', 4, 'cabinets', 2),
(25, 'Tables', 4, 'tables', 2),
(26, 'Chairs', 4, 'chairs', 2),
(27, 'Beds', 4, 'beds', 2),
(28, 'Office Furniture', 4, 'office-furniture', 2);

-- Fashion
INSERT INTO categories (category_id, name, parent_id, slug, level) VALUES
(29, 'Clothing', 5, 'clothing', 2),
(30, 'Hats & Caps', 5, 'hat', 2),
(31, 'Shoes', 5, 'shoes', 2),
(32, 'Bags', 5, 'bags', 2),
(33, 'Sunglasses', 5, 'sunglasses', 2);

-- Jewelry
INSERT INTO categories (category_id, name, parent_id, slug, level) VALUES
(34, 'Rings', 6, 'rings', 2),
(35, 'Watches', 6, 'watches', 2),
(36, 'Earrings', 6, 'earrings', 2),
(37, 'Necklaces', 6, 'necklaces', 2);

-- Pets
INSERT INTO categories (category_id, name, parent_id, slug, level) VALUES
(38, 'Pet Food', 7, 'pet-food', 2),
(39, 'Pet Toys', 7, 'pet-toys', 2),
(40, 'Pet Clothing', 7, 'pet-clothing', 2),
(41, 'Dogs', 7, 'dogs', 2),
(42, 'Cats', 7, 'cats', 2),
(43, 'Birds', 7, 'birds', 2),
(44, 'Fish', 7, 'fish', 2),
(45, 'Hamsters', 7, 'hamsters', 2),
(46, 'Turtles', 7, 'turtles', 2);

-- Digital Goods
INSERT INTO categories (category_id, name, parent_id, slug, level) VALUES
(47, 'Software', 8, 'software', 2),
(48, 'EBooks', 8, 'ebooks', 2),
(49, 'Courses', 8, 'courses', 2),
(50, 'Licenses', 8, 'licenses', 2);

-- Books & Documents
INSERT INTO categories (category_id, name, parent_id, slug, level) VALUES
(51, 'Textbooks', 9, 'textbooks', 2),
(52, 'Novels', 9, 'novels', 2),
(53, 'Comics', 9, 'comics', 2),
(54, 'Books', 9, 'books', 2);

INSERT INTO PaidService (paid_id, service_name, price, description, usage_time) VALUES 
('PRO0001', 'Premium', 5000, 'Access to premium features and content.', 30),
('PRO0002', 'Priority Post', 2000, 'Priority placement for your posts in listings.', 1);

INSERT INTO conversation (conversation_id, sender_id, receiver_id, product_id) VALUES
('CON0001', 'CUS0001', 'CUS0002', 'PRD0001'),
('CON0002', 'CUS0002', 'CUS0003', 'PRD0002'),
('CON0003', 'CUS0003', 'CUS0001', 'PRD0003');
