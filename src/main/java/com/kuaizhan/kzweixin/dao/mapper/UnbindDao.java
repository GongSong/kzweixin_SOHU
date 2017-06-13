package com.kuaizhan.kzweixin.dao.mapper;


import com.kuaizhan.kzweixin.pojo.po.UnbindPO;


/**
 * 解绑信息dao
 * Created by Mr.Jadyn on 2017/1/25.
 */
public interface UnbindDao {
    /**
     * 添加解绑信息
     *
     * @param unbind
     * @return
     */
    int insertUnbind(UnbindPO unbind);

    /**
     * 通过long型appid获取解绑信息
     *
     * @param appId
     * @return
     */
    UnbindPO getUnbindByWeixinAppId(Long appId);

    /**
     * 通过long型appid更新
     *
     * @param unbind
     * @return
     */
    int updateUnbindByWeixinAppId(UnbindPO unbind);
}
