-- TRIGGERS
DELIMITER $$

CREATE TRIGGER deactivate_voucher
AFTER UPDATE ON vouchers
FOR EACH ROW
BEGIN
    IF NEW.usage_limit > 0 AND NEW.used_count >= NEW.usage_limit THEN
        UPDATE vouchers SET is_active = 0 WHERE voucher_id = NEW.voucher_id;
    END IF;
END$$
-- post
CREATE TRIGGER set_priority_on_insert
BEFORE INSERT ON product
FOR EACH ROW
BEGIN
    IF NEW.price >= 1000 THEN
        SET NEW.is_priority = 1;
    ELSE
        SET NEW.is_priority = 0;
    END IF;
END$$

CREATE TRIGGER set_priority_on_update
BEFORE UPDATE ON product
FOR EACH ROW
BEGIN
    IF NEW.price >= 1000 THEN
        SET NEW.is_priority = 1;
    ELSE
        SET NEW.is_priority = 0;
    END IF;
END$$

DELIMITER ;
DELIMITER //
-- hàm chuyển từ name sang slug
CREATE FUNCTION generate_slug(input VARCHAR(255)) RETURNS VARCHAR(255)
DETERMINISTIC
BEGIN
    DECLARE slug VARCHAR(255);

    SET slug = LOWER(input);
    SET slug = REPLACE(slug, ' ', '-');
    SET slug = REPLACE(slug, 'đ', 'd');
    SET slug = REPLACE(slug, '.', '');
    SET slug = REPLACE(slug, ',', '');
    SET slug = REPLACE(slug, '''', '');
    SET slug = REPLACE(slug, '"', '');

    RETURN slug;
END //

DELIMITER ;

DELIMITER //
-- cập nhật slug và level khi thêm mới
CREATE TRIGGER trg_before_insert_categories
BEFORE INSERT ON categories
FOR EACH ROW
BEGIN
    -- Khai báo biến phải đặt trước
    DECLARE parent_level INT DEFAULT 0;

    -- Gán slug
    SET NEW.slug = generate_slug(NEW.name);

    -- Gán level
    IF NEW.parent_id IS NOT NULL THEN
        SELECT level INTO parent_level FROM categories WHERE category_id = NEW.parent_id;
        SET NEW.level = parent_level + 1;
    ELSE
        SET NEW.level = 0;
    END IF;
END //

DELIMITER ;

-- Cập nhật slug trước
UPDATE categories
SET slug = generate_slug(name)
WHERE category_id IS NOT NULL;

-- Cập nhật level thủ công từ root → con
-- Cấp 0: không có parent
UPDATE categories
SET level = 0
WHERE parent_id IS NULL;

-- Cấp 1
UPDATE categories c
JOIN categories p ON c.parent_id = p.category_id
SET c.level = p.level + 1;

DELIMITER $$

CREATE TRIGGER before_insert_product
BEFORE INSERT ON product
FOR EACH ROW
BEGIN
    DECLARE new_id INT;
    DECLARE new_code CHAR(7);

    -- Tăng số thứ tự
    UPDATE product_sequence SET last_number = last_number + 1 WHERE id = 1;

    -- Lấy số mới
    SELECT last_number INTO new_id FROM product_sequence WHERE id = 1;

    -- Tạo mã mới: PRD + số padded
    SET new_code = CONCAT('PRD', LPAD(new_id, 4, '0'));

    -- Gán vào trường product_id
    SET NEW.product_id = new_code;
END$$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER before_insert_product_images
BEFORE INSERT ON product_images
FOR EACH ROW
BEGIN
    DECLARE new_img_id INT;

    -- Tăng số thứ tự
    UPDATE product_images_sequence SET last_number = last_number + 1 WHERE id = 1;

    -- Lấy số mới
    SELECT last_number INTO new_img_id FROM product_images_sequence WHERE id = 1;

    -- Gán vào trường img_id
    SET NEW.img_id = new_img_id;
END$$

DELIMITER ;

DELIMITER //

CREATE TRIGGER trg_set_default_quantity
BEFORE INSERT ON product
FOR EACH ROW
BEGIN
    IF NEW.quantity IS NULL THEN
        SET NEW.quantity = 1;
    END IF;
END;
//

DELIMITER ;

UPDATE product
SET quantity = 1
WHERE quantity IS NULL;

DELIMITER //

CREATE TRIGGER trg_set_default_status
BEFORE INSERT ON product
FOR EACH ROW
BEGIN
    IF NEW.status IS NULL THEN
        SET NEW.status = 'active';
    END IF;
END;
//

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE GiveDailyVoucherToPremiumUsers()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE uid CHAR(7);
    DECLARE voucher_id CHAR(7);
    DECLARE voucherCode VARCHAR(255);

    -- Con trỏ để duyệt các user premium
    DECLARE premium_users CURSOR FOR
        SELECT user_id FROM users WHERE is_premium = 1;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN premium_users;

    loop_users: LOOP
        FETCH premium_users INTO uid;

        IF done THEN
            LEAVE loop_users;
        END IF;

        -- Kiểm tra xem user đã có voucher hôm nay chưa
        IF NOT EXISTS (
            SELECT 1 FROM user_vouchers 
            WHERE user_id = uid AND DATE(assigned_at) = CURDATE()
        ) THEN
            -- Tạo mã voucher và id
            SET voucher_id = CONCAT('VOU', LPAD(FLOOR(RAND() * 1000000), 4, '0'));
            SET voucherCode = CONCAT('VOUCHER-', uid, '-', DATE_FORMAT(NOW(), '%Y%m%d'));

            -- Thêm vào bảng vouchers
            INSERT INTO vouchers (
                voucher_id, code, description, discount_value,
                start_date, end_date, usage_limit, is_active
            ) VALUES (
                voucher_id, voucherCode, 'Daily voucher for premium user', 10000,
                NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 1, 1
            );

            -- Gán voucher cho user
            INSERT INTO user_vouchers (
                user_voucher_id, user_id, voucher_id, assigned_at, is_used
            ) VALUES (
                CONCAT('UVU', LPAD(FLOOR(RAND() * 1000000), 4, '0')),
                uid,
                voucher_id,
                NOW(),
                0
            );
        END IF;
    END LOOP;

    CLOSE premium_users;
END$$

DELIMITER ;
-- Tạo sự kiện để gọi stored procedure mỗi ngày lúc 00:00
CREATE EVENT give_daily_voucher_event
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_DATE + INTERVAL 1 DAY
DO
    CALL GiveDailyVoucherToPremiumUsers();