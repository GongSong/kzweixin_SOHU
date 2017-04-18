package com.kuaizhan.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zixiong on 2017/4/18.
 */
public class FileUtilTest {
    @Test
    public void download() throws Exception {
        FileUtil.download("http://10.10.120.180/g1/M00/01/99/CgoYvFj0ncuAJEtIAAB-RvpkxrU5155940", "pic.t1.com");
    }

}