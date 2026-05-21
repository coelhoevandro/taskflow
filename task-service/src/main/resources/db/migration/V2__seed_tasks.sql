-- Realistic demo tasks matching what a small product team would actually have
INSERT INTO tasks (id, title, description, status, priority, assignee_id, reporter_id, due_date, assignment_status) VALUES
    (
        'aaaaaaaa-0000-0000-0000-000000000001',
        'Set up CI/CD pipeline for staging environment',
        'Configure GitHub Actions to run tests and deploy to staging on every merge to main. Currently doing this manually and it''s slow.',
        'IN_PROGRESS', 'HIGH',
        '00000000-0000-0000-0000-000000000003',
        '00000000-0000-0000-0000-000000000002',
        CURRENT_DATE + 3,
        'CONFIRMED'
    ),
    (
        'aaaaaaaa-0000-0000-0000-000000000002',
        'Fix pagination bug on task list page',
        'When filtering by status and then changing page, the filter resets. Reported by Julia in the last sprint review.',
        'TODO', 'HIGH',
        '00000000-0000-0000-0000-000000000003',
        '00000000-0000-0000-0000-000000000004',
        CURRENT_DATE + 1,
        'CONFIRMED'
    ),
    (
        'aaaaaaaa-0000-0000-0000-000000000003',
        'Write API documentation for external integrations',
        'Partners need OpenAPI docs to integrate with our task API. The endpoint exists but there''s no public documentation yet.',
        'TODO', 'MEDIUM',
        '00000000-0000-0000-0000-000000000002',
        '00000000-0000-0000-0000-000000000001',
        CURRENT_DATE + 7,
        'CONFIRMED'
    ),
    (
        'aaaaaaaa-0000-0000-0000-000000000004',
        'Performance review for task search endpoint',
        'Search is slow when there are more than 500 tasks. Probably needs an index and maybe some query optimization.',
        'TODO', 'MEDIUM',
        NULL,
        '00000000-0000-0000-0000-000000000003',
        CURRENT_DATE + 14,
        'UNASSIGNED'
    ),
    (
        'aaaaaaaa-0000-0000-0000-000000000005',
        'Add e2e tests for task assignment flow',
        'The saga flow for task assignment is covered by unit tests but we don''t have any end-to-end coverage. Should add at least the happy path.',
        'IN_REVIEW', 'MEDIUM',
        '00000000-0000-0000-0000-000000000004',
        '00000000-0000-0000-0000-000000000002',
        CURRENT_DATE + 2,
        'CONFIRMED'
    ),
    (
        'aaaaaaaa-0000-0000-0000-000000000006',
        'Implement task due date reminders',
        'Users should get a notification 24h before a task is due. Notification service is already set up, just need to add the scheduler.',
        'TODO', 'LOW',
        NULL,
        '00000000-0000-0000-0000-000000000002',
        CURRENT_DATE + 21,
        'UNASSIGNED'
    ),
    (
        'aaaaaaaa-0000-0000-0000-000000000007',
        'Migrate legacy user import script to new user-service API',
        'Old script writes directly to the database. Now that user-service has the internal API, we should use that instead.',
        'DONE', 'LOW',
        '00000000-0000-0000-0000-000000000003',
        '00000000-0000-0000-0000-000000000001',
        CURRENT_DATE - 5,
        'CONFIRMED'
    ),
    (
        'aaaaaaaa-0000-0000-0000-000000000008',
        'Review and update onboarding documentation',
        'README hasn''t been updated since we added the gateway service. New developers are confused about how to set up the local environment.',
        'DONE', 'LOW',
        '00000000-0000-0000-0000-000000000002',
        '00000000-0000-0000-0000-000000000001',
        CURRENT_DATE - 3,
        'CONFIRMED'
    );

INSERT INTO task_comments (task_id, author_id, content, created_at) VALUES
    ('aaaaaaaa-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003',
     'Started working on this. The GitHub Actions setup is straightforward, main issue is the staging DB credentials — need to figure out secrets management.',
     NOW() - INTERVAL '2 days'),
    ('aaaaaaaa-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000002',
     'Can we use GitHub Secrets for now and move to Vault later? That should unblock you.',
     NOW() - INTERVAL '1 day'),
    ('aaaaaaaa-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003',
     'Yes, doing that. Should have a working pipeline by EOD.',
     NOW() - INTERVAL '6 hours'),

    ('aaaaaaaa-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000004',
     'I can reproduce this. Happens consistently with status=IN_PROGRESS and page > 1.',
     NOW() - INTERVAL '1 day'),
    ('aaaaaaaa-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003',
     'Looks like the filter state isn''t being preserved in the URL query params. Should be a quick fix.',
     NOW() - INTERVAL '5 hours'),

    ('aaaaaaaa-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000004',
     'Tests written and passing locally. PR is up for review.',
     NOW() - INTERVAL '3 hours');
