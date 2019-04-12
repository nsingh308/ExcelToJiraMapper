package com.excel.jira.domain.beans;

import com.google.gson.annotations.Expose;

public class ProjectBean extends BaseBean{

	@Expose
    private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
}
