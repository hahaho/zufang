package com.apass.zufang.search.dao;
import com.apass.zufang.search.entity.HouseEs;
import com.apass.zufang.search.entity.UpdatedObject;
import com.apass.zufang.search.enums.IndexType;
import com.apass.zufang.search.enums.OperatorType;

/**
 * Created by xiaohai on 2018/2/26.
 */
public class HouseEsDao implements EsBaseDao<HouseEs>{
    @Override
    public boolean add(HouseEs houseEs) {
        UpdatedObject<HouseEs> object = new UpdatedObject<>(houseEs, OperatorType.ADD);
        return IndexType.HOUSE.offerQueue(object);
    }

    @Override
    public boolean update(HouseEs houseEs) {
        UpdatedObject<HouseEs> object = new UpdatedObject<>(houseEs, OperatorType.UPDATE);
        return IndexType.HOUSE.offerQueue(object);
    }

    @Override
    public boolean delete(HouseEs houseEs) {
        UpdatedObject<HouseEs> object = new UpdatedObject<>(houseEs, OperatorType.DELETE);
        return IndexType.HOUSE.offerQueue(object);
    }
}
