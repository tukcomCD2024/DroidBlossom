-- 외래 키 제약 조건 삭제
ALTER TABLE capsule DROP FOREIGN KEY fk_capsule_group_id;

-- group_id를 NULL로 변경
alter table capsule
    modify group_id bigint null;

