CREATE TABLE member_temporary
(
    member_temporary_id          INT AUTO_INCREMENT PRIMARY KEY,
    profile_url VARCHAR(255),
    nickname    VARCHAR(255),
    social_type VARCHAR(255),
    email       VARCHAR(255),
    is_verified BOOLEAN,
    auth_id     VARCHAR(255),
    tag         VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

alter table friend_invite drop column friend_status;