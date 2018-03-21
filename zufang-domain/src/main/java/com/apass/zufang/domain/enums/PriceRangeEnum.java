package com.apass.zufang.domain.enums;

import javax.xml.bind.ValidationEventLocator;

/**
 * Created by DELL on 2018/2/27.
 */
public enum PriceRangeEnum {
    PRICE_ONE(1,"¥1500以下"),
    PRICE_TWO(2,"¥ 1501 - ¥ 2500"),
    PRICE_THREE(3,"¥ 2501 - ¥ 3500"),
    PRICE_FOUR(4,"¥ 3501 - ¥ 5500"),
    PRICE_FIVE(5,"¥ 5501以上"),
    PRICE_ALL(6,"全部"),
    ;

    private int val;
    private String desc;

    private PriceRangeEnum(int val,String desc){
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
