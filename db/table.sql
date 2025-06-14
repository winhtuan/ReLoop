use reloop;
-- 1. Bảng users
CREATE TABLE users (
    user_id CHAR(7) NOT NULL PRIMARY KEY CHECK (user_id LIKE 'US______'),
    FullName VARCHAR(255),
    role ENUM('admin', 'user', 'shopkeeper', 'supporter'),
    Address TEXT,
    PhoneNumber VARCHAR(50),
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

-- 3. Bảng vouchers
CREATE TABLE vouchers (
    voucher_id CHAR(7) NOT NULL PRIMARY KEY CHECK (voucher_id LIKE 'VOU___'),
    code VARCHAR(255),
    description TEXT,
    discount_type ENUM('percent', 'fixed'),
    discount_value DECIMAL(10, 2) CHECK (discount_value >= 0),
    max_discount DECIMAL(10, 2),
    min_order_amount DECIMAL(10, 2),
    start_date DATETIME,
    end_date DATETIME,
    usage_limit INT CHECK (usage_limit >= 0),
    used_count INT DEFAULT 0,
    is_active TINYINT NOT NULL DEFAULT 1
);

-- 4. Bảng categories
CREATE TABLE categories (
    category_id INT NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    parent_id INT,
    CONSTRAINT FK_categories_parent FOREIGN KEY (parent_id) REFERENCES categories(category_id) ON DELETE NO ACTION
);

-- 5. Bảng product
CREATE TABLE product (
    product_id CHAR(7) NOT NULL PRIMARY KEY CHECK (product_id LIKE 'PROD___'),
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

-- 6. Bảng Account
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

-- 7. Bảng PasswordResetToken
CREATE TABLE PasswordResetToken (
    reset_id CHAR(7) NOT NULL PRIMARY KEY CHECK (reset_id LIKE 'RS_____'),
    user_id CHAR(7) NOT NULL,
    email VARCHAR(255),
    token VARCHAR(255),
    expiryDate DATETIME,
    CONSTRAINT FK_PasswordResetToken_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 8. Bảng conversation
CREATE TABLE conversation (
    conversation_id CHAR(7) NOT NULL PRIMARY KEY CHECK (conversation_id LIKE 'CONV___'),
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

-- 11. Bảng payments
CREATE TABLE payments (
    pay_id CHAR(7) NOT NULL PRIMARY KEY CHECK (pay_id LIKE 'PAY____'),
    user_id CHAR(7) NOT NULL,
    amount DECIMAL(10, 2) CHECK (amount >= 0),
    status ENUM('pending', 'paid', 'failed', 'refunded'),
    paid_at DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT FK_payments_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 12. Bảng product_reports
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

-- 13. Bảng product_images
CREATE TABLE product_images (
    img_id INT NOT NULL,
    product_id CHAR(7) NOT NULL,
    image_url VARCHAR(255),
    is_primary TINYINT NOT NULL DEFAULT 0,
    CONSTRAINT PK_product_images PRIMARY KEY (img_id, product_id),
    CONSTRAINT FK_product_images_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

-- 14. Bảng favorites
CREATE TABLE favorites (
    fav_id CHAR(7) NOT NULL PRIMARY KEY CHECK (fav_id LIKE 'FAV____'),
    user_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    favorited_at DATETIME,
    CONSTRAINT FK_favorites_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_favorites_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

-- 15. Bảng shop_reviews
CREATE TABLE shop_reviews (
    review_id CHAR(7) NOT NULL PRIMARY KEY CHECK (review_id LIKE 'REV____'),
    reviewer_id CHAR(7) NOT NULL,
    seller_id CHAR(7) NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    review_date DATETIME,
    CONSTRAINT FK_shop_reviews_reviewer FOREIGN KEY (reviewer_id) REFERENCES users(user_id),
    CONSTRAINT FK_shop_reviews_seller FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 16. Bảng cart
CREATE TABLE cart (
    cart_id CHAR(7) NOT NULL PRIMARY KEY CHECK (cart_id LIKE 'CART___'),
    user_id CHAR(7) NOT NULL,
    CONSTRAINT FK_cart_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 17. Bảng cart_items
CREATE TABLE cart_items (
    cart_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    quantity INT CHECK (quantity > 0),
    PRIMARY KEY (cart_id, product_id),
    CONSTRAINT FK_cart_items_cart FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE,
    CONSTRAINT FK_cart_items_product FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 18. Bảng orders
CREATE TABLE orders (
    order_id CHAR(7) NOT NULL PRIMARY KEY CHECK (order_id LIKE 'ORD____'),
    user_id CHAR(7) NOT NULL,
    service_name varchar(50) not null,
    total_amount DECIMAL(10, 2) CHECK (total_amount >= 0),
    status ENUM('pending', 'paid', 'shipped', 'delivered', 'cancelled', 'refunded'),
    shipping_address TEXT,
    shipping_method INT,
    voucher_id CHAR(7),
    discount_amount DECIMAL(10, 2) DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT FK_orders_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT FK_orders_shipping_method FOREIGN KEY (shipping_method) REFERENCES shipping_methods(id) ON DELETE SET NULL,
    CONSTRAINT FK_orders_voucher FOREIGN KEY (voucher_id) REFERENCES vouchers(voucher_id) ON DELETE SET NULL
);
ALTER TABLE orders
MODIFY COLUMN total_amount DECIMAL(18, 2) CHECK (total_amount >= 0);

-- 19. Bảng order_items
CREATE TABLE order_items (
    order_id CHAR(7) NOT NULL,
    product_id CHAR(7) NOT NULL,
    quantity INT CHECK (quantity > 0),
    price DECIMAL(10, 2) CHECK (price >= 0),
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT FK_order_items_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT FK_order_items_product FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 20. Bảng premium_subscriptionsaccountaccount
CREATE TABLE paid_service (
	paid_id CHAR(7) NOT NULL PRIMARY KEY CHECK (paid_id LIKE 'PRO____'),
    service_name VARCHAR(50) NOT NULL,
    price DECIMAL(10,2),
    start_date DATETIME, 
    usage_time INT,
    end_date DATETIME,
    CONSTRAINT CHK_paid_id CHECK (paid_id LIKE 'PRO____')
);
-- Tạo Trigger tự động tính end_date dựa trên start_date + usage_time
DELIMITER $$

CREATE TRIGGER update_end_date
BEFORE INSERT ON paid_service
FOR EACH ROW
BEGIN
    -- Nếu usage_time không null và start_date không null thì tự động tính end_date
    IF NEW.usage_time IS NOT NULL AND NEW.start_date IS NOT NULL THEN
        SET NEW.end_date = DATE_ADD(NEW.start_date, INTERVAL NEW.usage_time DAY);
    END IF;
END$$

DELIMITER ;

