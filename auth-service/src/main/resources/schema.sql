-- auth-service/schema.sql
-- - credentials.user_id comes from identity-service (no FK)
-- - refresh_tokens.id DB-generated UUID
-- - created_at defaults to now()
-- - updated_at maintained via trigger (so it actually updates)

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================
-- credentials
-- =========================
CREATE TABLE IF NOT EXISTS credentials (
    user_id UUID PRIMARY KEY,
    password_hash TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'active'
       CHECK (status IN ('active','disabled','locked')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_credentials_status
    ON credentials(status);

-- =========================
-- refresh tokens
-- =========================
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    token_hash TEXT NOT NULL,              -- hash of refresh token
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ,                -- null = active
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_rt_user_id
    ON refresh_tokens(user_id);

CREATE INDEX IF NOT EXISTS idx_rt_expires_at
    ON refresh_tokens(expires_at);

CREATE INDEX IF NOT EXISTS idx_rt_revoked_at
    ON refresh_tokens(revoked_at);

CREATE UNIQUE INDEX IF NOT EXISTS uq_rt_user_token_hash
    ON refresh_tokens(user_id, token_hash);

-- Optional but useful: fast queries for "active" tokens and cleanup
CREATE INDEX IF NOT EXISTS idx_rt_active_by_user
    ON refresh_tokens(user_id)
    WHERE revoked_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_rt_active_expires
    ON refresh_tokens(expires_at)
    WHERE revoked_at IS NULL;

-- =========================
-- updated_at trigger (credentials only)
-- =========================
CREATE OR REPLACE FUNCTION set_updated_at()
    RETURNS trigger AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_credentials_updated_at ON credentials;

CREATE TRIGGER trg_credentials_updated_at
    BEFORE UPDATE ON credentials
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
