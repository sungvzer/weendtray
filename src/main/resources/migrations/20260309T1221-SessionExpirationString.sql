ALTER TABLE user_session DROP COLUMN expires_at;
ALTER TABLE user_session ADD COLUMN expires_at TEXT;
