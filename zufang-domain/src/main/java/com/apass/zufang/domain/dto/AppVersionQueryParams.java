package com.apass.zufang.domain.dto;

import com.apass.zufang.common.model.QueryParams;

public class AppVersionQueryParams extends QueryParams{
	
	private Long id;
    private String versionName;

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
  
}
