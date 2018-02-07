package com.apass.zufang.search.dao;

import com.apass.zufang.search.entity.Goods;
import com.apass.zufang.search.entity.UpdatedObject;
import com.apass.zufang.search.enums.IndexType;
import com.apass.zufang.search.enums.OperatorType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * type: class
 *
 * @author xianzhi.wang
 * @date 2017/8/21
 * @see
 * @since JDK 1.8
 */
@Service
public class GoodsEsDao implements EsBaseDao<Goods> {
    @Override
    public boolean add(Goods goods) {
        UpdatedObject<Goods> object = new UpdatedObject<>(goods, OperatorType.ADD);
        return IndexType.GOODS.offerQueue(object);
    }

    @Override
    public boolean update(Goods goods) {
        UpdatedObject<Goods> object = new UpdatedObject<>(goods, OperatorType.UPDATE);
        return IndexType.GOODS.offerQueue(object);
    }

    @Override
    public boolean delete(Goods goods) {
        UpdatedObject<Goods> object = new UpdatedObject<>(goods, OperatorType.DELETE);
        return IndexType.GOODS.offerQueue(object);
    }

}
