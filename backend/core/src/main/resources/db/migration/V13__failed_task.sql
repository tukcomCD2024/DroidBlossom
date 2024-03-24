CREATE TABLE failed_task
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255),
    full_name       VARCHAR(255),
    args            TEXT,
    kwargs          TEXT,
    exception_class TEXT,
    exception_msg   TEXT,
    traceback       TEXT,
    celery_task_id  VARCHAR(255),
    failures        INT,

    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);