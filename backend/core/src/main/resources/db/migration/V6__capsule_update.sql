-- -- change capsule Geography data type
ALTER TABLE capsule
    DROP
        COLUMN longitude,
    DROP
        COLUMN latitude;


ALTER TABLE capsule
    ADD COLUMN point POINT SRID 3857;



