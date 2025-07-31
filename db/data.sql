INSERT INTO users (user_id, FullName, role, Address, PhoneNumber, email, is_premium, premium_expiry, balance, img) VALUES
('CUS0001', 'John Smitch', 'shopkeeper', '247 Đường Trần Đại Nghĩa, Hoà Hải, Ngũ Hành Sơn, Đà Nẵng, Việt Nam', '1234567890', 'johnsmith@example.com', 0, NULL, 1000.00, 'https://upload.wikimedia.org/wikipedia/commons/f/fd/John_de_Lancie_Photo_Op_GalaxyCon_Raleigh_2023.jpg'),
('CUS0002', 'John Doe', 'user', '306 Đường Trần Đại Nghĩa, Điện Ngọc, Ngũ Hành Sơn, Đà Nẵng, Việt Nam', '1234562890', 'johndoe@example.com', 0, NULL, 1000.00, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQoU--uMeTvtsF5UhuSpYTl-xI32WmWAwThQ&s'),
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
UPDATE users
SET 
    Address = '306 Đường Trần Đại Nghĩa, Điện Ngọc, Ngũ Hành Sơn, Đà Nẵng, Việt Nam',
    img = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQoU--uMeTvtsF5UhuSpYTl-xI32WmWAwThQ&s'
WHERE user_id = 'CUS0002';
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

INSERT INTO cart (cart_id, user_id)
VALUES
('CRT0001', 'CUS0002'),
 ('CRT0002', 'CUS0001');

INSERT INTO cart_items (cart_id, product_id, quantity, price)
VALUES 
('CRT0001', 'PRD0001', 2, 1500000),
('CRT0001', 'PRD0002', 1, 800000),
('CRT0002', 'PRD0003', 3, 2500000),
('CRT0002', 'PRD0004', 1, 2000000);

INSERT INTO vouchers (voucher_id, code, description, discount_value, min_order_amount, start_date, end_date, usage_limit, used_count, is_active) VALUES 
('VOU0001', 'SUMMER2025', 'Giảm 10k cho đơn hàng từ 100K, áp dụng toàn hệ thống.', 10000.00, 100000.00, '2025-06-01 00:00:00', '2025-06-30 23:59:59', 100, 0, 1),
('VOU0002', 'FREESHIP', 'Miễn phí vận chuyển cho đơn từ 120K, chỉ dùng 1 lần/người.', 35000.00, 120000.00, '2025-06-10 00:00:00', '2025-07-10 23:59:59', 50, 10, 1),
('VOU0003', 'NEWUSER', 'Voucher dành cho khách hàng mới, giảm ngay 50K cho đơn đầu tiên.', 50000.00, 0.00, '2025-01-01 00:00:00', '2025-12-31 23:59:59', 200, 45, 1),
('VOU0004', 'WELCOME10', 'Giảm 10k cho đơn hàng đầu tiên', 10000, 50000, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 100, 0, 1),
('VOU0005', 'FREESHIP', 'Miễn phí vận chuyển', 15000, 100000, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 200, 0, 1),
('VOU0006', 'SUMMER15', 'Giảm 15k cho đơn mùa hè', 15000, 70000, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 150, 0, 1),
('VOU0007', 'VIP30', 'Ưu đãi cho khách VIP', 30000, 150000, NOW(), DATE_ADD(NOW(), INTERVAL 60 DAY), 50, 0, 1);

INSERT INTO user_vouchers (user_voucher_id, user_id, voucher_id, assigned_at, is_used) VALUES
('UVU0001', 'CUS0001', 'VOU0001', NOW(), 0),
('UVU0002', 'CUS0002', 'VOU0001', NOW(), 0),
('UVU0003', 'CUS0003', 'VOU0001', NOW(), 0),
('UVU0004', 'CUS0004', 'VOU0001', NOW(), 1),
('UVU0005', 'CUS0005', 'VOU0001', NOW(), 0),
('UVU0006', 'CUS0006', 'VOU0001', NOW(), 0),
('UVU0007', 'CUS0007', 'VOU0001', NOW(), 1),
('UVU0008', 'CUS0008', 'VOU0001', NOW(), 0),
('UVU0009', 'CUS0009', 'VOU0001', NOW(), 0),
('UVU0010', 'CUS0010', 'VOU0001', NOW(), 0),

('UVU0011', 'CUS0001', 'VOU0002', NOW(), 1),
('UVU0012', 'CUS0002', 'VOU0002', NOW(), 0),
('UVU0013', 'CUS0003', 'VOU0002', NOW(), 0),
('UVU0014', 'CUS0004', 'VOU0002', NOW(), 0),
('UVU0015', 'CUS0005', 'VOU0002', NOW(), 0),
('UVU0016', 'CUS0006', 'VOU0002', NOW(), 1),
('UVU0017', 'CUS0007', 'VOU0002', NOW(), 0),
('UVU0018', 'CUS0008', 'VOU0002', NOW(), 0),
('UVU0019', 'CUS0009', 'VOU0002', NOW(), 0),
('UVU0020', 'CUS0010', 'VOU0002', NOW(), 0),

('UVU0021', 'CUS0001', 'VOU0003', NOW(), 0),
('UVU0022', 'CUS0002', 'VOU0003', NOW(), 0),
('UVU0023', 'CUS0003', 'VOU0003', NOW(), 0),
('UVU0024', 'CUS0004', 'VOU0003', NOW(), 0),
('UVU0025', 'CUS0005', 'VOU0003', NOW(), 1),
('UVU0026', 'CUS0006', 'VOU0003', NOW(), 0),
('UVU0027', 'CUS0007', 'VOU0003', NOW(), 0),
('UVU0028', 'CUS0008', 'VOU0003', NOW(), 0),
('UVU0029', 'CUS0009', 'VOU0003', NOW(), 0),
('UVU0030', 'CUS0010', 'VOU0003', NOW(), 0),

('UVU0031', 'CUS0011', 'VOU0001', NOW(), 0),
('UVU0032', 'CUS0012', 'VOU0001', NOW(), 0),
('UVU0033', 'CUS0013', 'VOU0001', NOW(), 0),
('UVU0034', 'CUS0014', 'VOU0001', NOW(), 1),
('UVU0035', 'CUS0015', 'VOU0001', NOW(), 0),

('UVU0036', 'CUS0011', 'VOU0002', NOW(), 0),
('UVU0037', 'CUS0012', 'VOU0002', NOW(), 0),
('UVU0038', 'CUS0013', 'VOU0002', NOW(), 0),
('UVU0039', 'CUS0014', 'VOU0002', NOW(), 0),
('UVU0040', 'CUS0015', 'VOU0002', NOW(), 1),

('UVU0041', 'CUS0011', 'VOU0003', NOW(), 0),
('UVU0042', 'CUS0012', 'VOU0003', NOW(), 0),
('UVU0043', 'CUS0013', 'VOU0003', NOW(), 0),
('UVU0044', 'CUS0014', 'VOU0003', NOW(), 0),
('UVU0045', 'CUS0015', 'VOU0003', NOW(), 0),

-- Một vài người dùng nhận thêm voucher khác
('UVU0046', 'CUS0001', 'VOU0004', NOW(), 0),
('UVU0047', 'CUS0002', 'VOU0004', NOW(), 1),
('UVU0048', 'CUS0003', 'VOU0004', NOW(), 0),
('UVU0049', 'CUS0004', 'VOU0004', NOW(), 0),
('UVU0050', 'CUS0005', 'VOU0004', NOW(), 0);

INSERT INTO notification 
(noti_id, user_id, title, content, link, type, created_at, is_read)
VALUES
('NOT0001', 'CUS0002', 
 'Premium Activated',
 'Premium service has been activated for your account! Enjoy all advanced features now.',
 NULL,
 'PREMIUM',
 NOW(),
 FALSE);

-- Các category là đồ vật
INSERT INTO category_state_options (category_id, options)
SELECT category_id, '["mới", "cũ", "hư hỏng nhẹ"]'
FROM categories
WHERE category_id IN (10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,47,48,49,50,51,52,53,54);

-- Các category là vật sống
INSERT INTO category_state_options (category_id, options)
SELECT category_id, '["trưởng thành", "con non"]'
FROM categories
WHERE category_id IN (41,42,43,44,45,46);

-- Laptop (category_id = 10)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(10, 'Brand', 'select', '["Acer", "Apple", "Asus", "Dell", "Honor", "HP", "Lenovo", "Microsoft", "MSI", "Other"]', TRUE),
(10, 'RAM (GB)', 'select', '["2GB", "4GB", "8GB", "16GB", "32GB", "64GB"]', TRUE),
(10, 'Storage Capacity', 'select', '["128GB", "250GB", "256GB", "320GB", "480GB", "500GB", "512GB", "640GB", "700GB", "750GB", "1TB"]', TRUE),
(10, 'Storage Type', 'select', '["HDD", "SSD", "Hybrid"]', TRUE),
(10, 'Processor (CPU)', 'select', '["Intel Core i3", "Intel Core i5", "Intel Core i7", "Intel Core i9", "AMD Ryzen 3", "AMD Ryzen 5", "AMD Ryzen 7", "AMD Ryzen 9", "Apple M1", "Apple M2", "Other"]', TRUE),
(10, 'Graphics Card (GPU)', 'select', '["Intel UHD", "Intel Iris", "NVIDIA", "AMD", "Other"]', TRUE),
(10, 'Screen Size', 'select', '["9 - 10.9 inch", "11 - 12.9 inch", "13 - 14.9 inch", "15 - 16.9 inch", "17 - 18.9 inch", "19 - 20.9 inch", "Other"]', TRUE),
(10, 'Operating System', 'select', '["Windows", "macOS", "Linux", "Other"]', TRUE),
(10, 'Battery Condition', 'select', '["Good", "Average", "Needs Replacement"]', TRUE),
(10, 'Warranty Policy', 'select', '["Out of Warranty", "1 month", "2 months", "3 months", "4-6 months", "7-12 months", "13-24 months", "Still Under Warranty"]', TRUE);

-- Phone (category_id = 11)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(11, 'Brand', 'select', '["Apple", "Huawei", "Oppo", "Realme", "Samsung", "Vivo", "Xiaomi", "Other"]', TRUE),
(11, 'Color', 'select', '["Silver", "Black", "Red", "Pink", "White", "Gold", "Gray", "Blue", "Green", "Purple", "Other"]', TRUE),
(11, 'Internal Storage (ROM)', 'select', '["8GB", "16GB", "32GB", "64GB", "128GB", "256GB", "512GB", "1TB", "2TB", "Other"]', TRUE),
(11, 'RAM', 'select', '["2GB", "4GB", "6GB", "8GB", "12GB"]', TRUE),
(11, 'Warranty Policy', 'select', '["Out of Warranty", "1 month", "2 months", "3 months", "4-6 months", "7-12 months", "13-24 months", "Still Under Warranty"]', TRUE);

-- Camera (category_id = 12)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(12, 'Camera Type', 'select', '["DSLR", "Mirrorless", "Compact", "GoPro", "Other"]', TRUE),
(12, 'Brand', 'select', '["Canon", "Nikon", "Sony", "Fujifilm", "Panasonic", "Other"]', TRUE),
(12, 'Warranty Policy', 'select', '["Out of Warranty", "1 month", "2 months", "3 months", "4-6 months", "7-12 months", "13-24 months", "Still Under Warranty"]', TRUE);

-- TV (category_id = 13)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(13, 'Brand', 'select', '["Sony", "Samsung", "LG", "TCL", "Panasonic", "Sharp", "Other"]', TRUE),
(13, 'Size', 'select', '["32 inch", "40 inch", "43 inch", "50 inch", "55 inch", "65 inch", "75 inch", "Other"]', TRUE),
(13, 'Warranty Policy', 'select', '["Out of Warranty", "1 month", "2 months", "3 months", "4-6 months", "7-12 months", "13-24 months", "Still Under Warranty"]', TRUE);

-- Accessory (category_id = 14)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(14, 'Accessory Type', 'select', '["Monitor", "Mouse", "Keyboard", "Headphones", "Charging Cable", "Power Bank", "Other"]', TRUE),
(14, 'Warranty Policy', 'select', '["Out of Warranty", "1 month", "2 months", "3 months", "4-6 months", "7-12 months", "13-24 months", "Still Under Warranty"]', TRUE);

-- Car (category_id = 15)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(15, 'Brand', 'select', '["Audi", "Bentley", "BMW", "Cadillac", "Ferrari", "Ford", "Honda", "Hyundai", "Jeep", "Kia", "Lamborghini", "Lexus", "Mazda", "Mercedes", "Mitsubishi", "Nissan", "Porsche", "Rolls Royce", "Suzuki", "Tesla", "Toyota", "VinFast", "Other"]', TRUE),
(15, 'Year of Manufacture', 'select', '["Before 1980", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025"]', TRUE),
(15, 'Seating Capacity', 'select', '["2", "4", "5", "6", "7", "8", "9", "10", "12", "14", "16"]', TRUE),
(15, 'Color', 'select', '["Silver", "Black", "Red", "Pink", "White", "Gold", "Gray", "Blue", "Green", "Purple", "Other"]', TRUE),
(15, 'Fuel Type', 'select', '["Petrol", "Diesel", "Electric", "Hybrid"]', TRUE),
(15, 'Transmission', 'select', '["Manual", "Automatic", "Semi-automatic"]', TRUE),
(15, 'Origin', 'select', '["Vietnam", "India", "South Korea", "Thailand", "Taiwan", "Japan", "China", "USA", "Germany", "Other"]', TRUE),
(15, 'Mileage (km)', 'number', NULL, TRUE);

-- Motorbike (category_id = 16)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(16, 'Brand', 'select', '["Air Blade", "Honda", "Lead", "Suzuki", "Vision", "Yamaha", "Other"]', TRUE),
(16, 'Engine Displacement (cc)', 'select', '["50", "110", "125", "150", "175", "250+"]', TRUE),
(15, 'Registration Year', 'select', '["Before 1980", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025"]', TRUE),
(16, 'Motorbike Type', 'select', '["Manual", "Scooter", "Clutch"]', TRUE),
(16, 'Origin', 'select', '["Vietnam", "India", "South Korea", "Thailand", "Taiwan", "Japan", "China", "USA", "Germany", "Other"]', TRUE),
(16, 'License Plate', 'text', NULL, FALSE),
(16, 'Mileage (km)', 'number', NULL, FALSE);

-- Tram (category_id = 17)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(17, 'Vehicle Type', 'select', '["Electric Bicycle", "Electric Scooter"]', TRUE),
(17, 'Battery Condition', 'select', '["Good", "Normal", "Needs Replacement"]', TRUE),
(17, 'Motor Power', 'select', '["Below 200W", "200 - 250W", "251 - 350W", "351 - 500W", "501 - 1000W", "Above 1000W"]', TRUE),
(17, 'Origin', 'select', '["Vietnam", "India", "South Korea", "Thailand", "Taiwan", "Japan", "China", "USA", "Germany", "Other"]', TRUE),
(17, 'Usage Duration', 'text', NULL, FALSE);

-- Components (category_id = 18)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(18, 'Component Type', 'select', '["Motorbike Parts", "Car Parts", "Electric Vehicle Parts", "Other Parts"]', TRUE),
(18, 'Origin', 'select', '["Vietnam", "India", "South Korea", "Thailand", "Taiwan", "Japan", "China", "USA", "Germany", "Other"]', TRUE);

-- Refrigerators (category_id = 19)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(19, 'Brand', 'select', '["Samsung", "LG", "Panasonic", "Sharp", "Electrolux", "Toshiba"]', TRUE),
(19, 'Capacity', 'select', '["100L", "150L", "200L", "300L", "400L", "500L+"]', TRUE),
(19, 'Refrigerator Type', 'select', '["Single Door", "Double Door", "Side by Side", "Bottom Freezer"]', TRUE),
(19, 'Origin', 'select', '["Vietnam", "India", "South Korea", "Thailand", "Taiwan", "Japan", "China", "USA", "Germany", "Other"]', TRUE),
(19, 'Warranty Policy', 'select', '["Out of Warranty", "1 month", "2 months", "3 months", "4-6 months", "7-12 months", "13-24 months", "Still Under Warranty"]', TRUE);

-- Washing Machines (category_id = 20)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(20, 'Brand', 'select', '["Samsung", "LG", "Panasonic", "Toshiba", "Electrolux", "Aqua"]', TRUE),
(20, 'Machine Type', 'select', '["Top Load", "Front Load"]', TRUE),
(20, 'Washing Capacity', 'select', '["7kg", "8kg", "9kg", "10kg", "12kg+"]', TRUE),
(20, 'Origin', 'select', '["Vietnam", "India", "South Korea", "Thailand", "Taiwan", "Japan", "China", "USA", "Germany", "Other"]', TRUE),
(20, 'Warranty Policy', 'select', '["Out of Warranty", "1 month", "2 months", "3 months", "4-6 months", "7-12 months", "13-24 months", "Still Under Warranty"]', TRUE);

-- Kitchen Appliances (category_id = 21)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(21, 'Appliance Type', 'select', '["Microwave", "Gas Stove", "Induction Cooker", "Rice Cooker", "Blender", "Other"]', TRUE),
(21, 'Brand', 'text', NULL, TRUE),
(21, 'Origin', 'select', '["Vietnam", "India", "South Korea", "Thailand", "Taiwan", "Japan", "China", "USA", "Germany", "Other"]', TRUE);

-- Air-Conditioners (category_id = 22)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(22, 'Brand', 'select', '["Daikin", "Panasonic", "LG", "Toshiba", "Samsung", "Other"]', TRUE),
(22, 'Cooling Capacity', 'select', '["1HP", "1.5HP", "2HP", "2.5HP"]', TRUE),
(22, 'AC Type', 'select', '["1-Way", "2-Way", "Inverter"]', TRUE),
(22, 'Origin', 'select', '["Vietnam", "India", "South Korea", "Thailand", "Taiwan", "Japan", "China", "USA", "Germany", "Other"]', TRUE),
(22, 'Warranty Policy', 'select', '["Out of Warranty", "1 month", "2 months", "3 months", "4-6 months", "7-12 months", "13-24 months", "Still Under Warranty"]', TRUE);

-- Blenders (category_id = 23)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(23, 'Brand', 'select', '["Philips", "Panasonic", "Sunhouse", "Bluestone", "Kangaroo", "Toshiba", "Other"]', TRUE),
(23, 'Capacity', 'select', '["<1L", "1L", "1.5L", "2L+"]', FALSE),
(23, 'Appliance Type', 'select', '["Blender", "Juicer", "Meat Grinder", "Other"]', TRUE);

-- Cabinets (category_id = 24)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(24, 'Cabinet Type', 'select', '["Wardrobe", "Kitchen Cabinet", "Shoe Cabinet", "Display Cabinet"]', TRUE),
(24, 'Material', 'select', '["Wood", "Plastic", "Iron", "Stainless Steel", "Other"]', TRUE);

-- Tables (category_id = 25)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(25, 'Table Type', 'select', '["Study Table", "Office Desk", "Dining Table", "Coffee Table", "Dressing Table", "Other"]', TRUE),
(25, 'Material', 'select', '["Wood", "Glass", "Plastic", "Metal"]', TRUE);

-- Chairs (category_id = 26)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(26, 'Chair Type', 'select', '["Sofa", "Dining Chair", "Office Chair", "Folding Chair", "Gaming Chair", "Other"]', TRUE),
(26, 'Material', 'select', '["Wood", "Leather", "Fabric", "Plastic", "Metal"]', TRUE);

-- Beds (category_id = 27)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(27, 'Bed Type', 'select', '["Single Bed", "Double Bed", "Bunk Bed", "Folding Bed"]', TRUE),
(27, 'Material', 'select', '["Wood", "Iron", "Plastic", "Stainless Steel"]', TRUE);

-- Office Furniture (category_id = 28)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(28, 'Furniture Type', 'select', '["Desk", "Chair", "File Cabinet", "Partition", "Storage Cabinet", "Other"]', TRUE),
(28, 'Material', 'select', '["Wood", "Plastic", "Metal", "Mixed", "Other"]', TRUE),
(28, 'Quantity', 'number', NULL, TRUE);

-- Clothing (category_id = 29)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(29, 'Clothing Type', 'select', '["Shirt", "Pants", "Dress", "Skirt", "Jacket", "Sportswear", "Other"]', TRUE);

-- Hats & Caps (category_id = 30)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(30, 'Hat Type', 'select', '["Cap", "Bucket Hat", "Beanie", "Wide-Brim Hat", "Helmet", "Other"]', TRUE),
(30, 'Material', 'select', '["Cotton", "Fleece", "Wool", "Plastic", "Leather"]', TRUE);

-- Shoes (category_id = 31)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(31, 'Shoe Type', 'select', '["Sneakers", "Dress Shoes", "Sandals", "High Heels", "Boots", "Other"]', TRUE),
(31, 'Size', 'select', '["35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "Other"]', TRUE);

-- Bags (category_id = 32)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(32, 'Bag Type', 'select', '["Handbag", "Crossbody Bag", "Backpack", "Travel Bag", "Laptop Bag", "Other"]', TRUE),
(32, 'Material', 'select', '["Leather", "Fabric", "Nylon", "Canvas", "Other"]', TRUE);

-- Sunglasses (category_id = 33)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(33, 'Sunglasses Type', 'select', '["Standard", "Fashion", "Polarized", "Sport"]', TRUE);

-- Rings (category_id = 34)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(34, 'Ring Type', 'select', '["Wedding Ring", "Couple Ring", "Fashion Ring", "Feng Shui Ring", "Other"]', TRUE),
(34, 'Material', 'select', '["Gold", "Silver", "Platinum", "Titanium", "Alloy", "Other"]', TRUE),
(34, 'Size', 'select', '["10", "12", "14", "16", "18", "20", "22", "24", "Other"]', TRUE);

-- Watches (category_id = 35)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(35, 'Watch Type', 'select', '["Mechanical", "Quartz", "Smartwatch", "Digital", "Other"]', TRUE);

-- Earrings (category_id = 36)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(36, 'Earring Type', 'select', '["Studs", "Drop", "Gemstone", "Hoop", "Cuff", "Other"]', TRUE);

-- Necklaces (category_id = 37)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(37, 'Necklace Type', 'select', '["Thin Chain", "Thick Chain", "Gemstone Pendant", "Name Engraved", "Beaded", "Other"]', TRUE),
(37, 'Material', 'select', '["Gold", "Silver", "Platinum", "Alloy", "Pearl", "Other"]', TRUE);

-- Pet Food (category_id = 38)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(38, 'Pet Type', 'select', '["Dog", "Cat", "Bird", "Fish", "Hamster", "Turtle"]', TRUE);

-- Pet Toys (category_id = 39)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(39, 'Toy Type', 'select', '["Chew Toy", "Ball", "Squeaky Toy", "Treat-Dispensing Toy", "Other"]', TRUE),
(39, 'Material', 'select', '["Plastic", "Fabric", "Rubber", "Wool", "Wood", "Other"]', TRUE),
(39, 'Suitable For', 'select', '["Dog", "Cat", "Other"]', TRUE);

-- Pet Clothing (category_id = 40)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(40, 'Clothing Type', 'select', '["Shirt", "Dress", "Raincoat", "Head Accessory", "Seasonal Outfit", "Other"]', TRUE),
(40, 'Size', 'select', '["XS", "S", "M", "L", "XL", "XXL"]', TRUE),
(40, 'Material', 'select', '["Cotton", "Wool", "Nylon", "Denim", "Other"]', TRUE),
(40, 'Suitable For', 'select', '["Dog", "Cat", "Other"]', TRUE);

-- Dogs (category_id = 41)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(41, 'Breed', 'select', '["Alaskan Malamute", "Pekingese", "German Shepherd", "Bulldog", "Chihuahua", "Chow Chow", "Corgi", "Vietnamese Native Dog", "Golden Retriever", "Husky", "Japanese Spitz", "Dachshund", "Pomeranian", "Poodle", "Pug", "Shiba Inu", "Doberman", "Samoyed", "Rottweiler", "Phu Quoc Ridgeback", "Malinois", "Other"]', TRUE),
(41, 'Age Group', 'select', '["Puppy (under 3 months)", "Young (under 1 year)", "Adult (over 1 year)", "Unknown"]', TRUE),
(41, 'Gender', 'select', '["Male", "Female"]', TRUE),
(41, 'Health Condition', 'select', '["Healthy", "Vaccinated", "Needs treatment"]', TRUE);

-- Cats (category_id = 42)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(42, 'Breed', 'select', '["British Longhair", "British Shorthair", "Persian", "Bengal", "Golden", "Maine Coon", "Munchkin", "Tabby", "Sphynx", "Siamese", "Scottish Fold", "Russian Blue", "Toyger", "Somali", "Himalayan", "Other"]', TRUE),
(42, 'Age Group', 'select', '["Kitten (under 3 months)", "Young (under 1 year)", "Adult (over 1 year)", "Unknown"]', TRUE),
(42, 'Gender', 'select', '["Male", "Female"]', TRUE),
(42, 'Health Condition', 'select', '["Healthy", "Vaccinated", "Needs treatment"]', TRUE);

-- Birds (category_id = 43)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(43, 'Species', 'select', '["Oriental Magpie-Robin", "Hwamei", "Budgerigar", "Mynah", "Dove", "Parrot", "Red-whiskered Bulbul", "Other"]', TRUE);

-- Fish (category_id = 44)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(44, 'Species', 'select', '["Koi", "Betta", "Goldfish", "Flowerhorn", "Guppy", "Neon Tetra", "Other"]', TRUE);

-- Hamsters (category_id = 45)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(45, 'Breed', 'select', '["Bear", "Robo", "Winter White", "Campbell", "Other"]', TRUE),
(45, 'Age Group', 'select', '["Under 1 month", "1–3 months", "Over 3 months"]', TRUE),
(45, 'Gender', 'select', '["Male", "Female"]', TRUE),
(45, 'Health Condition', 'select', '["Healthy", "Under treatment", "Unknown"]', TRUE);

-- Turtles (category_id = 46)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(46, 'Species', 'select', '["Red-eared Slider", "Yellow Turtle", "Mountain Turtle", "Rock Turtle", "Other"]', TRUE),
(46, 'Shell Size (cm)', 'number', NULL, FALSE),
(46, 'Estimated Age (years)', 'number', NULL, FALSE),
(46, 'Health Condition', 'select', '["Healthy", "Under treatment", "Unknown"]', TRUE);

-- Software (category_id = 47)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(47, 'Software Type', 'select', '["Operating System", "Office Suite", "Antivirus", "Graphic Design", "Programming", "Other"]', TRUE),
(47, 'License Form', 'select', '["License Key", "Installer File", "Login Account", "Other"]', TRUE),
(47, 'Supported Platforms', 'select', '["Windows", "macOS", "Linux", "Cross-platform"]', TRUE);

-- EBooks (category_id = 48)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(48, 'Genre', 'select', '["Novel", "Business", "Self-help", "Educational", "Comics", "Other"]', TRUE),
(48, 'Format', 'select', '["PDF", "ePub", "MOBI", "Word"]', TRUE),
(48, 'Language', 'text', NULL, FALSE);

-- Courses (category_id = 49)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(49, 'Field of Study', 'select', '["IT - Programming", "Design", "Marketing", "Language", "Soft Skills", "Other"]', TRUE),
(49, 'Learning Mode', 'select', '["Online", "Offline", "Self-learning (Video)", "PDF Materials"]', TRUE),
(49, 'Duration', 'text', NULL, FALSE);

-- Licenses (category_id = 50)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(50, 'License Type', 'select', '["Windows License", "Office License", "Other Software License", "Hosting/Domain", "Other"]', TRUE),
(50, 'Validity Period', 'select', '["1 Month", "6 Months", "1 Year", "Lifetime", "Other"]', TRUE),
(50, 'Delivery Format', 'select', '["Key Code", "Login Account", "Other"]', TRUE);

-- Textbooks (category_id = 51)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(51, 'Education Level', 'select', '["Primary", "Secondary", "High School", "University", "Postgraduate"]', TRUE),
(51, 'Subject', 'text', NULL, FALSE);

-- Novels (category_id = 52)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(52, 'Genre', 'select', '["Romance", "Detective", "Science Fiction", "Horror", "Historical", "Fantasy", "Other"]', TRUE),
(52, 'Language', 'text', NULL, FALSE);

-- Comics (category_id = 53)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(53, 'Comic Type', 'select', '["Manga", "Manhua", "Manhwa", "Vietnamese Comic", "US Comic", "Other"]', TRUE),
(53, 'Series Type', 'select', '["Single Volume", "Complete Series"]', TRUE);

-- Books (category_id = 54)
INSERT INTO category_attribute (category_id, name, input_type, options, is_required) VALUES 
(54, 'Genre', 'select', '["Life Skills", "Business", "Programming", "Marketing", "Medicine", "Education", "Other"]', TRUE),
(54, 'Language', 'text', NULL, FALSE);

-- Following-Follower
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0002', 'CUS0001'), -- John Doe follows John Smitch
('CUS0003', 'CUS0002'), -- Alice Johnson follows John Doe
('CUS0004', 'CUS0003'), -- Bob Williams follows Alice Johnson
('CUS0005', 'CUS0001'), -- Emma Brown follows John Smitch
('CUS0006', 'CUS0005'), -- Liam Harris follows Emma Brown
('CUS0007', 'CUS0006'), -- Sophia Davis follows Liam Harris
('CUS0008', 'CUS0005'), -- Mason Martinez follows Emma Brown
('CUS0009', 'CUS0002'), -- Olivia Anderson follows John Doe
('CUS0010', 'CUS0004'), -- James Wilson follows Bob Williams
('CUS0011', 'CUS0001'), -- Isabella Lee follows John Smitch
('CUS0012', 'CUS0008'), -- Lucas Scott follows Mason Martinez
('CUS0013', 'CUS0012'), -- Charlotte Walker follows Lucas Scott
('CUS0014', 'CUS0006'), -- Henry Adams follows Liam Harris
('CUS0015', 'CUS0011'); -- Amelia Taylor follows Isabella Lee
-- CUS0001 (John Smitch) đã có: follower = 3 (CUS0002, CUS0005, CUS0011), following = 0
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0001', 'CUS0002'), -- John Smitch follows John Doe
('CUS0001', 'CUS0003'), -- John Smitch follows Alice Johnson
('CUS0001', 'CUS0004'); -- John Smitch follows Bob Williams

-- CUS0002 (John Doe) đã có: follower = 2, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0002', 'CUS0004'),
('CUS0002', 'CUS0005');

-- CUS0003 (Alice Johnson) đã có: follower = 1, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0003', 'CUS0001'),
('CUS0003', 'CUS0005'),
('CUS0010', 'CUS0003'); -- James Wilson follows Alice Johnson

-- CUS0004 (Bob Williams) đã có: follower = 1, following = 0
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0004', 'CUS0001'),
('CUS0004', 'CUS0002'),
('CUS0011', 'CUS0004'); -- Isabella follows Bob

-- CUS0005 (Emma Brown) đã có: follower = 2, following = 0
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0005', 'CUS0002'),
('CUS0005', 'CUS0003');

-- CUS0006 (Liam Harris) đã có: follower = 2, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0006', 'CUS0001'),
('CUS0006', 'CUS0002');

-- CUS0007 (Sophia Davis) đã có: follower = 0, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0007', 'CUS0001'),
('CUS0007', 'CUS0002'),
('CUS0007', 'CUS0003');

-- CUS0008 (Mason Martinez) đã có: follower = 1, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0008', 'CUS0001'),
('CUS0008', 'CUS0002');

-- CUS0009 (Olivia Anderson) đã có: follower = 0, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0009', 'CUS0001'),
('CUS0009', 'CUS0005'),
('CUS0009', 'CUS0003');

-- CUS0010 (James Wilson) đã có: follower = 0, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0010', 'CUS0001'),
('CUS0010', 'CUS0002');

-- CUS0011 (Isabella Lee) đã có: follower = 0, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0011', 'CUS0002'),
('CUS0011', 'CUS0003');

-- CUS0012 (Lucas Scott) đã có: follower = 1, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0012', 'CUS0001'),
('CUS0012', 'CUS0002');

-- CUS0013 (Charlotte Walker) đã có: follower = 0, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0013', 'CUS0001'),
('CUS0013', 'CUS0002'),
('CUS0013', 'CUS0003');

-- CUS0014 (Henry Adams) đã có: follower = 0, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0014', 'CUS0001'),
('CUS0014', 'CUS0002'),
('CUS0014', 'CUS0003');

-- CUS0015 (Amelia Taylor) đã có: follower = 0, following = 1
INSERT INTO following_follower (follower_id, following_id) VALUES
('CUS0015', 'CUS0001'),
('CUS0015', 'CUS0002'),
('CUS0015', 'CUS0003');
