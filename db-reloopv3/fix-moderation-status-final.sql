-- Script to fix moderation_status column issues
-- Run this script on both local and production databases

USE reloop_v3;

-- Check current table structure
DESCRIBE product;

-- Check current moderation_status values
SELECT DISTINCT moderation_status FROM product;

-- Drop and recreate the column to ensure proper ENUM definition
ALTER TABLE product 
MODIFY COLUMN moderation_status ENUM('pending', 'approved', 'rejected', 'blocked', 'warn') NOT NULL DEFAULT 'pending';

-- Update any invalid values to 'pending'
UPDATE product 
SET moderation_status = 'pending' 
WHERE moderation_status NOT IN ('pending', 'approved', 'rejected', 'blocked', 'warn') 
   OR moderation_status IS NULL;

-- Verify the fix
DESCRIBE product;
SELECT DISTINCT moderation_status FROM product;

-- Test insert to verify the fix works
-- (This is just a test - you can remove this after verification)
-- INSERT INTO product (product_id, user_id, category_id, title, description, price, location, state, status, moderation_status, is_priority) 
-- VALUES ('PRDTEST', 'CUS0001', 1, 'Test Product', 'Test Description', 1000, 'Test Location', 'mới', 'active', 'warn', 0);

-- Clean up test data if needed
-- DELETE FROM product WHERE product_id = 'PRDTEST'; 