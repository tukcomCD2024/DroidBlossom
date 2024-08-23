-- 외래 키 제약 조건 삭제
ALTER TABLE group_invite
    DROP CONSTRAINT fk_group_invite_group_id;

ALTER TABLE group_invite
    DROP CONSTRAINT fk_group_invite_group_owner_id;

ALTER TABLE group_invite
    DROP CONSTRAINT fk_group_invite_group_member_id;

-- 유니크 키 삭제
ALTER TABLE group_invite DROP KEY unique_group_invite;

-- 외래 키 제약 조건 추가
ALTER TABLE group_invite
    ADD CONSTRAINT fk_group_invite_group_id FOREIGN KEY (group_id) REFERENCES `group` (group_id);

ALTER TABLE group_invite
    ADD CONSTRAINT fk_group_invite_group_owner_id FOREIGN KEY (group_owner_id) REFERENCES member (member_id);

ALTER TABLE group_invite
    ADD CONSTRAINT fk_group_invite_group_member_id FOREIGN KEY (group_member_id) REFERENCES member (member_id);