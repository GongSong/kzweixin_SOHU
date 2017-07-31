package com.kuaizhan.kzweixin.dao.typehandler;

import com.kuaizhan.kzweixin.annotation.EnumTypeHandler;
import com.kuaizhan.kzweixin.enums.BizCode;
import com.kuaizhan.kzweixin.utils.BaseEnumTypeHandler;

import java.sql.Types;

/**
 * Created by zixiong on 2017/07/31.
 */
@EnumTypeHandler(target = BizCode.class, jdbcType = Types.INTEGER)
public class BizCodeTypeHandler extends BaseEnumTypeHandler {
}
