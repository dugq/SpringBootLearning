package com.dugq;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * @ClassName MongoPageHelper
 * @Description
 * @Author Su.sc
 * @Date 2022/4/14 11:37
 * 学而不思则罔 思而不学则殆
 */
@AllArgsConstructor
public class MongoPageHelper {

    @Autowired
    private MongoTemplate mongoTemplate;

    public <T> PageResult<T> pageQuery(Query query, Class<T> entityClass, Integer pageNum, Integer pageSize){
        //分页逻辑
        long total = mongoTemplate.count(query, entityClass);
        final Integer pages = (int) Math.ceil(total / (double) pageSize);

        final List<T> entityList = mongoTemplate
                .find(query.with(PageRequest.of(pageNum-1,pageSize)),entityClass);
        final PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setPages(pages);
        pageResult.setPageSize(pageSize);
        pageResult.setPageNum(pageNum);
        pageResult.setList(entityList);
        return pageResult;
    }

    public <T> PageResult<T> pageQuery(Query query, Class<T> entityClass,String collectionName, Integer pageNum, Integer pageSize){
        //分页逻辑
        long total = mongoTemplate.count(query, entityClass,collectionName);
        final Integer pages = (int) Math.ceil(total / (double) pageSize);

        final List<T> entityList = mongoTemplate
                .find(query.with(PageRequest.of(pageNum-1,pageSize)),entityClass,collectionName);
        final PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setPages(pages);
        pageResult.setPageSize(pageSize);
        pageResult.setPageNum(pageNum);
        pageResult.setList(entityList);
        return pageResult;
    }

}
