-- User profiles. The auth-service stores credentials;
-- this service stores everything else about the person.

CREATE TABLE user_profiles (
    id          UUID PRIMARY KEY,  -- same UUID as in auth-service
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    avatar_url  VARCHAR(512),
    job_title   VARCHAR(255),
    department  VARCHAR(255),
    active      BOOLEAN      NOT NULL DEFAULT true,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_user_profiles_email ON user_profiles(email);
