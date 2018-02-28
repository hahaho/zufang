package com.apass.zufang.domain.entity;
import java.util.ArrayList;
import java.util.List;

import com.apass.zufang.domain.vo.HouseVo;
public class ApartHouseList{
	
    private Long id;
    private String code;
    private String name;
    
    private List<HouseVo> rows = new ArrayList<HouseVo>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<HouseVo> getRows() {
		return rows;
	}

	public void setRows(List<HouseVo> rows) {
		this.rows = rows;
	}

}