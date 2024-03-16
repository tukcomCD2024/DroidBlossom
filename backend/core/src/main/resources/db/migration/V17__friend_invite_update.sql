# 친구 대상 아이디 추가
ALTER TABLE friend_invite
    ADD COLUMN friend_id bigint,
    ADD FOREIGN KEY fk_member_friend_member(member_id) REFERENCES member(member_id);

ALTER TABLE friend_invite
    CHANGE member_id owner_id BIGINT