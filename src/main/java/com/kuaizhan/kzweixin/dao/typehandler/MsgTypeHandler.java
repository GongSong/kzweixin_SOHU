package com.kuaizhan.kzweixin.dao.typehandler;

import com.kuaizhan.kzweixin.annotation.EnumTypeHandler;
import com.kuaizhan.kzweixin.enums.MsgType;

import java.sql.Types;

/**
 * Created by zixiong on 2017/08/03.
 */
@EnumTypeHandler(target = MsgType.class, jdbcType = Types.INTEGER)
public class MsgTypeHandler extends BaseEnumTypeHandler{
}
