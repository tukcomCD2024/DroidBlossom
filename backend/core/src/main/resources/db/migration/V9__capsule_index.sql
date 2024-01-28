alter table capsule
    modify point point srid 3857 not null;

alter table capsule
    add spatial index `point_spatial_index` (`point`);