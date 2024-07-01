alter table capsule add column deleted_at timestamp null default null;

alter table capsule_skin add column deleted_at timestamp null default null;

alter table friend_invite add column deleted_at timestamp null default null;

alter table `group` add column deleted_at timestamp null default null;

alter table group_capsule_open add column deleted_at timestamp null default null;

alter table group_invite add column deleted_at timestamp null default null;

alter table history add column deleted_at timestamp null default null;

alter table history_image add column deleted_at timestamp null default null;

alter table image add column deleted_at timestamp null default null;

alter table member add column deleted_at timestamp null default null;

alter table member_friend add column deleted_at timestamp null default null;

alter table member_group add column deleted_at timestamp null default null;

alter table member_temporary add column deleted_at timestamp null default null;

alter table notification add column deleted_at timestamp null default null;

alter table notification_category add column deleted_at timestamp null default null;

alter table video add column deleted_at timestamp null default null;
