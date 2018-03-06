package com.apass.zufang.mapper.zfang;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.dto.BsdiffQuery;
import com.apass.zufang.domain.entity.weex.BsdiffInfoEntity;

import java.util.List;

public interface BsdiffInfoEntityMapper extends GenericMapper<BsdiffInfoEntity, Long> {

    /**
     * 查询所有bsdiff内容
     * @return
     */
    List<BsdiffInfoEntity> selectAllBsdiff();

    List<BsdiffInfoEntity> selectBsdiffInfoByVo(BsdiffQuery query);

    List<BsdiffInfoEntity> selectAllBsdiffNewest();

    BsdiffInfoEntity selectMaxBsdiffInfoById(String id);


}