-- 외래 키 제약 조건 해제
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE member_friend;
TRUNCATE TABLE member;

-- 외래 키 제약 조건 활성화
SET FOREIGN_KEY_CHECKS = 1;