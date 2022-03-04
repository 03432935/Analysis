package com.analysis.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/23 15:02
 */
public class Base<T> extends PageInfo<T>{

    private int del = 0;

    private Date createTime = new Date();

    public int getDel() {
        return del;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime == null ? new Date() : createTime;
    }

    public void setDel(int del) throws Exception {
        if (del != 0 && del != 1){
            throw new Exception("数据状态出错");
        }
        this.del = del;
    }

    @Override
    public String toString() {
        return "Base{" +
                "del=" + del +
                ", createTime=" + createTime +
                '}';
    }

}
