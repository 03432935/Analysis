package com.analysis.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/1/19 20:03
 */
@Slf4j
public class PageUtils implements Serializable {
//
//    public static<T> PageInfo<T> pageList(List<T> list, Integer pageNum, Integer pageSize){
//        // 开启分页
//        PageHelper.startPage(pageNum, pageSize);
//        // 封装list到PageInfo对象中自动分页
//        PageInfo<T> poPageInfo = new PageInfo<>(list);
//        log.info("开启分页");
//        return poPageInfo;
//    }
//
//    private static final long serialVersionUID = 1L;
//    /**
//     * 总记录数
//     */
//    private int totalCount;
//    /**
//     * 每页记录数
//     */
//    private int pageSize;
//    /**
//     * 总页数
//     */
//    private int totalPage;
//    /**
//     * 当前页数
//     */
//    private int currPage;
//    /**
//     * 列表数据
//     */
//    private List<?> list;
//
//    /**
//     * 分页
//     *
//     * @param list       列表数据
//     * @param totalCount 总记录数
//     * @param pageSize   每页记录数
//     * @param currPage   当前页数
//     */
//    public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
//        this.list = list;
//        this.totalCount = totalCount;
//        this.pageSize = pageSize;
//        this.currPage = currPage;
//        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
//    }
//
//    /**
//     * 分页
//     */
//    public PageUtils(IPage page) {
//        this.list = page.getRecords();
//        this.totalCount = (int) page.getTotal();
//        this.pageSize = (int) page.getSize();
//        this.currPage = (int) page.getCurrent();
//        this.totalPage = (int) page.getPages();
//    }
//
//    public int getTotalCount() {
//        return totalCount;
//    }
//
//    public void setTotalCount(int totalCount) {
//        this.totalCount = totalCount;
//    }
//
//    public int getPageSize() {
//        return pageSize;
//    }
//
//    public void setPageSize(int pageSize) {
//        this.pageSize = pageSize;
//    }
//
//    public int getTotalPage() {
//        return totalPage;
//    }
//
//    public void setTotalPage(int totalPage) {
//        this.totalPage = totalPage;
//    }
//
//    public int getCurrPage() {
//        return currPage;
//    }
//
//    public void setCurrPage(int currPage) {
//        this.currPage = currPage;
//    }
//
//    public List<?> getList() {
//        return list;
//    }
//
//    public void setList(List<?> list) {
//        this.list = list;
//    }


}
