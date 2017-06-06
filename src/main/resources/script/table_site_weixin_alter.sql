ALTER TABLE site_weixin MODIFY site_id BIGINT(10) unsigned COMMENT '站点id';
ALTER TABLE site_weixin ADD user_id BIGINT(10) unsigned NOT NULL DEFAULT 0 COMMENT '主站用户id';
