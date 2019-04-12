package com.excel.jira.application.clientImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import com.excel.jira.application.JiraRestClientService;
import com.excel.jira.application.URIHelper;
import com.excel.jira.application.client.IssueClient;
import com.excel.jira.constants.RestParamConstants;
import com.excel.jira.constants.RestPathConstants;
import com.excel.jira.domain.beans.ErrorBean;
import com.excel.jira.domain.beans.IssueBean;
import com.excel.jira.utils.HttpMethodFactory;
import com.excel.jira.utils.IssueResponse;
import com.excel.jira.utils.RestException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class IssueClientImpl implements IssueClient, RestParamConstants, RestPathConstants {

	private static final String SEPARATOR = ",";
	protected final URI baseUri;
	protected final JiraRestClientService jiraRestClient;
	protected final CloseableHttpClient client;
	protected final HttpClientContext clientContext;

	public IssueClientImpl(JiraRestClientService jiraRestClient) {
		this.baseUri = jiraRestClient.getBaseUri();
		this.clientContext = jiraRestClient.getClientContext();
		this.jiraRestClient = jiraRestClient;
		this.client = jiraRestClient.getClient();
	}

	protected final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	protected URIBuilder buildPath(String... paths) throws URISyntaxException {
		return URIHelper.buildPath(baseUri, paths);
	}

	public IssueResponse createIssue(final IssueBean issue)
			throws URISyntaxException, ClientProtocolException, IOException, RestException {
		Validate.notNull(issue);

		String json = gson.toJson(issue);
		URIBuilder uriBuilder = buildPath(ISSUE);
		HttpPost method = HttpMethodFactory.createPostMethod(uriBuilder.build(), json);
		CloseableHttpResponse response = client.execute(method, clientContext);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
			JsonReader jsonReader = getJsonReader(response);
			IssueBean issueBean = gson.fromJson(jsonReader, IssueBean.class);
			method.releaseConnection();
			response.close();
			return new IssueResponse(issueBean.getKey());
		} else if (statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();
			JsonReader jsonReader = toJsonReader(inputStream);
			ErrorBean error = gson.fromJson(jsonReader, ErrorBean.class);
			method.releaseConnection();
			response.close();
			return new IssueResponse(error);
		} else {
			RestException restException = new RestException(response);
			method.releaseConnection();
			response.close();
			throw restException;
		}

	}

	protected JsonReader getJsonReader(CloseableHttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		InputStream inputStream = entity.getContent();
		return toJsonReader(inputStream);
	}

	protected JsonReader toJsonReader(InputStream inputStream) throws UnsupportedEncodingException {

		Validate.notNull(inputStream);
		InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
		JsonReader jsonReader = new JsonReader(reader);
		jsonReader.setLenient(true);
		return jsonReader;
	}

}
