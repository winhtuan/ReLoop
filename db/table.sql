-- Tạo database và chọn
CREATE DATABASE reloop_v2;
USE reloop_v2;
-- 1. users
CREATE TABLE users (
    user_id CHAR(7) NOT NULL PRIMARY KEY CHECK (user_id LIKE 'CUS____'),
    FullName VARCHAR(255),
    role ENUM('admin', 'user', 'shopkeeper', 'supporter'),
    Address TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    PhoneNumber VARCHAR(50) UNIQUE,
    email VARCHAR(255) UNIQUE,
    is_premium TINYINT NOT NULL DEFAULT 0,
    premium_expiry DATETIME,
    balance DECIMAL(18, 2) DEFAULT 0,
    img TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
);

-- 2. shipping_methods
CREATE TABLE shipping_methods (
    id INT NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    cost DECIMAL(10, 2) CHECK (cost >= 0),
    estimated_days INT CHECK (estimated_days >= 0),
    is_active TINYINT NOT NULL DEFAULT 1
);

-- 3. categories
CREATE TABLE categories (
    category_id INT NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    parent_id INT,
    slug VARCHAR(255) UNIQUE,
    level INT DEFAULT 0,
    CONSTRAINT FK_categories_parent FOREIGN KEY (parent_id) REFERENCES categories(category_id) ON DELETE NO ACTION
);
SELECT * FROM categories;
-- 4. vouchers
CREATE TABLE vouchers (
    voucher_id CHAR(7) NOT NULL PRIMARY KEY CHECK (voucher_id LIKE 'VOU____'),
    code VARCHAR(255),
    description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    discount_value DECIMAL(10, 2) CHECK (discount_value >= 0),
    min_order_amount DECIMAL(10, 2),
    start_date DATETIME,
    end_date DATETIME,
    usage_limit INT CHECK (usage_limit >= 0),
    used_count INT DEFAULT 0,
    is_active TINYINT NOT NULL DEFAULT 1
);

-- 5. PaidService
CREATE TABLE PaidService (
    paid_id CHAR(7) NOT NULL PRIMARY KEY CHECK (paid_id LIKE 'PRO____'),
    service_name varchar(50) NOT NULL,
    price DECIMAL(18, 2) NOT NULL CHECK (price > 0),
    description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    usage_time INT NOT NULL
);

