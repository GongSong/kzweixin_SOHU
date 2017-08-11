package com.kuaizhan.kzweixin.dao.typehandler;

import com.kuaizhan.kzweixin.annotation.EnumTypeHandler;
import com.kuaizhan.kzweixin.enums.WxResponseType;

import java.sql.Types;

/**
 * Created by zixiong on 2017/07/30.
 */
@EnumTypeHandler(target = WxResponseType.class, jdbcType = Types.INTEGER)
public class WxResponseTypeHandler extends BaseEnumTypeHandler {
}
