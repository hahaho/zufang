package com.apass.zufang.domain.common;

import java.util.List;

/**
 * 地铁集合
 * @author zwd
 *
 */
public class WorkSubwayContent {
	
	private String line_name;//名称
	
	private String id;
	
	private String line_uid;
	
	private String pair_line_uid;
	
	private List<Stops> stops;//站点

	 
	public String getLine_name() {
		return line_name;
	}

	public void setLine_name(String line_name) {
		this.line_name = line_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLine_uid() {
		return line_uid;
	}

	public void setLine_uid(String line_uid) {
		this.line_uid = line_uid;
	}

	public String getPair_line_uid() {
		return pair_line_uid;
	}

	public void setPair_line_uid(String pair_line_uid) {
		this.pair_line_uid = pair_line_uid;
	}

	public List<Stops> getStops() {
		return stops;
	}

	public void setStops(List<Stops> stops) {
		this.stops = stops;
	}
}
