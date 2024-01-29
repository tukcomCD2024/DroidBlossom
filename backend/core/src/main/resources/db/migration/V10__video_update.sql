-- video BaseEntity 추가
ALTER TABLE video
    ADD COLUMN created_at TIMESTAMP null;

ALTER TABLE video
    ADD COLUMN updated_at TIMESTAMP null;