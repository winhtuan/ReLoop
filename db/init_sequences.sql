-- Initialize sequence tables with initial data
-- This script should be run after creating the tables to ensure sequences work properly

-- Initialize product_sequence
INSERT INTO product_sequence (id, last_number) VALUES (1, 0)
ON DUPLICATE KEY UPDATE last_number = last_number;

-- Initialize product_images_sequence  
INSERT INTO product_images_sequence (id, last_number) VALUES (1, 0)
ON DUPLICATE KEY UPDATE last_number = last_number;

-- Update sequences to start from the current max values
UPDATE product_sequence 
SET last_number = (SELECT COALESCE(MAX(CAST(SUBSTRING(product_id, 4) AS UNSIGNED)), 0) 
                   FROM product);

UPDATE product_images_sequence 
SET last_number = (SELECT COALESCE(MAX(img_id), 0) FROM product_images); 