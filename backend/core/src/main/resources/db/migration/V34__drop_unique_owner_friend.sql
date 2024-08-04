-- friend_invite 외래 키 삭제 -> 유니크 키 삭제 -> 외래 키 추가
ALTER TABLE friend_invite
    DROP CONSTRAINT fk_friend_invite_owner_id;

ALTER TABLE friend_invite
    DROP CONSTRAINT fk_friend_invite_friend_id;

ALTER TABLE friend_invite DROP KEY unique_owner_friend_pair;

ALTER TABLE friend_invite
    ADD CONSTRAINT fk_friend_invite_owner_id FOREIGN KEY (owner_id) REFERENCES member (member_id);

ALTER TABLE friend_invite
    ADD CONSTRAINT fk_friend_invite_friend_id FOREIGN KEY (friend_id) REFERENCES member (member_id);

-- member_friend 외래 키 삭제 -> 유니크 키 삭제 -> 외래 키 추가
ALTER TABLE member_friend
    DROP CONSTRAINT fk_member_friend_owner_id;

ALTER TABLE member_friend
    DROP CONSTRAINT fk_member_friend_friend_id;

ALTER TABLE member_friend DROP KEY unique_owner_friend_relation;

ALTER TABLE member_friend
    ADD CONSTRAINT fk_member_friend_owner_id FOREIGN KEY (owner_id) REFERENCES member (member_id);

ALTER TABLE  member_friend
    ADD CONSTRAINT fk_member_friend_friend_id FOREIGN KEY (friend_id) REFERENCES member (member_id);