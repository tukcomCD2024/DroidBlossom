-- drop size email
ALTER TABLE member DROP KEY unique_email;

ALTER TABLE member_temporary
    MODIFY email VARBINARY(255);

ALTER TABLE member
    MODIFY email VARBINARY(255);

ALTER TABLE member_temporary
    ADD email_hash VARBINARY(255);

ALTER TABLE member
    ADD email_hash VARBINARY(255);

ALTER TABLE member ADD CONSTRAINT unique_email_hash UNIQUE (email_hash);