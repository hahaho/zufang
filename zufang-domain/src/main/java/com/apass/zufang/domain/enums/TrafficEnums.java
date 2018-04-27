package com.apass.zufang.domain.enums;

/**
 * Created by xiaohai on 2018/4/26.
 */
public enum  TrafficEnums {
    TRAFFIC_1(1, "公交","40"),
    TRAFFIC_2(2, "驾车","50"),
    TRAFFIC_3(3, "步行","5.6"),
    TRAFFIC_4(4, "骑行","20");

    private Integer code;

    /**
     * 出行方式
     */
    private String message;

    /**
     * 速度，单位km/h
     */
    private String speed;

    private TrafficEnums(Integer code, String message,String speed) {
        this.code = code;
        this.message = message;
        this.speed = speed;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public static TrafficEnums getEnum(String name){
        for(TrafficEnums e : values()){
            if(name.contains(e.getMessage())){
                return e;
            }
        }

        return null;
    }

    /**
     * 在生活的一般速度可定为：人5,自行车15,摩托车60,汽车80,火车120,飞机1000
     * @param code
     * @param minute
     * @return
     */
    public static String getDistance(Integer code,Double minute){
        String distance = null;
        if(code == 1){//公交
            distance = String.valueOf(Double.valueOf(TRAFFIC_1.getSpeed()) * (minute / 60));
        }else if(code == 2){//驾车
            distance = String.valueOf(Double.valueOf(TRAFFIC_2.getSpeed()) * (minute / 60));

        }else if(code == 3){//步行
            distance = String.valueOf(Double.valueOf(TRAFFIC_3.getSpeed()) * (minute / 60));

        }else if(code == 4){//骑行
            distance = String.valueOf(Double.valueOf(TRAFFIC_4.getSpeed()) * (minute / 60));
        }

        return distance;
    }
}

