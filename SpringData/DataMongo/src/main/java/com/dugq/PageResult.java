package com.dugq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * @ClassName PageResult
 * @Description
 * @Author Su.sc
 * @Date 2022/4/14 11:39
 * 学而不思则罔 思而不学则殆
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {

    /**
     * 页码，从1开始
     */
    private Integer pageNum;

    /**
     * 页面大小
     */
    private Integer pageSize;


    /**
     * 总数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 数据
     */
    private List<T> list;

}
