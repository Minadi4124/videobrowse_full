-- SQL Script to add code and price columns to subscriptions table
-- Run this in MySQL Workbench to add code and price fields

-- Add code and price columns to existing subscriptions table
ALTER TABLE subscriptions 
ADD COLUMN code varchar(50) AFTER creator_name,
ADD COLUMN price varchar(20) AFTER code;

-- Update existing subscriptions with default values (optional)
UPDATE subscriptions SET 
    code = CASE 
        WHEN RAND() < 0.33 THEN 'basic'
        WHEN RAND() < 0.66 THEN 'standard' 
        ELSE 'premium' 
    END,
    price = CASE 
        WHEN code = 'basic' THEN 'Rs. 3000'
        WHEN code = 'standard' THEN 'Rs. 5000'
        WHEN code = 'premium' THEN 'Rs. 10000'
        ELSE 'Rs. 3000'
    END;

-- Show the updated data
SELECT * FROM subscriptions;