-- 6. Account
CREATE TABLE Account (
    acc_id CHAR(7) NOT NULL PRIMARY KEY CHECK (acc_id LIKE 'ACC____'),
    password VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    regisDate DATETIME,
    user_id CHAR(7) NOT NULL,
    verification_token VARCHAR(255),
    is_verified TINYINT NOT NULL DEFAULT 0,
    is_block TINYINT NOT NULL DEFAULT 0,
    offline_at DATETIME,
    CONSTRAINT FK_Account_User FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 7. PasswordResetToken
CREATE TABLE PasswordResetToken (
    reset_id CHAR(7) NOT NULL PRIMARY KEY CHECK (reset_id LIKE 'RST____'),
    user_id CHAR(7) NOT NULL,
    email VARCHAR(255),
    token VARCHAR(255),
    expiryDate DATETIME,
    CONSTRAINT FK_PasswordResetToken_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 8. BlockUser_Conversation
CREATE TABLE BlockUser_Conversation (
    BlockerUserID CHAR(7) NOT NULL,
    BlockedUser_ID CHAR(7) NOT NULL,
    PRIMARY KEY (BlockerUserID, BlockedUser_ID),
    CONSTRAINT FK_Blocker FOREIGN KEY (BlockerUserID) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT FK_Blocked FOREIGN KEY (BlockedUser_ID) REFERENCES users(user_id)
);

-- 9. cart
CREATE TABLE cart (
    cart_id CHAR(7) NOT NULL PRIMARY KEY CHECK (cart_id LIKE 'CRT____'),
    user_id CHAR(7) NOT NULL,
    CONSTRAINT FK_cart_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 10. transaction_logs
CREATE TABLE transaction_logs (
    transaction_id CHAR(7) NOT NULL PRIMARY KEY CHECK (transaction_id LIKE 'TRA____'),
    user_id CHAR(7) NOT NULL,
    type ENUM('deposit', 'withdraw', 'refund', 'payment', 'adjustment') NOT NULL,
    amount DECIMAL(18, 2) NOT NULL CHECK (amount > 0),
    balance_before DECIMAL(18, 2) NOT NULL,
    balance_after DECIMAL(18, 2) NOT NULL,
    reference_id VARCHAR(20),
    description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'success', 'failed') DEFAULT 'success',
    CONSTRAINT FK_transaction_logs_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_transaction_logs_user_id ON transaction_logs(user_id);

-- 11. product
CREATE TABLE product (
    product_id CHAR(7) NOT NULL PRIMARY KEY CHECK (product_id LIKE 'PRD____'),
    user_id CHAR(7) NOT NULL,
    category_id INT,
    title VARCHAR(255),
    description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    price DECIMAL(10, 2) CHECK (price >= 0),
    location VARCHAR(255),
    status ENUM('active', 'sold', 'expired'),
    moderation_status ENUM('pending', 'approved', 'rejected', 'blocked') NOT NULL DEFAULT 'pending',
    is_priority TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME,
    CONSTRAINT FK_product_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT FK_product_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL
);

-- 12. orders
CREATE TABLE orders (
    order_id CHAR(7) NOT NULL PRIMARY KEY CHECK (order_id LIKE 'ORD____'),
    user_id CHAR(7) NOT NULL,
    total_amount DECIMAL(18, 2) CHECK (total_amount >= 0),
    status ENUM('pending', 'paid', 'shipped', 'delivered', 'cancelled', 'refunded'),
    shipping_address TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    shipping_method INT,
    voucher_id CHAR(7),
    discount_amount DECIMAL(10, 2) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_orders_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT FK_orders_shipping_method FOREIGN KEY (shipping_method) REFERENCES shipping_methods(id) ON DELETE SET NULL,
    CONSTRAINT FK_orders_voucher FOREIGN KEY (voucher_id) REFERENCES vouchers(voucher_id) ON DELETE SET NULL
);

CREATE INDEX idx_orders_user_id ON orders(user_id);

-- 13. conversation
CREATE TABLE conversation (
    conversation_id CHAR(7) NOT NULL PRIMARY KEY CHECK (conversation_id LIKE 'CON____'),
    sender_id CHAR(7) NOT NULL,
    receiver_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    CONSTRAINT FK_sender FOREIGN KEY (sender_id) REFERENCES users(user_id),
    CONSTRAINT FK_receiver FOREIGN KEY (receiver_id) REFERENCES users(user_id),
    CONSTRAINT FK_product_conversation FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 14. product_reports
CREATE TABLE product_reports (
    report_id CHAR(7) NOT NULL PRIMARY KEY CHECK (report_id LIKE 'REP____'),
    product_id CHAR(7) NOT NULL,
    reporter_id CHAR(7) NOT NULL,
    reason VARCHAR(255),
    description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    reported_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'reviewed', 'rejected', 'action_taken'),
    CONSTRAINT FK_product_reports_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    CONSTRAINT FK_product_reports_reporter FOREIGN KEY (reporter_id) REFERENCES users(user_id)
);

-- 15. product_images
CREATE TABLE product_images (
    img_id INT NOT NULL,
    product_id CHAR(7) NOT NULL,
    image_url VARCHAR(255),
    is_primary TINYINT NOT NULL DEFAULT 0,
    CONSTRAINT PK_product_images PRIMARY KEY (img_id, product_id),
    CONSTRAINT FK_product_images_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

-- 16. favorites
CREATE TABLE favorites (
    fav_id CHAR(7) NOT NULL PRIMARY KEY CHECK (fav_id LIKE 'FAV____'),
    user_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    favorited_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_favorites_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_favorites_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

CREATE INDEX idx_favorites_user_id ON favorites(user_id);

-- 17. cart_items
CREATE TABLE cart_items (
    cart_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    quantity INT CHECK (quantity > 0),
    PRIMARY KEY (cart_id, product_id),
    CONSTRAINT FK_cart_items_cart FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE,
    CONSTRAINT FK_cart_items_product FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);

-- 18. order_items
CREATE TABLE order_items (
    order_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    quantity INT CHECK (quantity > 0),
    price DECIMAL(10, 2) CHECK (price >= 0),
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT FK_order_items_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT FK_order_items_product FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 19. payments
CREATE TABLE payments (
    pay_id CHAR(7) NOT NULL PRIMARY KEY CHECK (pay_id LIKE 'PAY____'),
    order_id CHAR(7) NOT NULL,
    amount DECIMAL(10, 2) CHECK (amount >= 0),
    status ENUM('pending', 'paid', 'failed', 'refunded'),
    paid_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_payments_user FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- 20. Messages
CREATE TABLE Messages (
    message_id CHAR(7) NOT NULL PRIMARY KEY CHECK (message_id LIKE 'MSG____'),
    conversation_id CHAR(7) NOT NULL,
    sender_id CHAR(7) NOT NULL,
    content TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    isRead TINYINT NOT NULL DEFAULT 0,
    type ENUM('img', 'text'),
    SentAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_recall TINYINT NOT NULL DEFAULT 0,
    CONSTRAINT FK_Messages_conversation FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id) ON DELETE CASCADE,
    CONSTRAINT FK_Messages_sender FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE
);

alter table product add column state varchar(50) NOT NULL;

UPDATE product SET state = 'mới' WHERE product_id IN ('PRD0004', 'PRD0010', 'PRD0011', 'PRD0012', 'PRD0031', 'PRD0033', 'PRD0035', 'PRD0049', 'PRD0053', 'PRD0055', 'PRD0057', 'PRD0059', 'PRD0063', 'PRD0065', 'PRD0082', 'PRD0083', 'PRD0084', 'PRD0089');
UPDATE product SET state = 'cũ' WHERE product_id IN ('PRD0001', 'PRD0002', 'PRD0003', 'PRD0006', 'PRD0007', 'PRD0008', 'PRD0009', 'PRD0013', 'PRD0014', 'PRD0017', 'PRD0018', 'PRD0020', 'PRD0021', 'PRD0023', 'PRD0024', 'PRD0025', 'PRD0026', 'PRD0027', 'PRD0028', 'PRD0029', 'PRD0030', 'PRD0032', 'PRD0034', 'PRD0036', 'PRD0037', 'PRD0038', 'PRD0039', 'PRD0040', 'PRD0041', 'PRD0042', 'PRD0043', 'PRD0044', 'PRD0045', 'PRD0046', 'PRD0047', 'PRD0048', 'PRD0050', 'PRD0051', 'PRD0052', 'PRD0054', 'PRD0056', 'PRD0058', 'PRD0060', 'PRD0061', 'PRD0062', 'PRD0064', 'PRD0066', 'PRD0067', 'PRD0068', 'PRD0069', 'PRD0070', 'PRD0071', 'PRD0072', 'PRD0073', 'PRD0074', 'PRD0075', 'PRD0076', 'PRD0077', 'PRD0078', 'PRD0079', 'PRD0080', 'PRD0081', 'PRD0085', 'PRD0086', 'PRD0087', 'PRD0088', 'PRD0090', 'PRD0091', 'PRD0092', 'PRD0093');
UPDATE product SET state = 'hư hỏng nhẹ' WHERE product_id IN ('PRD0005', 'PRD0015', 'PRD0016', 'PRD0019', 'PRD0022');
create table notification(
	noti_id CHAR(7) NOT NULL PRIMARY KEY CHECK (noti_id LIKE 'NOT____'),
    user_id CHAR(7) NOT NULL,
    content text,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    CONSTRAINT FK_notic_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
select * from product;
select * from product_images;
select * from categories;
UPDATE product
SET moderation_status = 'approved'
WHERE moderation_status = 'pending';

UPDATE categories SET slug = REPLACE(slug, '&', '-') WHERE slug LIKE '%&%';

CREATE TABLE category_attribute (
    attr_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(100) NOT NULL, -- Tên thuộc tính (VD: Hãng, Kích thước)
    input_type ENUM('text', 'number', 'select') NOT NULL, -- Kiểu nhập liệu
    options TEXT, -- JSON chứa các lựa chọn nếu là select
    is_required BOOLEAN DEFAULT FALSE,
    CONSTRAINT FK_attr_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

CREATE TABLE product_attribute_value (
    product_id CHAR(7) NOT NULL,
    attr_id INT NOT NULL,
    value TEXT,
    PRIMARY KEY (product_id, attr_id),
    CONSTRAINT FK_attr_value_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    CONSTRAINT FK_attr_value_attr FOREIGN KEY (attr_id) REFERENCES category_attribute(attr_id) ON DELETE CASCADE
);

CREATE TABLE category_state_options (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    options TEXT NOT NULL, -- lưu JSON ["mới", "cũ", "hư hỏng nhẹ"]
    CONSTRAINT FK_cat_state FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

CREATE TABLE product_sequence (
    id INT PRIMARY KEY,
    last_number INT
);

CREATE TABLE product_images_sequence (
    id INT PRIMARY KEY,
    last_number INT
);

-- Khởi tạo với số 0
INSERT INTO product_sequence (id, last_number) VALUES (1, 93);
INSERT INTO product_images_sequence (id, last_number) VALUES (1, 93);

-- Gán 10 sản phẩm đầu tiên là "con non"
UPDATE product
SET state = 'con non'
WHERE product_id IN (
  'PRD0001', 'PRD0002', 'PRD0003', 'PRD0006', 'PRD0007',
  'PRD0008', 'PRD0009', 'PRD0012', 'PRD0013', 'PRD0014', 'PRD0017'
);

-- Gán 10 sản phẩm còn lại là "trưởng thành"
UPDATE product
SET state = 'trưởng thành'
WHERE product_id IN (
  'PRD0018', 'PRD0020', 'PRD0021', 'PRD0005', 'PRD0015',
  'PRD0016', 'PRD0019', 'PRD0004', 'PRD0010', 'PRD0011'
);
