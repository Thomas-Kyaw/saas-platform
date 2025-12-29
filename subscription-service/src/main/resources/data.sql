-- Insert FREE Plan
INSERT INTO subscription_plans (id, name, tier, monthly_price, annual_price, max_users, max_api_calls_per_month, max_storage_gb, features, created_at, updated_at)
VALUES
  (RANDOM_UUID(), 'Free Plan', 'FREE', 0.00, 0.00, 1, 1000, 1, '["Basic Support", "1 User"]', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert BASIC Plan
INSERT INTO subscription_plans (id, name, tier, monthly_price, annual_price, max_users, max_api_calls_per_month, max_storage_gb, features, created_at, updated_at)
VALUES
  (RANDOM_UUID(), 'Basic Plan', 'BASIC', 29.99, 299.99, 5, 10000, 10, '["Email Support", "5 Users", "10GB Storage"]', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert PRO Plan
INSERT INTO subscription_plans (id, name, tier, monthly_price, annual_price, max_users, max_api_calls_per_month, max_storage_gb, features, created_at, updated_at)
VALUES
  (RANDOM_UUID(), 'Pro Plan', 'PRO', 99.99, 999.99, 25, 100000, 100, '["Priority Support", "25 Users", "100GB Storage", "Advanced Analytics"]', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert ENTERPRISE Plan
INSERT INTO subscription_plans (id, name, tier, monthly_price, annual_price, max_users, max_api_calls_per_month, max_storage_gb, features, created_at, updated_at)
VALUES
  (RANDOM_UUID(), 'Enterprise Plan', 'ENTERPRISE', 499.99, 4999.99, NULL, NULL, NULL, '["24/7 Support", "Unlimited Users", "Unlimited Storage", "Custom Integrations", "SLA"]', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
