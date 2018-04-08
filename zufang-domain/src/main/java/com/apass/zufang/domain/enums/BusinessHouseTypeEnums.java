package com.apass.zufang.domain.enums;

/**
 * 
 * @author shilin
 *
 */
public enum BusinessHouseTypeEnums {
	
	// 热门房源类型	1：正常，2:精选',
	FYLX_1(1, "正常"),
	FYLX_2(2, "精选"),
	//合租类型	'1:整租；2:合租',
	HZ_0(0, "不限"),
	HZ_1(1, "整租"),
	HZ_2(2, "合租"),
	// 押金类型	'1:押一付三;2:押一付一;.....'
	YJLX_1(1, "押一付一"),
	YJLX_2(2, "押一付二"),
	YJLX_3(3, "押一付三"),
	YJLX_4(4, "押二付一"),
	YJLX_5(5, "押二付二"),
	YJLX_6(6, "押二付三"),
	YJLX_7(7, "面议"),
	YJLX_8(8, "半年付"),
	YJLX_9(9, "半年付不押"),
	YJLX_10(10, "押一付半年"),
	// 朝向， 1:东 2:南 3:西 4:北
	CX_1(1, "东"),
	CX_2(2, "南"),
	CX_3(3, "西"),
	CX_4(3, "北"),
	CX_5(5, "东南"),
	CX_6(6, "东北"),
	CX_7(7, "西南"),
	CX_8(8, "西北"),
	// 1:待上架；2：上架；3：下架；4：删除
	ZT_1(1, "待上架"),
	ZT_2(2, "上架"),
	ZT_3(3, "下架"),
	ZT_4(4, "删除"),
	ZT_5(5,"审核中"),
	// '装修情况:1:豪华装修...'
	ZX_1(1, "豪华装修"),
	ZX_2(2, "精装修"),
	ZX_3(3, "简单装修"),
	ZX_4(4, "毛坯"),
	
	CZJS_1(1, "出租主卧"),
	CZJS_2(2, "出租次卧"),
	CZJS_3(3, "出租隔断间"),
	CZJS_4(4, "出租床位"),
	
	DT_1(1,"有电梯"),
	DT_2(2,"无电梯");
	

	private Integer code;

	private String message;
	
	private BusinessHouseTypeEnums(Integer code, String message) {
		this.code = code;
		this.message = message;
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

	/*** 获取房源类型*/
	public static BusinessHouseTypeEnums valueOfFYLX(Integer code) {
		return BusinessHouseTypeEnums.valueOf("FYLX_"+code);
	}
	
	/*** 获取合租*/
	public static BusinessHouseTypeEnums valueOfHZ(Integer code) {
		return BusinessHouseTypeEnums.valueOf("HZ_"+code);
	}
	
	/*** 获取押金类型*/
	public static BusinessHouseTypeEnums valueOfYJLX(Integer code) {
		return BusinessHouseTypeEnums.valueOf("YJLX_"+code);
	}
	
	/*** 获取朝向*/
	public static BusinessHouseTypeEnums valueOfCX(Integer code) {
		return BusinessHouseTypeEnums.valueOf("CX_"+code);
	}
	/*** 获取状态*/
	public static BusinessHouseTypeEnums valueOfZT(Integer code) {
		return BusinessHouseTypeEnums.valueOf("ZT_"+code);
	}
	
	/*** 获取装修*/
	public static BusinessHouseTypeEnums valueOfZX(Integer code) {
		return BusinessHouseTypeEnums.valueOf("ZX_"+code);
	}
	/*** 获取出租介绍*/
	public static BusinessHouseTypeEnums valueOfCZJS(Integer code) {
		return BusinessHouseTypeEnums.valueOf("CZJS_"+code);
	}


	/**
	 * 获取朝向code
	 */
	public static String getCXCode(String message){
		for(int i = 1;i<=10;i++){
			BusinessHouseTypeEnums e = BusinessHouseTypeEnums.valueOf("CX_"+i);
			if(e.getMessage().equals(message)){
				return e.getCode().toString();
			}
		}
		//默认返回1
		return "1";
	}


	/**
	 * 获取押金类型code
	 */
	public static String getYJLXCode(String message){
		for(int i = 1;i<=10;i++){
			BusinessHouseTypeEnums e = BusinessHouseTypeEnums.valueOf("YJLX_"+i);
			if(e.getMessage().contains(message)){
				return e.getCode().toString();
			}
		}
		//默认返回1
		return "1";
	}

	/**
	 * 获取合租类型
	 */
	public static String getHZCode(String message){
		for(int i = 0;i<=2;i++){
			BusinessHouseTypeEnums e = BusinessHouseTypeEnums.valueOf("HZ_"+i);
			if(e.getMessage().contains(message)){
				return e.getCode().toString();
			}
		}
		return "0";
	}

}
