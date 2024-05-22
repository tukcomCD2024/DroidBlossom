ALTER TABLE group_invite DROP CONSTRAINT fk_group_invite_group_member_id;
ALTER TABLE group_invite DROP CONSTRAINT fk_group_invite_group_owner_id;

ALTER TABLE group_invite DROP CONSTRAINT unique_owner_member_pair;

ALTER TABLE group_invite
    ADD CONSTRAINT fk_group_invite_group_owner_id FOREIGN KEY (group_owner_id) REFERENCES member (member_id);

ALTER TABLE group_invite
    ADD CONSTRAINT fk_group_invite_group_member_id FOREIGN KEY (group_member_id) REFERENCES member (member_id);

ALTER TABLE group_invite
    ADD CONSTRAINT unique_group_invite UNIQUE (group_id, group_owner_id, group_member_id);