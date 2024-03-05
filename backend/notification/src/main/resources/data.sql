INSERT INTO notification_category (category_name, category_description, created_at, updated_at)
SELECT 'CAPSULE_SKIN', '캡슐 스킨 생성 관련', now(), now()
WHERE NOT EXISTS(SELECT * FROM notification_category where category_name='CAPSULE_SKIN');
