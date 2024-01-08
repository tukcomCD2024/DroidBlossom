create table address (
    address_id   bigint auto_increment primary key,
    created_at   timestamp  null,
    updated_at   timestamp  null,
    village      varchar(255)  not null,
    city         varchar(255) not null,
    province     varchar(255) not null,
    sub_distinct varchar(255) not null,
    zip_code     varchar(255) null
);

create table `group` (
    created_at        timestamp  null,
    group_id          bigint auto_increment primary key,
    updated_at        timestamp  null,
    group_name        varchar(255) not null,
    group_description varchar(255) not null,
    group_profile_url varchar(255) not null
);

create table member (
    is_verified          bit          not null,
    notification_enabled bit          not null,
    created_at           timestamp  null,
    member_id            bigint auto_increment primary key,
    updated_at           timestamp  null,
    phone                varchar(20)  null,
    nickname             varchar(50)  not null,
    oauth2provider       varchar(50)  not null,
    email                varchar(255) null,
    fcm_token            varchar(255) null,
    profile_url          varchar(255) not null
);

create table capsule_skin (
    capsule_skin_id bigint auto_increment primary key,
    created_at      timestamp  null,
    member_id       bigint       not null,
    size            bigint       not null,
    updated_at      timestamp  null,
    skin_name       varchar(50)  not null,
    image_url       varchar(255) not null,
    motion_name     varchar(255) not null,
    constraint FK_capsule_skin_member foreign key (member_id) references member (member_id)
);

create table capsule (
    is_opened       bit          not null,
    latitude        float        not null,
    longitude       float        not null,
    address_id      bigint       not null,
    capsule_id      bigint auto_increment primary key,
    capsule_skin_id bigint       not null,
    created_at      timestamp  null,
    due_date        timestamp  null,
    group_id        bigint       not null,
    member_id       bigint       not null,
    updated_at      timestamp  null,
    content         varchar(255) not null,
    title           varchar(255) not null,
    type            varchar(255) not null,
    constraint FK_capsule_group foreign key (group_id) references `group` (group_id),
    constraint FK_capsule_member foreign key (member_id) references member (member_id),
    constraint FK_capsule_capsule_skin foreign key (capsule_skin_id) references capsule_skin (capsule_skin_id),
    constraint FK_capsule_address foreign key (address_id) references address (address_id)
);

create table friend_invite (
    created_at       timestamp null,
    friend_invite_id bigint auto_increment
        primary key,
    member_id        bigint      not null,
    updated_at       timestamp null,
    constraint FK_friend_invite_member foreign key (member_id) references member (member_id)
);

create table group_capsule_open (
    capsule_id            bigint      not null,
    created_at            timestamp null,
    group_capsule_open_id bigint auto_increment
        primary key,
    member_id             bigint      not null,
    updated_at            timestamp null,
    constraint FK_group_capsule_open_member foreign key (member_id) references member (member_id),
    constraint FK_group_capsule_open_capsule foreign key (capsule_id) references capsule (capsule_id)
);

create table group_invite (
    created_at      timestamp null,
    group_invite_id bigint auto_increment
        primary key,
    member_id       bigint      not null,
    updated_at      timestamp null,
    constraint FK_group_invite_member foreign key (member_id) references member (member_id)
);

create table history (
    created_at timestamp  null,
    history_id bigint auto_increment
        primary key,
    member_id  bigint       not null,
    updated_at timestamp  null,
    title      varchar(255) not null,
    constraint FK_history_member foreign key (member_id) references member (member_id)
);

create table image (
    capsule_id bigint       not null,
    created_at timestamp  null,
    image_id   bigint auto_increment
        primary key,
    size       bigint       not null,
    updated_at timestamp  null,
    image_name varchar(255) not null,
    image_url  varchar(255) not null,
    constraint FK_image_capsule foreign key (capsule_id) references capsule (capsule_id)
);

create table history_image (
    created_at       timestamp null,
    history_id       bigint      not null,
    history_image_id bigint auto_increment
        primary key,
    image_id         bigint      not null,
    updated_at       timestamp null,
    constraint FK_history_image_image foreign key (image_id) references image (image_id),
    constraint FK_history_image_history foreign key (history_id) references history (history_id)
);

create table member_friend (
    created_at       timestamp null,
    member_friend_id bigint auto_increment
        primary key,
    member_id        bigint      not null,
    updated_at       timestamp null,
    constraint FK_member_friend_member foreign key (member_id) references member (member_id)
);

create table member_group (
    is_owner        bit         not null,
    created_at      timestamp null,
    group_id        bigint      not null,
    member_group_id bigint auto_increment
        primary key,
    member_id       bigint      not null,
    updated_at      timestamp null,
    constraint FK_member_group_group foreign key (group_id) references `group` (group_id),
    constraint FK_member_group_member foreign key (member_id) references member (member_id)
);

create table video (
    size       int          not null,
    capsule_id bigint       not null,
    video_id   bigint auto_increment
        primary key,
    video_name varchar(255) not null,
    video_url  varchar(255) not null,
    constraint FK_video_capsule foreign key (capsule_id) references capsule (capsule_id)
);
