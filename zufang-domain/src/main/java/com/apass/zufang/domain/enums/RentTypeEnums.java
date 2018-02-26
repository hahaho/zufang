package com.apass.zufang.domain.enums;

/**
 * 
 * @author shilin
 *
 */
public enum RentTypeEnums {
	
	// 热门房源类型	1：正常，2:精选',
	FY_ZHENGCHANG_1(1, "正常"),
	FY_JINGXUAN_2(2, "精选"),
	//合租类型	'1:整租；2:合租',
	HZ_ZHENGZU_1(1, "整租"),
	HZ_HEZU_2(2, "合租"),
	// 押金类型	'1:押一付三;2:押一付一;.....'
	YJLX_1(1, "押一付一"),
	YJLX_2(2, "押一付三"),
	YJLX_3(3, "押一付六"),
	// 朝向， 1:东 2:南 3:西 4:北
	CX_EAST_1(1, "东"),
	CX_SOUTH_2(2, "南"),
	CX_WEST_3(3, "西"),
	CX_NORTH_4(4, "北"),
	// 1:待上架；2：上架；3：下架；4：删除
	ZT_WSHAGNJIA_1(1, "待上架"),
	ZT_SHAGNJIA_2(2, "上架"),
	ZT_XIAJIA_3(3, "下架"),
	ZT_SHANGCHU_4(4, "删除"),
	ZT_XIUGAI_5(5,"修改"),
	// '装修情况:1:豪华装修...'
	ZX_HAOHUA_1(1, "豪华装修"),
	// '1:普通住宅...',
	ZZ_PUTONG(1, "普通住宅");
	

	private Integer code;

	private String message;
	
	private RentTypeEnums(Integer code, String message) {
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
}
