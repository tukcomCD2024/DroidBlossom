INSERT INTO notification_category (category_name, category_description, created_at, updated_at)
SELECT 'CAPSULE_SKIN', '캡슐 스킨 생성 관련', now(), now()
WHERE NOT EXISTS(SELECT * FROM notification_category where category_name='CAPSULE_SKIN');

INSERT INTO notification_category (category_name, category_description, created_at, updated_at)
SELECT 'FRIEND_REQUEST', '친구 요청 관련', now(), now()
WHERE NOT EXISTS(SELECT * FROM notification_category where category_name='FRIEND_REQUEST');

INSERT INTO notification_category (category_name, category_description, created_at, updated_at)
SELECT 'FRIEND_ACCEPT', '친구 수락 관련', now(), now()
WHERE NOT EXISTS(SELECT * FROM notification_category where category_name='FRIEND_ACCEPT');

INSERT INTO notification_category (category_name, category_description, created_at, updated_at)
SELECT 'GROUP_INVITE', '그룹 초대 관련', now(), now()
WHERE NOT EXISTS(SELECT * FROM notification_category where category_name='GROUP_INVITE');