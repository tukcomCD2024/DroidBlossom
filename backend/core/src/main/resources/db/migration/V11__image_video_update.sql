-- image 테이블에 member_id 컬럼 추가
ALTER TABLE image
ADD COLUMN member_id BIGINT;

-- video 테이블에 member_id 컬럼 추가
ALTER TABLE video
ADD COLUMN member_id BIGINT;

-- image 테이블의 member_id를 member 테이블의 기본 키와 외래키로 설정
ALTER TABLE image
ADD CONSTRAINT fk_image_member
FOREIGN KEY (member_id)
REFERENCES member(member_id);

-- video 테이블의 member_id를 member 테이블의 기본 키와 외래키로 설정
ALTER TABLE video
ADD CONSTRAINT fk_video_member
FOREIGN KEY (member_id)
REFERENCES member(member_id);