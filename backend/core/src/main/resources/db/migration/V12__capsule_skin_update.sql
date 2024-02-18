ALTER TABLE capsule_skin DROP COLUMN size;

-- add retarget
alter table capsule_skin add column retarget varchar(255);

ALTER TABLE capsule_skin
    MODIFY motion_name VARCHAR(255) null;