-- Demo users — passwords hashed inline by PostgreSQL's pgcrypto extension.
-- admin@taskflow.dev    → admin123
-- *@taskflow.dev        → user123
--
-- Using crypt() + gen_salt('bf', 12) produces a proper BCrypt $2a$12$ hash
-- that Spring Security's BCryptPasswordEncoder verifies correctly at runtime.

CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO users (id, email, password, role) VALUES
    ('00000000-0000-0000-0000-000000000001', 'admin@taskflow.dev',
     crypt('admin123', gen_salt('bf', 12)), 'ROLE_ADMIN'),

    ('00000000-0000-0000-0000-000000000002', 'sarah.pm@taskflow.dev',
     crypt('user123',  gen_salt('bf', 12)), 'ROLE_USER'),

    ('00000000-0000-0000-0000-000000000003', 'carlos.dev@taskflow.dev',
     crypt('user123',  gen_salt('bf', 12)), 'ROLE_USER'),

    ('00000000-0000-0000-0000-000000000004', 'julia.qa@taskflow.dev',
     crypt('user123',  gen_salt('bf', 12)), 'ROLE_USER');
