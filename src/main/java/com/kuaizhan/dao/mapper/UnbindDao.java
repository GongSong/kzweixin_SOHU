package com.kuaizhan.dao.mapper;


import com.kuaizhan.pojo.DO.UnbindDO;


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
    int insertUnbind(UnbindDO unbind);

    /**
     * 通过long型appid获取解绑信息
     *
     * @param appId
     * @return
     */
    UnbindDO getUnbindByWeixinAppId(Long appId);

    /**
     * 通过long型appid更新
     *
     * @param unbind
     * @return
     */
    int updateUnbindByWeixinAppId(UnbindDO unbind);
}
