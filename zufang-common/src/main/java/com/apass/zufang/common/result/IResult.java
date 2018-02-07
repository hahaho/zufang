package com.apass.zufang.common.result;

import com.apass.zufang.common.code.ErrorCode;

/**
 * Created by jie.xu on 17/6/2.
 */
public interface IResult<T> {

  T getData();

  boolean isStatus();

  String getMessage();

  ErrorCode getErrorCode();
}
