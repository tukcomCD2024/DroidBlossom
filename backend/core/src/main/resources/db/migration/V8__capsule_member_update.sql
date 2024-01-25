-- 외래 키 제약 조건 삭제
ALTER TABLE capsule DROP FOREIGN KEY fk_capsule_group_id;

-- group_id를 NULL로 변경
alter table capsule
    modify group_id bigint null;

alter table member
    drop constraint UK_EMAIL;
alter table member
    drop constraint UK_PHONE;
alter table member
    drop constraint UK_NICKNAME;

-- 캡슐 phone_hash 속성 추가
ALTER TABLE member
    ADD COLUMN phone_hash VARBINARY(255) UNIQUE;
