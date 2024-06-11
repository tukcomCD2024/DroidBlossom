alter table group_capsule_open add column group_id BIGINT;
ALTER TABLE group_capsule_open
    ADD CONSTRAINT fk_group_capsule_open_group_id FOREIGN KEY (group_id) REFERENCES `group` (group_id);