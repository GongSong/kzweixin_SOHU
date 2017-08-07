package com.kuaizhan.kzweixin.dao.typehandler;

import com.kuaizhan.kzweixin.annotation.EnumTypeHandler;
import com.kuaizhan.kzweixin.enums.ResponseType;

import java.sql.Types;

/**
 * Created by zixiong on 2017/07/30.
 */
@EnumTypeHandler(target = ResponseType.class, jdbcType = Types.INTEGER)
public class ResponseTypeHandler extends BaseEnumTypeHandler {
}
