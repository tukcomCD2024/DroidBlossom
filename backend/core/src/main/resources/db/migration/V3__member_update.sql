-- Add new columns auth_id
ALTER TABLE member
    ADD COLUMN auth_id VARCHAR(255) NOT NULL;
