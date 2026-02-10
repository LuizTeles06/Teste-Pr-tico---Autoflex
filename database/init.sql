-- Initial database setup
-- This file is executed when the PostgreSQL container is first created

-- Create the database if it doesn't exist (handled by POSTGRES_DB env var)

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE inventory_db TO postgres;

-- Create schema (optional, Hibernate will create tables)
-- If you want to pre-populate data, add INSERT statements here

-- Example test data (uncomment to use):
/*
INSERT INTO raw_materials (name, stock_quantity) VALUES
    ('Steel', 1000),
    ('Aluminum', 500),
    ('Plastic', 2000),
    ('Rubber', 300),
    ('Glass', 150);

INSERT INTO products (name, value) VALUES
    ('Widget A', 50.00),
    ('Widget B', 75.00),
    ('Gadget X', 120.00);

INSERT INTO product_raw_materials (product_id, raw_material_id, required_quantity) VALUES
    (1, 1, 10),  -- Widget A needs 10 Steel
    (1, 3, 5),   -- Widget A needs 5 Plastic
    (2, 1, 15),  -- Widget B needs 15 Steel
    (2, 2, 8),   -- Widget B needs 8 Aluminum
    (3, 1, 20),  -- Gadget X needs 20 Steel
    (3, 2, 10),  -- Gadget X needs 10 Aluminum
    (3, 5, 2);   -- Gadget X needs 2 Glass
*/
