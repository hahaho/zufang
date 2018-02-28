package com.apass.zufang.domain.enums;

/**
 * 
 * @author shilin
 *
 */
public enum BusinessHouseTypeEnums {
	
	// 热门房源类型	1：正常，2:精选',
	FY_ZHENGCHANG_1(1, "正常","FY"),
	FY_JINGXUAN_2(2, "精选","FY"),
	//合租类型	'1:整租；2:合租',
	HZ_ZHENGZU_1(1, "整租","HZ"),
	HZ_HEZU_2(2, "合租","HZ"),
	// 押金类型	'1:押一付三;2:押一付一;.....'
	YJLX_1(1, "押一付一","YJLX"),
	YJLX_2(2, "押一付三","YJLX"),
	YJLX_3(3, "押一付六","YJLX"),
	// 朝向， 1:东 2:南 3:西 4:北
	CX_EAST_1(1, "东","CX"),
	CX_SOUTH_2(2, "南","CX"),
	CX_WEST_3(3, "西","CX"),
	CX_NORTH_4(4, "北","CX"),
	// 1:待上架；2：上架；3：下架；4：删除
	ZT_WSHAGNJIA_1(1, "待上架","ZT"),
	ZT_SHAGNJIA_2(2, "上架","ZT"),
	ZT_XIAJIA_3(3, "下架","ZT"),
	ZT_SHANGCHU_4(4, "删除","ZT"),
	ZT_XIUGAI_5(5,"修改","ZT"),
	// '装修情况:1:豪华装修...'
	ZX_HAOHUA_1(1, "豪华装修","ZX"),
	// '1:普通住宅...',
	ZZ_PUTONG(1, "普通住宅","ZZ");
	

	private Integer code;

	private String message;
	
	private String type;
	
	private BusinessHouseTypeEnums(Integer code, String message, String type) {
		this.code = code;
		this.message = message;
		this.type = type;
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
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static BusinessHouseTypeEnums valueOf(Integer value,String type) {
//		// 热门房源类型	1：正常，2:精选',
//		FY_ZHENGCHANG_1(1, "正常","FY"),
//		FY_JINGXUAN_2(2, "精选","FY"),
        if("FY".equals(type)&&value==1){
        	return FY_ZHENGCHANG_1;
        }
        if("FY".equals(type)&&value==2){
        	return FY_JINGXUAN_2;
        }
        
//		//合租类型	'1:整租；2:合租',
//		HZ_ZHENGZU_1(1, "整租","HZ"),
//		HZ_HEZU_2(2, "合租","HZ"),
        if("HZ".equals(type)&&value==1){
        	return HZ_ZHENGZU_1;
        }
        if("HZ".equals(type)||value==2){
        	return HZ_HEZU_2;
        }
        
//		// 押金类型	'1:押一付三;2:押一付一;.....'
//		YJLX_1(1, "押一付一","YJLX"),
//		YJLX_2(2, "押一付三","YJLX"),
//		YJLX_3(3, "押一付六","YJLX"),
        if("YJLX".equals(type)&&value==1){
        	return YJLX_1;
        }
        if("YJLX".equals(type)&&value==2){
        	return YJLX_2;
        }
        if("YJLX".equals(type)&&value==3){
        	return YJLX_3;
        }
        
//    	// 朝向， 1:东 2:南 3:西 4:北
//    	CX_EAST_1(1, "东","CX"),
        if("CX".equals(type)&&value==1){
        	return CX_EAST_1;
        }
//    	CX_SOUTH_2(2, "南","CX"),
        if("CX".equals(type)&&value==2){
        	return CX_SOUTH_2;
        }
//    	CX_WEST_3(3, "西","CX"),
        if("CX".equals(type)&&value==3){
        	return CX_WEST_3;
        }
//    	CX_NORTH_4(4, "北","CX"),
        if("CX".equals(type)&&value==4){
        	return CX_NORTH_4;
        }

//    	 1:待上架；2：上架；3：下架；4：删除
//    	ZT_WSHAGNJIA_1(1, "待上架","ZT"),
        if("ZT".equals(type)&&value==1){
        	return ZT_WSHAGNJIA_1;
        }
//    	ZT_SHAGNJIA_2(2, "上架","ZT"),
        if("ZT".equals(type)&&value==2){
        	return ZT_SHAGNJIA_2;
        }
//    	ZT_XIAJIA_3(3, "下架","ZT"),
        if("ZT".equals(type)&&value==3){
        	return ZT_XIAJIA_3;
        }
//    	ZT_SHANGCHU_4(4, "删除","ZT"),
        if("ZT".equals(type)&&value==4){
        	return ZT_SHANGCHU_4;
        }
//    	ZT_XIUGAI_5(5,"修改","ZT"),
        if("ZT".equals(type)&&value==4){
        	return ZT_XIUGAI_5;
        }
//    	 '装修情况:1:豪华装修...'
//    	ZX_HAOHUA_1(1, "豪华装修","ZX"),
        if("ZX".equals(type)&&value==1){
        	return ZX_HAOHUA_1;
        }
//    	// '1:普通住宅...',
//    	ZZ_PUTONG(1, "普通住宅","ZZ");
        if("ZZ".equals(type)&&value==1){
        	return ZZ_PUTONG;
        }
        return null;
    }
}
