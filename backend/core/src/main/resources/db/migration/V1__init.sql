CREATE TABLE `group`
(
    created_at        timestamp    NULL,
    group_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    updated_at        timestamp    NULL,
    group_name        VARCHAR(255) NOT NULL,
    group_description VARCHAR(255) NOT NULL,
    group_profile_url VARCHAR(255) NOT NULL
);

CREATE TABLE member
(
    is_verified          BIT          NOT NULL,
    notification_enabled BIT          NOT NULL,
    created_at           timestamp    NULL,
    member_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    updated_at           timestamp    NULL,
    phone                VARCHAR(255) NULL,
    nickname             VARCHAR(255) NOT NULL,
    oauth2_provider      VARCHAR(255) NOT NULL,
    email                VARCHAR(255) NULL,
    fcm_token            VARCHAR(255) NULL,
    profile_url          VARCHAR(255) NOT NULL
);

CREATE TABLE capsule_skin
(
    capsule_skin_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at      timestamp    NULL,
    member_id       BIGINT       NOT NULL,
    size            BIGINT       NOT NULL,
    updated_at      timestamp    NULL,
    skin_name       VARCHAR(255) NOT NULL,
    image_url       VARCHAR(255) NOT NULL,
    motion_name     VARCHAR(255) NOT NULL,
    CONSTRAINT fk_capsule_skin_member_id
        FOREIGN KEY (member_id) REFERENCES member (member_id)
);

CREATE TABLE capsule
(
    is_opened       BIT          NOT NULL,
    latitude        FLOAT        NOT NULL,
    longitude       FLOAT        NOT NULL,
    capsule_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    capsule_skin_id BIGINT       NOT NULL,
    created_at      timestamp    NULL,
    due_date        timestamp    NULL,
    group_id        BIGINT       NOT NULL,
    member_id       BIGINT       NOT NULL,
    updated_at      timestamp    NULL,
    village         VARCHAR(255) NOT NULL,
    city            VARCHAR(255) NOT NULL,
    province        VARCHAR(255) NOT NULL,
    sub_district    VARCHAR(255) NOT NULL,
    content         VARCHAR(255) NOT NULL,
    title           VARCHAR(255) NOT NULL,
    type            VARCHAR(255) NOT NULL,
    zip_code        VARCHAR(255) NULL,
    CONSTRAINT fk_capsule_group_id
        FOREIGN KEY (group_id) REFERENCES `group` (group_id),
    CONSTRAINT fk_capsule_member_id
        FOREIGN KEY (member_id) REFERENCES member (member_id),
    CONSTRAINT fk_capsule_capsule_skin_id
        FOREIGN KEY (capsule_skin_id) REFERENCES capsule_skin (capsule_skin_id)
);

CREATE TABLE friend_invite
(
    created_at       timestamp NULL,
    friend_invite_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id        BIGINT    NOT NULL,
    updated_at       timestamp NULL,
    CONSTRAINT fk_friend_invite_member_id
        FOREIGN KEY (member_id) REFERENCES member (member_id)
);

CREATE TABLE group_capsule_open
(
    capsule_id            BIGINT    NOT NULL,
    created_at            timestamp NULL,
    group_capsule_open_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id             BIGINT    NOT NULL,
    updated_at            timestamp NULL,
    CONSTRAINT fk_group_capsule_open_member_id
        FOREIGN KEY (member_id) REFERENCES member (member_id),
    CONSTRAINT fk_group_capsule_open_capsule_id
        FOREIGN KEY (capsule_id) REFERENCES capsule (capsule_id)
);

CREATE TABLE group_invite
(
    created_at      timestamp NULL,
    group_invite_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id       BIGINT    NOT NULL,
    updated_at      timestamp NULL,
    CONSTRAINT fk_group_invite_member_id
        FOREIGN KEY (member_id) REFERENCES member (member_id)
);

CREATE TABLE history
(
    created_at timestamp    NULL,
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT       NOT NULL,
    updated_at timestamp    NULL,
    title      VARCHAR(255) NOT NULL,
    CONSTRAINT fk_history_member_id
        FOREIGN KEY (member_id) REFERENCES member (member_id)
);

CREATE TABLE image
(
    capsule_id BIGINT       NOT NULL,
    created_at timestamp    NULL,
    image_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    size       BIGINT       NOT NULL,
    updated_at timestamp    NULL,
    image_name VARCHAR(255) NOT NULL,
    image_url  VARCHAR(255) NOT NULL,
    CONSTRAINT fk_image_capsule_id
        FOREIGN KEY (capsule_id) REFERENCES capsule (capsule_id)
);

CREATE TABLE history_image
(
    created_at       timestamp NULL,
    history_id       BIGINT    NOT NULL,
    history_image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_id         BIGINT    NOT NULL,
    updated_at       timestamp NULL,
    CONSTRAINT fk_history_image_image_id
        FOREIGN KEY (image_id) REFERENCES image (image_id),
    CONSTRAINT fk_history_image_history_id
        FOREIGN KEY (history_id) REFERENCES history (history_id)
);

CREATE TABLE member_friend
(
    created_at       timestamp NULL,
    member_friend_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id        BIGINT    NOT NULL,
    updated_at       timestamp NULL,
    CONSTRAINT fk_member_friend_member_id
        FOREIGN KEY (member_id) REFERENCES member (member_id)
);

CREATE TABLE member_group
(
    is_owner        BIT       NOT NULL,
    created_at      timestamp NULL,
    group_id        BIGINT    NOT NULL,
    member_group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id       BIGINT    NOT NULL,
    updated_at      timestamp NULL,
    CONSTRAINT fk_member_group_group_id
        FOREIGN KEY (group_id) REFERENCES `group` (group_id),
    CONSTRAINT fk_member_group_member_id
        FOREIGN KEY (member_id) REFERENCES member (member_id)
);

CREATE TABLE video
(
    size       INT          NOT NULL,
    capsule_id BIGINT       NOT NULL,
    video_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    video_name VARCHAR(255) NOT NULL,
    video_url  VARCHAR(255) NOT NULL,
    CONSTRAINT fk_video_capsule_id
        FOREIGN KEY (capsule_id) REFERENCES capsule (capsule_id)
);
