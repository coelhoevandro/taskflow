-- Same UUIDs as auth-service seed data
INSERT INTO user_profiles (id, name, email, job_title, department) VALUES
    ('00000000-0000-0000-0000-000000000001', 'Admin User',     'admin@taskflow.dev',   'Platform Admin',       'Engineering'),
    ('00000000-0000-0000-0000-000000000002', 'Sarah Miller',   'sarah.pm@taskflow.dev', 'Product Manager',     'Product'),
    ('00000000-0000-0000-0000-000000000003', 'Carlos Souza',   'carlos.dev@taskflow.dev', 'Backend Developer', 'Engineering'),
    ('00000000-0000-0000-0000-000000000004', 'Julia Ferreira', 'julia.qa@taskflow.dev', 'QA Analyst',         'Quality');
