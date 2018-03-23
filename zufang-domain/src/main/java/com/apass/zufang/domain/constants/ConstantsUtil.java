package com.apass.zufang.domain.constants;

public class ConstantsUtil {

	/**
	 * token失效时间间隔(默认7天失效)
	 */
	public static final Long TOKEN_EXPIRES_SPACE = 7 * 24 * 60 * 60L;

	/**
	 * 外部调租房接口，token设置失效时间（默认两小时）
	 */
	public static final Long TOKEN_EXPIRES_HOUSE = 2 * 60 * 60L;

	/**
	 * 显示附近房源数量
	 */
	public static final int THE_NEARBY_HOUSES_NUMBER = 10;
}
