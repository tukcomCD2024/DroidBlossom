ALTER TABLE friend_invite ADD CONSTRAINT unique_owner_friend_pair UNIQUE (owner_id, friend_id);
