-- Schema creation script for Feature Flag API (H2 Database)
-- This script creates the necessary tables based on the JPA entities

-- Drop indexes if they exist (before dropping tables)
DROP INDEX IF EXISTS idx_feature_configs_feature_id;
DROP INDEX IF EXISTS idx_feature_configs_client_id;
DROP INDEX IF EXISTS idx_feature_configs_environment;
DROP INDEX IF EXISTS idx_features_name;
DROP INDEX IF EXISTS idx_users_active;
DROP INDEX IF EXISTS idx_users_username;

-- Drop tables if they exist (in reverse order due to foreign keys)
DROP TABLE IF EXISTS feature_configs CASCADE;
DROP TABLE IF EXISTS features CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create users table
CREATE TABLE users (
    id UUID NOT NULL DEFAULT RANDOM_UUID(),
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'USER', 'GUEST')),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);

-- Create features table
CREATE TABLE features (
    feature_id UUID NOT NULL DEFAULT RANDOM_UUID(),
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    enabled_by_default BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (feature_id)
);

-- Create feature_configs table
CREATE TABLE feature_configs (
    feature_config_id UUID NOT NULL DEFAULT RANDOM_UUID(),
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