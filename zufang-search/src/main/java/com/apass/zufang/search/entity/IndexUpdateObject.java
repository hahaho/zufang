package com.apass.zufang.search.entity;

import com.apass.zufang.search.enums.IndexType;
import com.apass.zufang.search.enums.OperatorType;

/**
 * type: class
 *
 * @author xianzhi.wang
 * @date 2017/8/17
 * @see
 * @since JDK 1.8
 */
public class IndexUpdateObject {
    private final Integer id;

    private final OperatorType operatorType;

    private final IndexType indexType;

    public IndexUpdateObject(Integer id, OperatorType operatorType, IndexType indexType) {
        super();
        this.id = id;
        this.operatorType = operatorType;
        this.indexType = indexType;
    }


    public void offer() {
        this.indexType.offerQueue(new UpdatedObject(id, operatorType));
    }
}
