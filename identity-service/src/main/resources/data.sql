DO $$
    DECLARE ts timestamptz := now();
    BEGIN

        -- Tenants
        INSERT INTO tenants (id, name, status, created_at, updated_at)
        SELECT '11111111-1111-1111-1111-111111111111'::uuid, 'Acme Inc', 'active', ts, ts
        WHERE NOT EXISTS (SELECT 1 FROM tenants WHERE id = '11111111-1111-1111-1111-111111111111'::uuid);

        INSERT INTO tenants (id, name, status, created_at, updated_at)
        SELECT '22222222-2222-2222-2222-222222222222'::uuid, 'Beta LLC', 'active', ts, ts
        WHERE NOT EXISTS (SELECT 1 FROM tenants WHERE id = '22222222-2222-2222-2222-222222222222'::uuid);

-- Users
        INSERT INTO users (id, email, display_name, status, created_at, updated_at)
        SELECT 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'::uuid, 'admin@acme.com', 'Acme Admin', 'active', ts, ts
        WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'::uuid);

        INSERT INTO users (id, email, display_name, status, created_at, updated_at)
        SELECT 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'::uuid, 'member@acme.com', 'Acme Member', 'active', ts, ts
        WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'::uuid);

        INSERT INTO users (id, email, display_name, status, created_at, updated_at)
        SELECT 'cccccccc-cccc-cccc-cccc-cccccccccccc'::uuid, 'admin@beta.com', 'Beta Admin', 'active', ts, ts
        WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 'cccccccc-cccc-cccc-cccc-cccccccccccc'::uuid);

-- Tenant memberships
        INSERT INTO tenant_memberships (tenant_id, user_id, status, created_at, updated_at)
        SELECT '11111111-1111-1111-1111-111111111111'::uuid, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'::uuid, 'active', ts, ts
        WHERE NOT EXISTS (
            SELECT 1 FROM tenant_memberships
            WHERE tenant_id = '11111111-1111-1111-1111-111111111111'::uuid
              AND user_id   = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'::uuid
        );

        INSERT INTO tenant_memberships (tenant_id, user_id, status, created_at, updated_at)
        SELECT '11111111-1111-1111-1111-111111111111'::uuid, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'::uuid, 'active', ts, ts
        WHERE NOT EXISTS (
            SELECT 1 FROM tenant_memberships
            WHERE tenant_id = '11111111-1111-1111-1111-111111111111'::uuid
              AND user_id   = 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'::uuid
        );

        INSERT INTO tenant_memberships (tenant_id, user_id, status, created_at, updated_at)
        SELECT '22222222-2222-2222-2222-222222222222'::uuid, 'cccccccc-cccc-cccc-cccc-cccccccccccc'::uuid, 'active', ts, ts
        WHERE NOT EXISTS (
            SELECT 1 FROM tenant_memberships
            WHERE tenant_id = '22222222-2222-2222-2222-222222222222'::uuid
              AND user_id   = 'cccccccc-cccc-cccc-cccc-cccccccccccc'::uuid
        );

-- Permissions (global)
        INSERT INTO permissions (key, created_at, updated_at)
        SELECT 'tenant.read', ts, ts
        WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE key = 'tenant.read');

        INSERT INTO permissions (key, created_at, updated_at)
        SELECT 'tenant.write', ts, ts
        WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE key = 'tenant.write');

        INSERT INTO permissions (key, created_at, updated_at)
        SELECT 'users.manage', ts, ts
        WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE key = 'users.manage');

        INSERT INTO permissions (key, created_at, updated_at)
        SELECT 'roles.manage', ts, ts
        WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE key = 'roles.manage');

