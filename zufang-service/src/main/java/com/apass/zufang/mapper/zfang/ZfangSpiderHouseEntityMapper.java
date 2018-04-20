package com.apass.zufang.mapper.zfang;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.ZfangSpiderHouseEntity;

import java.util.List;

/**
 * @author xiaohai
 * @date 2018/4/19.
 */
public interface ZfangSpiderHouseEntityMapper extends GenericMapper<ZfangSpiderHouseEntity,Long> {

    /**
     * 查询所有外部房源
     * @return
     */
    List<ZfangSpiderHouseEntity> listAllExtHouse();

    ZfangSpiderHouseEntity selectByExtHouseId(String id);

    int updateByExtHouseIdSelective(ZfangSpiderHouseEntity entity);
}
