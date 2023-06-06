create table if not exists super_web.super_cache
(
    id           bigint auto_increment comment '主键'
        primary key,
    mach         text                               not null,
    second_word  text                               not null,
    content      text                               not null,
    created_time datetime default CURRENT_TIMESTAMP not null,
    update_time  datetime default CURRENT_TIMESTAMP not null,
    del          tinyint  default 0                 not null
);

create table if not exists super_web.super_code
(
    id           bigint auto_increment
        primary key,
    code         char(8)                            not null,
    frequency    tinyint                            not null,
    created_time datetime default CURRENT_TIMESTAMP not null,
    update_time  datetime default CURRENT_TIMESTAMP not null,
    del          tinyint  default 0                 not null
);

create table if not exists super_web.super_creation
(
    id           bigint auto_increment
        primary key,
    user_id      bigint                             not null,
    prompt       text                               not null,
    url          text                               not null,
    recommend    tinyint  default 0                 not null,
    created_time datetime default CURRENT_TIMESTAMP not null,
    update_time  datetime default CURRENT_TIMESTAMP not null,
    del          tinyint  default 0                 not null
);

create table if not exists super_web.super_favorite
(
    id            bigint auto_increment
        primary key,
    user_dialogue text                               not null,
    ai_dialogue   text                               not null,
    user_id       bigint                             not null,
    created_time  datetime default CURRENT_TIMESTAMP not null,
    update_time   datetime default CURRENT_TIMESTAMP not null,
    del           tinyint  default 0                 not null
);

create table if not exists super_web.super_operate
(
    id                   tinyint                            not null,
    alipay_callback_url  varchar(255)                       not null comment '回调地址',
    alipay_public        longblob                           not null comment '支付宝公钥',
    alipay_private       longblob                           not null comment '支付宝私钥',
    alipay_appid         varchar(100)                       not null comment '支付宝应用ID',
    bing_frequency       bigint                             not null comment 'bing 使用次数',
    chat_three_frequency bigint                             not null comment 'gpt 3.5使用次数',
    chat_four_frequency  bigint                             not null comment 'gpt 4 使用次数',
    mapping_frequency    bigint                             not null comment '绘图 使用次数',
    memory               bigint                             not null comment 'GPT 上下文记忆数量',
    user_frequency       bigint                             not null comment '新用户注册赠送量',
    created_time         datetime default CURRENT_TIMESTAMP not null,
    update_time          datetime default CURRENT_TIMESTAMP not null,
    del                  tinyint  default 0                 not null
);

create table if not exists super_web.super_order
(
    id             varchar(30)                        not null
        primary key,
    product_id     bigint                             null,
    product_name   varchar(100)                       not null,
    product_price  double                             not null,
    user_id        bigint                             not null,
    frequency      bigint                             not null,
    state          tinyint  default 0                 null comment '0 待支付 1已支付 3 已回收',
    pay_time       datetime                           null,
    reason_failure varchar(50)                        null,
    created_time   datetime default CURRENT_TIMESTAMP not null,
    update_time    datetime default CURRENT_TIMESTAMP not null,
    del            tinyint  default 0                 not null,
    constraint id
        unique (id)
);

create table if not exists super_web.super_product
(
    id           bigint auto_increment
        primary key,
    name         varchar(100)                       not null,
    frequency    bigint                             not null,
    price        double                             not null,
    created_time datetime default CURRENT_TIMESTAMP not null,
    update_time  datetime default CURRENT_TIMESTAMP not null,
    del          tinyint  default 0                 not null
);

create table if not exists super_web.super_user
(
    id           bigint                             null,
    email        varchar(30)                        not null,
    password     varchar(255)                        not null,
    frequency    bigint                             not null,
    created_time datetime                           null,
    update_time  datetime default CURRENT_TIMESTAMP not null,
    del          tinyint  default 0                 not null
);

