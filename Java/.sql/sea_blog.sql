create table if not exists sea_time.sea_blog
(
    sea_blog_id       bigint auto_increment
        primary key,
    sea_user_id       bigint                             not null comment '所属用户',
    sea_resource_id   bigint                             not null comment '文章封面 (资源表ID)',
    sea_classify_id   bigint                             not null comment '所属专题（专题ID）',
    title             varchar(100)                       not null comment '文章标题',
    label             varchar(100)                       not null comment '文章标签',
    summary           text                               not null comment '摘要',
    content           text                               not null comment '内容',
    file_resource_ids json                               null comment '文章内容所使用的资源ID (有可能没有)',
    is_recommend      tinyint  default 0                 not null comment '是否为推荐文章',
    created_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time       datetime default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '文章表';

create fulltext index idx_fulltext
    on sea_time.sea_blog (title, label, summary);

create index idx_is_recommend
    on sea_time.sea_blog (is_recommend);

create index idx_sea_classify_id
    on sea_time.sea_blog (sea_classify_id);

create index idx_sea_resource_id
    on sea_time.sea_blog (sea_resource_id);

create index idx_sea_user_id
    on sea_time.sea_blog (sea_user_id);

create table if not exists sea_time.sea_classify
(
    sea_classify_id bigint auto_increment comment '主键'
        primary key,
    classify_name   varchar(100)                       not null comment ' 分类名称',
    sea_resource_id bigint                             not null comment ' 图片路径',
    is_type         tinyint                            null comment '0 前端 1 后端 2. 中间件 3.杂项',
    created_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP not null comment '修改时间'
);

create index sea_classify_is_type_index
    on sea_time.sea_classify (is_type);

create index sea_classify_sea_resource_id_index
    on sea_time.sea_classify (sea_resource_id);

create table if not exists sea_time.sea_comment
(
    sea_comment_id  bigint auto_increment
        primary key,
    comment_content text                               not null comment '评论内容',
    sea_blog_id     bigint                             not null,
    sea_user_id     bigint                             null comment '评论人ID',
    created_time    datetime default CURRENT_TIMESTAMP not null comment '评论时间',
    update_time     datetime default CURRENT_TIMESTAMP null
);

create table if not exists sea_time.sea_image
(
    sea_image_id bigint auto_increment comment '主键'
        primary key,
    sea_user_id  bigint                             not null comment '所属用户',
    prompt       varchar(200)                       not null comment '关键词',
    original_id  bigint                             null comment '原图（关联资源ID）',
    generate_id  bigint                             null comment '新图',
    is_public    tinyint  default 0                 not null comment '是否公开 0 不公开 1公开',
    created_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP not null comment '修改时间'
);

create table if not exists sea_time.sea_reply
(
    sea_reply_id   bigint auto_increment comment '主键'
        primary key,
    sea_comment_id bigint                             not null comment '所属评论',
    reciprocity_id bigint                             null comment '回复人',
    sea_user_id    bigint                             not null comment '所属用户',
    reply_content  text                               not null comment '回复内容',
    created_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null comment '修改时间'
);

create index sea_reply_reciprocity_id_index
    on sea_time.sea_reply (reciprocity_id);

create index sea_reply_sea_comment_id_index
    on sea_time.sea_reply (sea_comment_id);

create index sea_reply_sea_user_id_index
    on sea_time.sea_reply (sea_user_id);

create table if not exists sea_time.sea_resource
(
    sea_resource_id bigint auto_increment comment '主键'
        primary key,
    target          varchar(200)                                 not null comment '物理位置',
    uri             varchar(200)                                 not null comment '访问链接位置',
    type            enum ('COVER', 'BLOG', 'AVATAR', 'PAINTING') not null comment '文件夹 ? 文件类型',
    created_time    datetime default (now())                     not null comment '创建时间',
    update_time     datetime default (now())                     not null comment '修改时间'
);

create index sea_resource_type_index
    on sea_time.sea_resource (type);

create table if not exists sea_time.sea_user
(
    sea_user_id     bigint                             not null comment '主键'
        primary key,
    sea_resource_id bigint                             null comment '用户头像管理ID',
    open_id         varchar(180)                       not null comment '微信用户标识',
    user_name       varchar(100)                       null comment '用户微信昵称',
    created_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP not null comment '修改时间'
);

create index sea_user_sea_resource_id_index
    on sea_time.sea_user (sea_resource_id);

