package com.excel.jira.domain.beans;

import com.google.gson.annotations.Expose;

public class IssueBean extends BaseBean {

	@Expose
    private FieldsBean fields;
    @Expose
    private String key;
	public FieldsBean getFields() {
		return fields;
	}
	public void setFields(FieldsBean fields) {
		this.fields = fields;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
    
    
}
