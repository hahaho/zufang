package com.apass.esp.mapper;

import com.apass.esp.third.party.jd.entity.base.JdCategory;
import com.apass.gfb.framework.mybatis.GenericMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JdCategoryMapper extends GenericMapper<JdCategory, Long> {
    JdCategory getCateGoryByCatId(@Param("catId") long catId);

    List<JdCategory> selectByParentId(@Param("parentId") Long parentId,@Param("catClass") Integer catClass);
}
