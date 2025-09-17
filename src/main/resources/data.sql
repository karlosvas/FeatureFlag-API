-- Data insertion script for Feature Flag API
-- This script inserts initial sample data for testing and development

-- Insert sample users
-- Note: Passwords should be properly hashed in production using BCrypt
-- These are example hashed passwords for 'password123'
INSERT INTO users (id, username, email, password, role, active) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'admin', 'admin@featureflag.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.FGvC8hRgQzlI6DzZ6/Vf6wrq.F4j3m', 'ADMIN', true),
    ('550e8400-e29b-41d4-a716-446655440002', 'user1', 'user1@featureflag.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.FGvC8hRgQzlI6DzZ6/Vf6wrq.F4j3m', 'USER', true),
    ('550e8400-e29b-41d4-a716-446655440003', 'guest', 'guest@featureflag.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.FGvC8hRgQzlI6DzZ6/Vf6wrq.F4j3m', 'GUEST', true),
    ('550e8400-e29b-41d4-a716-446655440004', 'testuser', 'test@featureflag.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.FGvC8hRgQzlI6DzZ6/Vf6wrq.F4j3m', 'USER', false);

-- Insert sample features
INSERT INTO features (feature_id, name, description, enabled_by_default) VALUES
    ('660e8400-e29b-41d4-a716-446655440001', 'NEW_USER_DASHBOARD', 'Enable the new user dashboard interface', false),
    ('660e8400-e29b-41d4-a716-446655440002', 'ADVANCED_ANALYTICS', 'Enable advanced analytics and reporting features', false),
    ('660e8400-e29b-41d4-a716-446655440003', 'MOBILE_APP_INTEGRATION', 'Enable mobile application integration features', true),
    ('660e8400-e29b-41d4-a716-446655440004', 'BETA_FEATURES', 'Enable access to beta features for testing', false),
    ('660e8400-e29b-41d4-a716-446655440005', 'DARK_MODE', 'Enable dark mode theme option', true),
    ('660e8400-e29b-41d4-a716-446655440006', 'REAL_TIME_NOTIFICATIONS', 'Enable real-time push notifications', false),
    ('660e8400-e29b-41d4-a716-446655440007', 'EXPORT_DATA', 'Enable data export functionality', true);

-- Insert sample feature configurations
-- NEW_USER_DASHBOARD configurations
INSERT INTO feature_configs (feature_config_id, environment, client_id, enabled, feature_id) VALUES
    ('770e8400-e29b-41d4-a716-446655440001', 'DEV', NULL, true, '660e8400-e29b-41d4-a716-446655440001'),
    ('770e8400-e29b-41d4-a716-446655440002', 'STAGING', NULL, true, '660e8400-e29b-41d4-a716-446655440001'),
    ('770e8400-e29b-41d4-a716-446655440003', 'PROD', NULL, false, '660e8400-e29b-41d4-a716-446655440001'),
    ('770e8400-e29b-41d4-a716-446655440004', 'PROD', 'client-premium', true, '660e8400-e29b-41d4-a716-446655440001');

-- ADVANCED_ANALYTICS configurations
INSERT INTO feature_configs (feature_config_id, environment, client_id, enabled, feature_id) VALUES
    ('770e8400-e29b-41d4-a716-446655440005', 'DEV', NULL, true, '660e8400-e29b-41d4-a716-446655440002'),
    ('770e8400-e29b-41d4-a716-446655440006', 'STAGING', NULL, false, '660e8400-e29b-41d4-a716-446655440002'),
    ('770e8400-e29b-41d4-a716-446655440007', 'PROD', NULL, false, '660e8400-e29b-41d4-a716-446655440002');

-- MOBILE_APP_INTEGRATION configurations
INSERT INTO feature_configs (feature_config_id, environment, client_id, enabled, feature_id) VALUES
    ('770e8400-e29b-41d4-a716-446655440008', 'DEV', NULL, true, '660e8400-e29b-41d4-a716-446655440003'),
    ('770e8400-e29b-41d4-a716-446655440009', 'STAGING', NULL, true, '660e8400-e29b-41d4-a716-446655440003'),
    ('770e8400-e29b-41d4-a716-446655440010', 'PROD', NULL, true, '660e8400-e29b-41d4-a716-446655440003');

-- BETA_FEATURES configurations
INSERT INTO feature_configs (feature_config_id, environment, client_id, enabled, feature_id) VALUES
    ('770e8400-e29b-41d4-a716-446655440011', 'DEV', NULL, true, '660e8400-e29b-41d4-a716-446655440004'),
    ('770e8400-e29b-41d4-a716-446655440012', 'STAGING', NULL, true, '660e8400-e29b-41d4-a716-446655440004'),
    ('770e8400-e29b-41d4-a716-446655440013', 'PROD', NULL, false, '660e8400-e29b-41d4-a716-446655440004'),
    ('770e8400-e29b-41d4-a716-446655440014', 'PROD', 'client-beta-testers', true, '660e8400-e29b-41d4-a716-446655440004');

-- DARK_MODE configurations
INSERT INTO feature_configs (feature_config_id, environment, client_id, enabled, feature_id) VALUES
    ('770e8400-e29b-41d4-a716-446655440015', 'DEV', NULL, true, '660e8400-e29b-41d4-a716-446655440005'),
    ('770e8400-e29b-41d4-a716-446655440016', 'STAGING', NULL, true, '660e8400-e29b-41d4-a716-446655440005'),
    ('770e8400-e29b-41d4-a716-446655440017', 'PROD', NULL, true, '660e8400-e29b-41d4-a716-446655440005');

-- REAL_TIME_NOTIFICATIONS configurations
INSERT INTO feature_configs (feature_config_id, environment, client_id, enabled, feature_id) VALUES
    ('770e8400-e29b-41d4-a716-446655440018', 'DEV', NULL, true, '660e8400-e29b-41d4-a716-446655440006'),
    ('770e8400-e29b-41d4-a716-446655440019', 'STAGING', NULL, false, '660e8400-e29b-41d4-a716-446655440006'),
    ('770e8400-e29b-41d4-a716-446655440020', 'PROD', NULL, false, '660e8400-e29b-41d4-a716-446655440006');

-- EXPORT_DATA configurations
INSERT INTO feature_configs (feature_config_id, environment, client_id, enabled, feature_id) VALUES
    ('770e8400-e29b-41d4-a716-446655440021', 'DEV', NULL, true, '660e8400-e29b-41d4-a716-446655440007'),
    ('770e8400-e29b-41d4-a716-446655440022', 'STAGING', NULL, true, '660e8400-e29b-41d4-a716-446655440007'),
    ('770e8400-e29b-41d4-a716-446655440023', 'PROD', NULL, true, '660e8400-e29b-41d4-a716-446655440007'),
    ('770e8400-e29b-41d4-a716-446655440024', 'PROD', 'client-enterprise', true, '660e8400-e29b-41d4-a716-446655440007');

-- Insert verification queries (for testing)
-- Uncomment these lines to verify the data was inserted correctly
-- SELECT 'Users count:' as info, COUNT(*) as count FROM users;
-- SELECT 'Features count:' as info, COUNT(*) as count FROM features;
-- SELECT 'Feature configs count:' as info, COUNT(*) as count FROM feature_configs;