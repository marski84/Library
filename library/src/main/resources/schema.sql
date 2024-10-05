-- create table,add constraints and validation
CREATE TABLE IF NOT EXISTS users (
                       id BIGSERIAL PRIMARY KEY,
                       user_name VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       penalty_points INTEGER DEFAULT 0 NOT NULL,
                       is_blocked BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE INDEX idx_users_user_name ON users(user_name);
ALTER TABLE users ADD CONSTRAINT uq_users_user_name UNIQUE (user_name);
ALTER TABLE users ADD CONSTRAINT chk_penalty_points_non_negative CHECK (penalty_points >= 0);
-----


