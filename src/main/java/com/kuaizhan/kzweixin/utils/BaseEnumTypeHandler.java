package com.kuaizhan.kzweixin.utils;

import com.kuaizhan.kzweixin.annotation.EnumTypeHandler;
import com.kuaizhan.kzweixin.enums.BaseEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/07/30.
 */
public class BaseEnumTypeHandler extends BaseTypeHandler {

    private static final Logger logger = LoggerFactory.getLogger(BaseEnumTypeHandler.class);

    private Map<Integer, Enum> enumMap = new HashMap<>();

    public BaseEnumTypeHandler() {
        if (! this.getClass().isAnnotationPresent(EnumTypeHandler.class)) {
            String msg = "[BaseEnumTypeHandler]" + this.getClass() + " must add EnumTypeHandler annotation";
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        EnumTypeHandler annotation = this.getClass().getAnnotation(EnumTypeHandler.class);
        Class targetEnum = annotation.target();
        try {
            for (Object o: targetEnum.getEnumConstants()) {
                Enum e = (Enum) o;
                BaseEnum baseEnum = (BaseEnum) e;
                enumMap.put(baseEnum.getCode(), e);
            }
        } catch (ClassCastException e) {
            String msg = "[BaseEnumTypeHandler]" + targetEnum.getName() + " must be enum and implements BaseEnum";
            logger.error(msg);
            throw new RuntimeException(msg);
        }

    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        BaseEnum baseEnum = (BaseEnum) o;
        preparedStatement.setObject(i, baseEnum.getCode());
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return enumMap.get(resultSet.getInt(s));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return enumMap.get(resultSet.getInt(i));
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return enumMap.get(callableStatement.getInt(i));
    }
}
