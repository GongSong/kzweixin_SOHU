CREATE TABLE weixin_action
(
  id INT(10) unsigned NOT NULL auto_increment PRIMARY KEY COMMENT '自增主键',
  weixin_appid bigint(10) unsigned NOT NULL COMMENT '微信插件唯一标志',
  biz_code VARCHAR(16) NOT NULL COMMENT '业务编码',
  biz_data VARCHAR(100) DEFAULT '' COMMENT '业务数据',
  action_type TINYINT(1) NOT NULL COMMENT '动作类型',
  response_type TINYINT(1) NOT NULL COMMENT '回复类型',
  response_json  TEXT NOT NULL COMMENT '回复的json数据',
  status TINYINT(1) NOT NULL COMMENT '启用状态',
  create_time INT(10) unsigned NOT NULL COMMENT '新建时间',
  update_time INT(10) unsigned NOT NULL COMMENT '更新时间'
);

CREATE INDEX IDX_WEIXIN_APPID_ACTION_TYPE_STATUS ON weixin_action (weixin_appid, action_type, status);


