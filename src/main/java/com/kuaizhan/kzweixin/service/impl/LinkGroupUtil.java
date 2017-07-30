package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.entity.LinkGroup.LinkGroup;
import com.kuaizhan.kzweixin.enums.LinkType;

/**
 * 链接组工具类
 * 此类太业务相关，主要提供给service层，故放在此处
 * Created by zixiong on 2017/07/30.
 */
public class LinkGroupUtil {

    /**
     * 入库前加上link_res_type等字段，兼容php数据
     */
    public static LinkGroup formatForDB(LinkGroup linkGroup) {
        linkGroup.setLinkResType(1);  // 都是链接类型
        linkGroup.setLinkResType(0);
        linkGroup.setLinkResName(linkGroup.getUrl()); // name是url
        return linkGroup;
    }

    /**
     * 返回给前端前，把php老数据转换为url类型
     */
    public static LinkGroup formatForView(LinkGroup linkGroup) {
        // 为null说明是老数据
        if (linkGroup.getLinkType() == null) {
            // 全部设置为url
            linkGroup.setLinkType(LinkType.URL);

            // 清空不返回
            linkGroup.setLinkResId(null);
            linkGroup.setLinkResType(null);
            linkGroup.setLinkResName(null);
        }
        return linkGroup;
    }
}
