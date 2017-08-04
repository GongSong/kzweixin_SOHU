package com.kuaizhan.kzweixin.dao.typehandler;

import com.kuaizhan.kzweixin.annotation.EnumTypeHandler;
import com.kuaizhan.kzweixin.enums.MsgSendType;
import com.kuaizhan.kzweixin.utils.BaseEnumTypeHandler;

import java.sql.Types;

/**
 * Created by zixiong on 2017/08/03.
 */
@EnumTypeHandler(target = MsgSendType.class, jdbcType = Types.INTEGER)
public class MsgSendTypeHandler extends BaseEnumTypeHandler{
}
