alter table capsule add column is_deleted BOOLEAN;

alter table capsule_skin add column is_deleted BOOLEAN;

alter table friend_invite add column is_deleted BOOLEAN;

alter table `group` add column is_deleted BOOLEAN;

alter table group_capsule_open add column is_deleted BOOLEAN;

alter table group_invite add column is_deleted BOOLEAN;

alter table history add column is_deleted BOOLEAN;

alter table history_image add column is_deleted BOOLEAN;

alter table image add column is_deleted BOOLEAN;

alter table member add column is_deleted BOOLEAN;

alter table member_friend add column is_deleted BOOLEAN;

alter table member_group add column is_deleted BOOLEAN;

alter table member_temporary add column is_deleted BOOLEAN;

alter table notification add column is_deleted BOOLEAN;

alter table notification_category add column is_deleted BOOLEAN;

alter table video add column is_deleted BOOLEAN;
