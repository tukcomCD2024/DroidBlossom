create table announcement (
    announcement_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    version VARCHAR(30),
    created_at timestamp    NULL,
    updated_at timestamp    NULL
)