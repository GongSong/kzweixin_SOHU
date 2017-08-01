package com.kuaizhan.kzweixin.utils;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParamUtilTest {
    @Test
    public void getIntList() throws Exception {
        List<Integer> intList = ImmutableList.of(1,2,3);

        String listStr = JsonUtil.bean2String(intList);

        assertEquals(intList, ParamUtil.getIntList(listStr));

        listStr = "1,2,3";
        assertEquals(intList, ParamUtil.getIntList(listStr));
    }

}