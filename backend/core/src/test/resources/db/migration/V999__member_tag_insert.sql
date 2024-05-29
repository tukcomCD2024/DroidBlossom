insert into member (is_verified, notification_enabled, created_at, member_id, updated_at, phone, nickname, social_type, email, fcm_token, profile_url, auth_id, phone_hash, tag, password)
values  (true, false, now(), 9999, now(), null, 'test_nickname_9999', 'KAKAO', 'test_email_9999@gmail.com', null, 'http://test_profile_url_9999.com', 'test_auth_id_9999', null, 'test_tag_9999', null),
        (true, false, now(), 10000, now(), null, 'test_nickname_10000', 'KAKAO', 'test_email_10000@gmail.co', null, 'http://test_profile_url_10000.com', 'test_auth_id_10000', null, 'test_tag_10000', null),
        (true, false, now(), 10001, now(), null, 'test_nickname_10001', 'KAKAO', 'test_email_10001@gmail.co', null, 'http://test_profile_url_10001.com', 'test_auth_id_10001', null, 'test_tag_10001', null),
        (true, false, now(), 10002, now(), null, 'test_nickname_10002', 'KAKAO', 'test_email_10002@gmail.co', null, 'http://test_profile_url_10002.com', 'test_auth_id_10002', null, 'test_tag_10002', null),
        (true, false, now(), 10003, now(), null, 'test_nickname_10003', 'KAKAO', 'test_email_10003@gmail.co', null, 'http://test_profile_url_10003.com', 'test_auth_id_10003', null, 'test_tag_10003', null);

insert into member_friend(member_friend_id, owner_id, friend_id, created_at, updated_at)
values (1, 10000, 9999, now(), now()),
       (2, 9999, 10000, now(), now()),
       (3, 9999, 10001, now(), now()),
       (4, 10001, 9999, now(), now());

insert into friend_invite(friend_invite_id, owner_id, friend_id, created_at, updated_at)
values (1, 10002, 9999, now(), now()),
       (2, 9999, 10002, now(), now()),
       (3, 9999, 10003, now(), now()),
       (4, 10003, 9999, now(), now());