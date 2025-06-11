-- Tạo database và chọn
CREATE DATABASE reloop_v2;
USE reloop_v2;

-- 1. Bảng users (bảng trung tâm)
CREATE TABLE users (
    user_id CHAR(7) NOT NULL PRIMARY KEY CHECK (user_id LIKE 'CUS____'),
    FullName VARCHAR(255),
    role ENUM('admin', 'user', 'shopkeeper', 'supporter'),
    Address TEXT,
    PhoneNumber VARCHAR(50) UNIQUE,
    email VARCHAR(255) UNIQUE,
    is_premium TINYINT NOT NULL DEFAULT 0,
    premium_expiry DATETIME,
    balance DECIMAL(18, 2) DEFAULT 0
);

-- 2. Bảng shipping_methods
CREATE TABLE shipping_methods (
    id INT NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    cost DECIMAL(10, 2) CHECK (cost >= 0),
    estimated_days INT CHECK (estimated_days >= 0),
    is_active TINYINT NOT NULL DEFAULT 1
);

-- 3. Bảng categories
CREATE TABLE categories (
    category_id INT NOT NULL,
    name VARCHAR(255),
    slug VARCHAR(255) UNIQUE,
    level INT DEFAULT 0,
    PRIMARY KEY (category_id, name)
);
-- 4. Bảng product
CREATE TABLE product (
    product_id CHAR(7) NOT NULL PRIMARY KEY CHECK (product_id LIKE 'PRD____'),
    user_id CHAR(7) NOT NULL,
    category_id INT,
    title VARCHAR(255),
    description TEXT,
    price DECIMAL(10, 2) CHECK (price >= 0),
    location VARCHAR(255),
    status ENUM('active', 'sold', 'expired'),
    is_priority TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT FK_product_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT FK_product_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL
);

