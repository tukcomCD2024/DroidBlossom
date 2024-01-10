-- Add new columns
ALTER TABLE member
    CHANGE COLUMN oauth2_provider social_type VARCHAR(255) NOT NULL;

ALTER TABLE member
    ADD COLUMN role VARCHAR(255) NOT NULL;
