-- Subscription Plans Table
CREATE TABLE IF NOT EXISTS subscription_plans (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    tier VARCHAR(20) NOT NULL,
    monthly_price DECIMAL(10,2),
    annual_price DECIMAL(10,2),
    max_users INTEGER,
    max_api_calls_per_month INTEGER,
    max_storage_gb INTEGER,
    features TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Tenant Subscriptions Table
CREATE TABLE IF NOT EXISTS tenant_subscriptions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    plan_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    auto_renew BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_subscription_plan FOREIGN KEY (plan_id) REFERENCES subscription_plans(id),
    CONSTRAINT uk_tenant_id UNIQUE (tenant_id)
);

-- Index for faster tenant lookups
CREATE INDEX IF NOT EXISTS idx_tenant_subscriptions_tenant_id ON tenant_subscriptions(tenant_id);
