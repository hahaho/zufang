package com.apass.zufang.domain.enums;

/**
 * Created by xiaohai on 2018/3/22.
 */
public enum  TokenCodeEnums {
    TOKEN_SUCCESS("0","成功"),
    TOKEN_INVALID("1002","无效token"),
    TOKEN_EXPIRE("1003","token过期"),
    APP_KEY_INVALID("1004","无效app_key"),
    TOKEN_LACK("1005","缺少token参数");


    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    TokenCodeEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
