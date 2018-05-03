package com.apass.zufang.domain.dto;

import com.apass.zufang.domain.entity.HouseInfoRela;

/**
 * Created by DELL on 2018/5/3.
 */
public class SimpleNearHouseDto {

    private double disOne;
    private HouseInfoRela houseInfoRela;

    public SimpleNearHouseDto(double disOne,HouseInfoRela houseInfoRela){
        this.disOne = disOne;
        this.houseInfoRela = houseInfoRela;

    }

    public double getDisOne() {
        return disOne;
    }

    public void setDisOne(double disOne) {
        this.disOne = disOne;
    }

    public HouseInfoRela getHouseInfoRela() {
        return houseInfoRela;
    }

    public void setHouseInfoRela(HouseInfoRela houseInfoRela) {
        this.houseInfoRela = houseInfoRela;
    }
}
