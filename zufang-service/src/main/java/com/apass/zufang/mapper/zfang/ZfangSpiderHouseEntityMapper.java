package com.apass.zufang.mapper.zfang;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.ZfangSpiderHouseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xiaohai
 * @date 2018/4/19.
 */
public interface ZfangSpiderHouseEntityMapper extends GenericMapper<ZfangSpiderHouseEntity,Long> {

    /**
     * 查询所有外部房源
     * @return
     * @param apartmentId
     * @param paramMap
     */
    List<ZfangSpiderHouseEntity> listAllExtHouse(Map<String, Object> paramMap);

    ZfangSpiderHouseEntity selectByExtHouseId(String id);

    int updateByExtHouseIdSelective(ZfangSpiderHouseEntity entity);
}
