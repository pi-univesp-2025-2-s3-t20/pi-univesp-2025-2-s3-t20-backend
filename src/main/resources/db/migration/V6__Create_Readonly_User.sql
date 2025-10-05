-- Migration V6: Create readonly user
-- This migration creates a readonly user for database access
-- Only executes on PostgreSQL databases (skipped for H2)

-- Check if we're running on PostgreSQL by testing for PostgreSQL-specific functions
-- H2 doesn't support user management, so we skip this migration
DO $$
BEGIN
    -- Only proceed if we're on PostgreSQL (check for PostgreSQL-specific system catalogs)
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'pg_roles') THEN
        -- Check if user already exists
        IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'pi_readonly') THEN
            CREATE ROLE pi_readonly WITH LOGIN PASSWORD 'readonly_password_2025';
        END IF;

        -- Grant connect privilege to the database
        GRANT CONNECT ON DATABASE pi_univesp_prd TO pi_readonly;

        -- Grant usage on schema
        GRANT USAGE ON SCHEMA public TO pi_readonly;

        -- Grant select privileges on all existing tables
        GRANT SELECT ON ALL TABLES IN SCHEMA public TO pi_readonly;

        -- Grant select privileges on all future tables
        ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO pi_readonly;

        -- Grant usage on all sequences (for potential future use)
        GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO pi_readonly;
        ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE ON SEQUENCES TO pi_readonly;

        -- Create a comment for documentation
        COMMENT ON ROLE pi_readonly IS 'Readonly user for PI Univesp application - created by migration V6';
    END IF;
END
$$;
