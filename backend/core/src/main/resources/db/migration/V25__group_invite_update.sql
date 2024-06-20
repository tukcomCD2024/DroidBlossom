alter table group_invite
    drop foreign key fk_group_invite_member_id;

alter table group_invite
    drop column member_id;


ALTER TABLE group_invite
    ADD COLUMN group_owner_id BIGINT;
ALTER TABLE group_invite
    ADD COLUMN group_member_id BIGINT;

ALTER TABLE group_invite
    ADD CONSTRAINT fk_group_invite_group_owner_id FOREIGN KEY (group_owner_id) REFERENCES member (member_id);

ALTER TABLE group_invite
    ADD CONSTRAINT fk_group_invite_group_member_id FOREIGN KEY (group_member_id) REFERENCES member (member_id);

ALTER TABLE group_invite
    ADD CONSTRAINT unique_owner_member_pair UNIQUE (group_owner_id, group_member_id);
