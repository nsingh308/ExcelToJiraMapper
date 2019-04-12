package com.excel.jira.domain.beans;

import com.google.gson.annotations.Expose;

public class FieldsBean extends BaseBean{

	@Expose
    private IssuetypeBean issuetype;
	@Expose
    private String description;
	@Expose
    private PriorityBean priority;
	@Expose
    private UserBean assignee;
	@Expose
    private ProjectBean project;
	@Expose
    private String summary;
	public IssuetypeBean getIssuetype() {
		return issuetype;
	}
	public void setIssuetype(IssuetypeBean issuetype) {
		this.issuetype = issuetype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public PriorityBean getPriority() {
		return priority;
	}
	public void setPriority(PriorityBean priority) {
		this.priority = priority;
	}
	public UserBean getAssignee() {
		return assignee;
	}
	public void setAssignee(UserBean assignee) {
		this.assignee = assignee;
	}
	public ProjectBean getProject() {
		return project;
	}
	public void setProject(ProjectBean project) {
		this.project = project;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
}
