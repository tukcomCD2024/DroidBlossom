ALTER TABLE member_friend
    ADD CONSTRAINT unique_owner_friend_relation UNIQUE (owner_id, friend_id);
