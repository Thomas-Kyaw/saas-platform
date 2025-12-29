-- =========================
-- seed (idempotent)
-- =========================

-- credentials
INSERT INTO credentials (user_id, password_hash, status)
SELECT
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'::uuid,
    '$2b$12$REPLACE_WITH_BCRYPT_HASH',
    'active'
WHERE NOT EXISTS (
    SELECT 1 FROM credentials
    WHERE user_id = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'::uuid
);

INSERT INTO credentials (user_id, password_hash, status)
SELECT
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'::uuid,
    '$2b$12$REPLACE_WITH_BCRYPT_HASH',
    'active'
WHERE NOT EXISTS (
    SELECT 1 FROM credentials
    WHERE user_id = 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'::uuid
);

-- refresh tokens (use DB-generated id, dedupe by user_id + token_hash)
INSERT INTO refresh_tokens (user_id, token_hash, expires_at, revoked_at)
SELECT
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'::uuid,
    'sha256:REPLACE_WITH_HASH',
    now() + interval '30 days',
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM refresh_tokens
    WHERE user_id = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'::uuid
      AND token_hash = 'sha256:REPLACE_WITH_HASH'
);
