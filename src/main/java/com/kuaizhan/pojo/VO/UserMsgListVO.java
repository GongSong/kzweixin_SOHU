package com.kuaizhan.pojo.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjiateng on 2017/3/18.
 */
public class UserMsgListVO {
    private Integer isExpire;
    private List<UserMsgVO> msgs = new ArrayList<>();

    public Integer getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(Integer isExpire) {
        this.isExpire = isExpire;
    }

    public List<UserMsgVO> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<UserMsgVO> msgs) {
        this.msgs = msgs;
    }

    @Override
    public String toString() {
        return "UserMsgListVO{" +
                "isExpire=" + isExpire +
                ", msgs=" + msgs +
                '}';
    }
}
