-- Add new columns
ALTER TABLE capsule ADD COLUMN full_road_address_name VARCHAR(255);
ALTER TABLE capsule ADD COLUMN road_name VARCHAR(255);
ALTER TABLE capsule ADD COLUMN main_building_number VARCHAR(255);
ALTER TABLE capsule ADD COLUMN sub_building_number VARCHAR(255);
ALTER TABLE capsule ADD COLUMN building_name VARCHAR(255);

-- Drop old columns
ALTER TABLE capsule DROP COLUMN village;

alter table capsule
    modify city varchar(255) null;

alter table capsule
    modify province varchar(255) null;

alter table capsule
    modify sub_district varchar(255) null;



-- drop video, image columns
ALTER TABLE video
    DROP
        COLUMN size,
    DROP
        COLUMN video_name;

ALTER TABLE image
    DROP
        COLUMN size,
    DROP
        COLUMN image_name;