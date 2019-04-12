package com.excel.jira.application.client;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import com.excel.jira.domain.beans.IssueBean;
import com.excel.jira.utils.IssueResponse;
import com.excel.jira.utils.RestException;

/**
 * The IssueClient provides all Informations for Jira Issues
 */
public interface IssueClient {
    /**
     * Create a new issue
     *
     * @param issue = the issue
     * @return IssueResponse
     * @throws IOException 
     */
    IssueResponse createIssue(IssueBean issue) throws URISyntaxException,ClientProtocolException, IOException,RestException;

}
