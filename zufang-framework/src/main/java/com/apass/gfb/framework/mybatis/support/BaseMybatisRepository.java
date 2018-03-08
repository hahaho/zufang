package com.apass.gfb.framework.mybatis.support;

import com.apass.gfb.framework.mybatis.page.Page;
import com.apass.gfb.framework.mybatis.page.Pagination;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mybatis 操作基础类， 封装常用的数据库访问方法
 * 
 * <pre>
 *  新的操作方法：
 *    用Class代替原來的Repository Interface, 继承 BaseMybatisRepository
 *    
 *    如： 
 *    &#64;MyBatisRepository
 *    public class TestRepository extends BaseMybatisRepository<DO, Long>{
 *    
 *    }
 * </pre>
 * 
 * @author lixining
 * @version $Id: BaseRepository.java, v 0.1 2015年4月9日 下午3:48:59 lixining Exp $
 */
public class BaseMybatisRepository<T, ID extends Serializable> extends SqlSessionDaoSupport {

    /**
     * 类名
     */
    protected String entityClassName;

    /**
     * 类Class
     */
    private Class<?> entityClass;

    /**
     * 插入
     */
    private String insert;

    /**
     * 查询
     */
    private String select;

    private String selectByPK;
    /**
     * 计数
     */
    private String count;

    /**
     * 更新
     */
    private String update;

    /**
     * 更新所有属性
     */
    private String updateAll;

    /**
     * 删除
     */
    private String delete;

    /**
     * @see org.springframework.dao.support.DaoSupport#initDao()
     */
    @Override
    public void initDao() throws Exception {
        Type type = super.getClass().getGenericSuperclass();
        if (type != null && type instanceof ParameterizedType) {
            ParameterizedType parameterType = (ParameterizedType) type;
            this.entityClass = (Class<?>) parameterType.getActualTypeArguments()[0];
        }
        if (this.entityClass != null) {
            this.entityClassName = entityClass.getSimpleName();
        }
        this.insert = MessageFormat.format("{0}.insert", entityClassName);
        this.select = MessageFormat.format("{0}.select", entityClassName);
        this.selectByPK = MessageFormat.format("{0}.selectByPK", entityClassName);
        this.count = MessageFormat.format("{0}.count", entityClassName);
        this.delete = MessageFormat.format("{0}.delete", entityClassName);
        this.update = MessageFormat.format("{0}.update", entityClassName);
        this.updateAll = MessageFormat.format("{0}.updateAll", entityClassName);
    }

    /**
     * 
     * @see SqlSessionDaoSupport#setSqlSessionFactory(SqlSessionFactory)
     */
    @Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    /**
     * Insert
     */
    public Integer insert(T domain) {
        return getSqlSession().insert(this.insert, domain);
    }

    /**
     * delete
     */
    public Integer delete(ID id) {
        return getSqlSession().delete(this.delete, id);
    }

    /**
     * select
     */
    public T select(ID id) {
        return getSqlSession().selectOne(this.selectByPK, id);
    }

    /**
     * update
     */
    public Integer update(T domain) {
        return getSqlSession().update(this.update, domain);
    }

    /**
     * updateBymap
     */
    public Integer updateBymap(Map map, String sqlkey) {
        return getSqlSession().update(sqlkey, map);
    }

    /**
     * updateAll
     */
    public Integer updateAll(T domain) {
        return getSqlSession().update(this.updateAll, domain);
    }

    /**
     * filter
     */
    public List<T> filter(T domain) {
        return getSqlSession().selectList(this.select, domain);
    }

    /**
     * 分页查询
     * 
     * @param domain 查询条件
     * @param page 分页信息
     * @return
     */
    public Pagination<T> page(T domain, Page page) {
        Integer offSet = (page.getPage() - 1) * page.getLimit();
        Integer maxRow = page.getLimit();
        List<T> dataList = getSqlSession().selectList(this.select, domain);

        Pagination<T> pageResult = new Pagination<T>();
        pageResult.setTotalCount(count(domain));
        pageResult.setDataList(dataList);
        return pageResult;
    }

    /**
     * 分页查询
     * 
     * @param domain 查询条件
     * @param pageNo 查询页码
     * @param pageSize 查询页大小
     * @return
     */
    public Pagination<T> page(T domain, Integer pageNo, Integer pageSize) {
        Integer offSet = (pageNo - 1) * pageSize;
        Integer maxRow = pageSize;
        List<T> dataList = getSqlSession().selectList(this.select, domain, new RowBounds(offSet, maxRow));

        Pagination<T> pageResult = new Pagination<T>();
        pageResult.setTotalCount(count(domain));
        pageResult.setDataList(dataList);
        return pageResult;
    }

    /**
     * 分页查询
     * 
     * @param domain 查询条件
     * @param page 分页信息
     * @param sqlKey 执行sql的id
     * @return
     */
    public Pagination<T> pageBykey(T domain, Page page, String sqlKey) {
        Integer offSet = (page.getPage() - 1) * page.getLimit();
        Integer maxRow = page.getLimit();
        List<T> dataList = getSqlSession().selectList(sqlKey, domain, new RowBounds(offSet, maxRow));

        Pagination<T> pageResult = new Pagination<T>();
        pageResult.setTotalCount(countByKey(domain, sqlKey));
        pageResult.setDataList(dataList);
        return pageResult;
    }

    /**
     * 分页查询
     * 
     * @param map 查询条件
     * @param page 分页信息
     * @param sqlKey 执行sql的id
     * @return
     */
    public Pagination<T> page(Map map, Page page, String sqlKey) {

        Pagination<T> pageResult = new Pagination<T>();
        List<T> dataList = new ArrayList<T>();
        if (page.getPage() != null && page.getLimit() != null) {
            Integer offSet = (page.getPage() - 1) * page.getLimit();
            Integer maxRow = page.getLimit();
            dataList = getSqlSession().selectList(sqlKey, map, new RowBounds(offSet, maxRow));
        } else {
            dataList = getSqlSession().selectList(sqlKey, map);

        }
        // 总记录数
        pageResult.setTotalCount(countByMap(map, sqlKey));
        pageResult.setDataList(dataList);
        return pageResult;
    }

    /**
     * countByKey
     */
    public Integer countByKey(T domain, String sqlKey) {
        return getSqlSession().selectOne(sqlKey + "Count", domain);
    }

    /**
     * count
     */
    public Integer count(T domain) {
        return getSqlSession().selectOne(this.count, domain);
    }

    /**
     * countByMap
     */
    public Integer countByMap(Map map, String sqlKey) {
        return getSqlSession().selectOne(sqlKey + "Count", map);
    }

    protected String getSQL(String format) {
        return MessageFormat.format("{0}." + format, entityClassName);
    }
}
