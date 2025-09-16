-- Schema creation script for Feature Flag API
-- This script creates the necessary tables based on the JPA entities

-- Drop tables if they exist (in reverse order due to foreign keys)
DROP TABLE IF EXISTS feature_configs CASCADE;
DROP TABLE IF EXISTS features CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create users table
CREATE TABLE users (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'USER', 'GUEST')),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);

-- Create features table
CREATE TABLE features (
    feature_id UUID NOT NULL DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    enabled_by_default BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (feature_id)
);

-- Create feature_configs table
CREATE TABLE feature_configs (
    feature_config_id UUID NOT NULL DEFAULT gen_random_uuid(),
    environment VARCHAR(20) NOT NULL CHECK (environment IN ('DEV', 'STAGING', 'PROD')),
    client_id VARCHAR(255),
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    feature_id UUID NOT NULL,
    PRIMARY KEY (feature_config_id),
    FOREIGN KEY (feature_id) REFERENCES features(feature_id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_active ON users(active);
CREATE INDEX idx_features_name ON features(name);
CREATE INDEX idx_feature_configs_environment ON feature_configs(environment);
CREATE INDEX idx_feature_configs_client_id ON feature_configs(client_id);
CREATE INDEX idx_feature_configs_feature_id ON feature_configs(feature_id);

-- Comments for documentation
COMMENT ON TABLE users IS 'User accounts for authentication and authorization';
COMMENT ON TABLE features IS 'Feature toggles/flags that can be enabled/disabled';
COMMENT ON TABLE feature_configs IS 'Configuration of features per environment and client';

COMMENT ON COLUMN users.role IS 'User role: ADMIN, USER, or GUEST';
COMMENT ON COLUMN users.active IS 'Whether the user account is active';
COMMENT ON COLUMN features.enabled_by_default IS 'Default state for new feature configurations';
COMMENT ON COLUMN feature_configs.environment IS 'Environment: DEV, STAGING, or PROD';
COMMENT ON COLUMN feature_configs.client_id IS 'Optional client identifier for client-specific configurations';
COMMENT ON COLUMN feature_configs.enabled IS 'Whether the feature is enabled in this environment/client';