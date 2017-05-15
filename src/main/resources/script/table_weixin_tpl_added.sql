CREATE TABLE weixin_tpl_added
(
  id INT(10) unsigned NOT NULL auto_increment PRIMARY KEY comment '自增主键',
	weixin_appid bigint(10) unsigned NOT NULL comment '微信插件唯一标志',
	template_id_short VARCHAR(32) NOT NULL comment '模板id的编号',
	template_id VARCHAR(64) NOT NULL comment '对应的用户模板id',
	create_time INT(10) unsigned NOT NULL comment '新建时间',
	update_time INT(10) unsigned NOT NULL comment '更新时间',

	CONSTRAINT UNIQUE_WEIXIN_APPID_TEMPLATE_SHORT UNIQUE (weixin_appid, template_id_short)
);

