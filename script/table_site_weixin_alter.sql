ALTER TABLE site_weixin MODIFY site_id BIGINT(10) unsigned COMMENT '站点id';

ALTER TABLE site_weixin ADD user_id BIGINT(10) unsigned NOT NULL DEFAULT 0 COMMENT '主站用户id';

ALTER TABLE site_weixin ALTER COLUMN app_secret SET DEFAULT '';
ALTER TABLE site_weixin ALTER COLUMN qrcode_url_kz SET DEFAULT '';
ALTER TABLE site_weixin ALTER COLUMN preview_open_id SET DEFAULT '';
ALTER TABLE site_weixin ALTER COLUMN unbind_time SET DEFAULT 0;
