package com.kuaizhan.kzweixin.dao.typehandler;

import com.kuaizhan.kzweixin.annotation.EnumTypeHandler;
import com.kuaizhan.kzweixin.enums.ResponseType;

import java.sql.Types;

/**
 * Created by fangtianyu on 8/2/17.
 */
@EnumTypeHandler(target = ResponseType.class, jdbcType = Types.INTEGER)
public class ResponseTypeHandler extends BaseEnumTypeHandler {
}
