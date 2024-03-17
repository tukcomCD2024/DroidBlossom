ALTER TABLE member_friend DROP CONSTRAINT fk_member_friend_member_id;
ALTER TABLE member_friend DROP COLUMN member_id;

ALTER TABLE member_friend ADD COLUMN owner_id BIGINT;
ALTER TABLE member_friend ADD COLUMN friend_id BIGINT;

ALTER TABLE member_friend
    ADD CONSTRAINT fk_member_friend_owner_id FOREIGN KEY (owner_id) REFERENCES member (member_id);

ALTER TABLE  member_friend
    ADD CONSTRAINT fk_member_friend_friend_id FOREIGN KEY (friend_id) REFERENCES member (member_id);
