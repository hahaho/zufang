package com.apass.zufang.domain.dto;

/**
 * Created by xiaohai on 2017/11/15.
 */
public class BsdiffQuery {
    private String bsdiffVer;
    private String lineId;

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getBsdiffVer() {
        return bsdiffVer;
    }

    public void setBsdiffVer(String bsdiffVer) {
        this.bsdiffVer = bsdiffVer;
    }
}
