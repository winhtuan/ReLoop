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

CREATE TRIGGER set_premium_expiry_on_insert
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    IF NEW.is_premium = 1 AND NEW.usage_time > 0 THEN  -- Sử dụng usage_time thay vì use_time
        SET NEW.premium_expiry = DATE_ADD(NOW(), INTERVAL NEW.usage_time DAY);
    END IF;
END$$

CREATE TRIGGER set_premium_expiry_on_update
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
    IF NEW.is_premium = 1 AND NEW.usage_time > 0 AND (NEW.premium_expiry IS NULL OR NEW.premium_expiry <= NOW()) THEN
        SET NEW.premium_expiry = DATE_ADD(NOW(), INTERVAL NEW.usage_time DAY);
    END IF;
END$$

CREATE TRIGGER update_is_premium
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
    IF NEW.premium_expiry IS NOT NULL AND NEW.premium_expiry > NOW() AND NEW.usage_time > 0 THEN  -- Sử dụng usage_time thay vì use_time
        SET NEW.is_premium = 1;
    ELSE
        SET NEW.is_premium = 0;
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
