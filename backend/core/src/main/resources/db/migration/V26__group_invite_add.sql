alter table group_invite add column group_id BIGINT;
ALTER TABLE group_invite
    ADD CONSTRAINT fk_group_invite_group_id FOREIGN KEY (group_id) REFERENCES `group` (group_id);