-- 5. Bảng Account
CREATE TABLE Account (
    acc_id CHAR(7) NOT NULL PRIMARY KEY CHECK (acc_id LIKE 'ACC____'),
    password VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    regisDate DATETIME,
    user_id CHAR(7) NOT NULL,
    verification_token VARCHAR(255),
    is_verified TINYINT NOT NULL DEFAULT 0,
    CONSTRAINT FK_Account_User FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 6. Bảng PasswordResetToken
CREATE TABLE PasswordResetToken (
    reset_id CHAR(7) NOT NULL PRIMARY KEY CHECK (reset_id LIKE 'RST____'),
    user_id CHAR(7) NOT NULL,
    email VARCHAR(255),
    token VARCHAR(255),
    expiryDate DATETIME,
    CONSTRAINT FK_PasswordResetToken_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 7. Bảng vouchers
CREATE TABLE vouchers (
    voucher_id CHAR(7) NOT NULL PRIMARY KEY CHECK (voucher_id LIKE 'VOU____'),
    code VARCHAR(255),
    description TEXT,
    discount_value DECIMAL(10, 2) CHECK (discount_value >= 0),
    min_order_amount DECIMAL(10, 2),
    start_date DATETIME,
    end_date DATETIME,
    usage_limit INT CHECK (usage_limit >= 0),
    used_count INT DEFAULT 0,
    is_active TINYINT NOT NULL DEFAULT 1
);

-- 8. Bảng conversation
CREATE TABLE conversation (
    conversation_id CHAR(7) NOT NULL PRIMARY KEY CHECK (conversation_id LIKE 'CON____'),
    sender_id CHAR(7) NOT NULL,
    receiver_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    CONSTRAINT FK_sender FOREIGN KEY (sender_id) REFERENCES users(user_id),
    CONSTRAINT FK_receiver FOREIGN KEY (receiver_id) REFERENCES users(user_id),
    CONSTRAINT FK_product_conversation FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 9. Bảng Messages
CREATE TABLE Messages (
    message_id CHAR(7) NOT NULL PRIMARY KEY CHECK (message_id LIKE 'MSG____'),
    conversation_id CHAR(7) NOT NULL,
    sender_id CHAR(7) NOT NULL,
    content TEXT,
    isRead TINYINT NOT NULL DEFAULT 0,
    type ENUM('img', 'text'),
    SentAt TIMESTAMP,
    is_recall TINYINT NOT NULL DEFAULT 0,
    CONSTRAINT FK_Messages_conversation FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id) ON DELETE CASCADE,
    CONSTRAINT FK_Messages_sender FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 10. Bảng BlockUser_Conversation
CREATE TABLE BlockUser_Conversation (
    BlockerUserID CHAR(7) NOT NULL,
    BlockedUser_ID CHAR(7) NOT NULL,
    PRIMARY KEY (BlockerUserID, BlockedUser_ID),
    CONSTRAINT FK_Blocker FOREIGN KEY (BlockerUserID) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT FK_Blocked FOREIGN KEY (BlockedUser_ID) REFERENCES users(user_id)
);

-- 11. Bảng product_reports
CREATE TABLE product_reports (
    report_id CHAR(7) NOT NULL PRIMARY KEY CHECK (report_id LIKE 'REP____'),
    product_id CHAR(7) NOT NULL,
    reporter_id CHAR(7) NOT NULL,
    reason VARCHAR(255),
    description TEXT,
    reported_at DATETIME,
    status ENUM('pending', 'reviewed', 'rejected', 'action_taken'),
    CONSTRAINT FK_product_reports_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    CONSTRAINT FK_product_reports_reporter FOREIGN KEY (reporter_id) REFERENCES users(user_id)
);

-- 12. Bảng product_images
CREATE TABLE product_images (
    img_id INT NOT NULL,
    product_id CHAR(7) NOT NULL,
    image_url VARCHAR(255),
    is_primary TINYINT NOT NULL DEFAULT 0,
    CONSTRAINT PK_product_images PRIMARY KEY (img_id, product_id),
    CONSTRAINT FK_product_images_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

-- 13. Bảng favorites
CREATE TABLE favorites (
    fav_id CHAR(7) NOT NULL PRIMARY KEY CHECK (fav_id LIKE 'FAV____'),
    user_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    favorited_at DATETIME,
    CONSTRAINT FK_favorites_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_favorites_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

CREATE INDEX idx_favorites_user_id ON favorites(user_id);

-- 14. Bảng cart
CREATE TABLE cart (
    cart_id CHAR(7) NOT NULL PRIMARY KEY CHECK (cart_id LIKE 'CRT____'),
    user_id CHAR(7) NOT NULL,
    CONSTRAINT FK_cart_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 15. Bảng cart_items
CREATE TABLE cart_items (
    cart_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    quantity INT CHECK (quantity > 0),
    PRIMARY KEY (cart_id, product_id),
    CONSTRAINT FK_cart_items_cart FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE,
    CONSTRAINT FK_cart_items_product FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);

-- 16. Bảng orders
CREATE TABLE orders (
    order_id CHAR(7) NOT NULL PRIMARY KEY CHECK (order_id LIKE 'ORD____'),
    user_id CHAR(7) NOT NULL,
    service_name VARCHAR(50) NOT NULL,
    total_amount DECIMAL(18, 2) CHECK (total_amount >= 0),
    status ENUM('pending', 'paid', 'shipped', 'delivered', 'cancelled', 'refunded'),
    shipping_address TEXT,
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

-- 17. Bảng order_items
CREATE TABLE order_items (
    order_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    quantity INT CHECK (quantity > 0),
    price DECIMAL(10, 2) CHECK (price >= 0),
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT FK_order_items_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT FK_order_items_product FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 18. Bảng payments
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

-- 19. Bảng transaction_logs
CREATE TABLE transaction_logs (
    transaction_id CHAR(7) NOT NULL PRIMARY KEY CHECK (transaction_id LIKE 'TRA____'),
    user_id CHAR(7) NOT NULL,
    type ENUM('deposit', 'withdraw', 'refund', 'payment', 'adjustment') NOT NULL,
    amount DECIMAL(18, 2) NOT NULL CHECK (amount > 0),
    balance_before DECIMAL(18, 2) NOT NULL,
    balance_after DECIMAL(18, 2) NOT NULL,
    reference_id VARCHAR(20),
    description TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'success', 'failed') DEFAULT 'success',
    CONSTRAINT FK_transaction_logs_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_transaction_logs_user_id ON transaction_logs(user_id);
