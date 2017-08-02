package com.kuaizhan.kzweixin.dao.typehandler;

import com.kuaizhan.kzweixin.annotation.EnumTypeHandler;
import com.kuaizhan.kzweixin.enums.ComponentResponseType;
import com.kuaizhan.kzweixin.utils.BaseEnumTypeHandler;

import java.sql.Types;

/**
 * Created by fangtianyu on 8/2/17.
 */
@EnumTypeHandler(target = ComponentResponseType.class, jdbcType = Types.INTEGER)
public class ComponentResponseTypeHandler extends BaseEnumTypeHandler {
}