-- Roles (per tenant)
        INSERT INTO roles (tenant_id, name, created_at, updated_at)
        SELECT '11111111-1111-1111-1111-111111111111'::uuid, 'owner', ts, ts
        WHERE NOT EXISTS (
            SELECT 1 FROM roles
            WHERE tenant_id = '11111111-1111-1111-1111-111111111111'::uuid
              AND name = 'owner'
        );

        INSERT INTO roles (tenant_id, name, created_at, updated_at)
        SELECT '11111111-1111-1111-1111-111111111111'::uuid, 'member', ts, ts
        WHERE NOT EXISTS (
            SELECT 1 FROM roles
            WHERE tenant_id = '11111111-1111-1111-1111-111111111111'::uuid
              AND name = 'member'
        );

        INSERT INTO roles (tenant_id, name, created_at, updated_at)
        SELECT '22222222-2222-2222-2222-222222222222'::uuid, 'owner', ts, ts
        WHERE NOT EXISTS (
            SELECT 1 FROM roles
            WHERE tenant_id = '22222222-2222-2222-2222-222222222222'::uuid
              AND name = 'owner'
        );

-- Role permissions
        INSERT INTO role_permissions (role_id, permission_id, created_at)
        SELECT r.id, p.id, ts
        FROM roles r
                 JOIN permissions p ON p.key IN ('tenant.read','tenant.write','users.manage','roles.manage')
        WHERE r.tenant_id = '11111111-1111-1111-1111-111111111111'::uuid
          AND r.name = 'owner'
          AND NOT EXISTS (
            SELECT 1 FROM role_permissions rp
            WHERE rp.role_id = r.id AND rp.permission_id = p.id
        );

        INSERT INTO role_permissions (role_id, permission_id, created_at)
        SELECT r.id, p.id, ts
        FROM roles r
                 JOIN permissions p ON p.key IN ('tenant.read')
        WHERE r.tenant_id = '11111111-1111-1111-1111-111111111111'::uuid
          AND r.name = 'member'
          AND NOT EXISTS (
            SELECT 1 FROM role_permissions rp
            WHERE rp.role_id = r.id AND rp.permission_id = p.id
        );

        INSERT INTO role_permissions (role_id, permission_id, created_at)
        SELECT r.id, p.id, ts
        FROM roles r
                 JOIN permissions p ON p.key IN ('tenant.read','tenant.write','users.manage','roles.manage')
        WHERE r.tenant_id = '22222222-2222-2222-2222-222222222222'::uuid
          AND r.name = 'owner'
          AND NOT EXISTS (
            SELECT 1 FROM role_permissions rp
            WHERE rp.role_id = r.id AND rp.permission_id = p.id
        );

-- User roles
        INSERT INTO user_roles (tenant_id, user_id, role_id, created_at)
        SELECT
            '11111111-1111-1111-1111-111111111111'::uuid,
            'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'::uuid,
            r.id,
            ts
        FROM roles r
        WHERE r.tenant_id = '11111111-1111-1111-1111-111111111111'::uuid
          AND r.name = 'owner'
          AND NOT EXISTS (
            SELECT 1 FROM user_roles ur
            WHERE ur.tenant_id = '11111111-1111-1111-1111-111111111111'::uuid
              AND ur.user_id   = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'::uuid
              AND ur.role_id   = r.id
        );

        INSERT INTO user_roles (tenant_id, user_id, role_id, created_at)
        SELECT
            '11111111-1111-1111-1111-111111111111'::uuid,
            'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'::uuid,
            r.id,
            ts
        FROM roles r
        WHERE r.tenant_id = '11111111-1111-1111-1111-111111111111'::uuid
          AND r.name = 'member'
          AND NOT EXISTS (
            SELECT 1 FROM user_roles ur
            WHERE ur.tenant_id = '11111111-1111-1111-1111-111111111111'::uuid
              AND ur.user_id   = 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'::uuid
              AND ur.role_id   = r.id
        );

        INSERT INTO user_roles (tenant_id, user_id, role_id, created_at)
        SELECT
            '22222222-2222-2222-2222-222222222222'::uuid,
            'cccccccc-cccc-cccc-cccc-cccccccccccc'::uuid,
            r.id,
            ts
        FROM roles r
        WHERE r.tenant_id = '22222222-2222-2222-2222-222222222222'::uuid
          AND r.name = 'owner'
          AND NOT EXISTS (
            SELECT 1 FROM user_roles ur
            WHERE ur.tenant_id = '22222222-2222-2222-2222-222222222222'::uuid
              AND ur.user_id   = 'cccccccc-cccc-cccc-cccc-cccccccccccc'::uuid
              AND ur.role_id   = r.id
        );

    END $$;
