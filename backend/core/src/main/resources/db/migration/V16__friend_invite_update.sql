ALTER TABLE friend_invite
    ADD COLUMN friend_status VARCHAR(20);
ALTER TABLE friend_invite
    DROP CONSTRAINT fk_friend_invite_member_id;
ALTER TABLE friend_invite
    DROP COLUMN member_id;

ALTER TABLE friend_invite
    ADD COLUMN owner_id BIGINT;
ALTER TABLE friend_invite
    ADD COLUMN friend_id BIGINT;
ALTER TABLE friend_invite
    ADD CONSTRAINT fk_friend_invite_owner_id FOREIGN KEY (owner_id) REFERENCES member (member_id);

ALTER TABLE friend_invite
    ADD CONSTRAINT fk_friend_invite_friend_id FOREIGN KEY (friend_id) REFERENCES member (member_id);
