package com.apass.zufang.domain.dto;
import java.util.ArrayList;
import java.util.List;

import com.apass.zufang.domain.vo.HouseVo;
public class ApartHouseList{
	
    private Long id;
    private String area;
    private String name;
    
    private List<HouseVo> rows = new ArrayList<HouseVo>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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