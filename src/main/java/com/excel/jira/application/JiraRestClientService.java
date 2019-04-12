package com.excel.jira.application;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.excel.jira.application.client.IssueClient;
import com.excel.jira.application.clientImpl.IssueClientImpl;
import com.excel.jira.constants.RestParamConstants;
import com.excel.jira.constants.RestPathConstants;
import com.excel.jira.domain.beans.IssueBean;
import com.excel.jira.utils.HttpMethodFactory;
import com.excel.jira.utils.IssueResponse;
import com.excel.jira.utils.RestException;


@Service
public class JiraRestClientService implements RestPathConstants,RestParamConstants{

	
    private String username = StringUtils.EMPTY;
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private CloseableHttpClient httpclient;
    private HttpHost proxyHost;
    private HttpClientContext clientContext;
    private URI baseUri;
    private static RequestConfig requestConfig;
    private IssueClient issueClient;

    
    @Value("${url}")
    private String testSystemUrl;
    @Value("${username}")
    private String login;
    @Value("${password}")
    private String password;
    
	/*
	 * protected final ExecutorService executorService;
	 * 
	 * @Autowired public JiraRestClientService(ExecutorService executorService) {
	 * this.executorService = executorService; }
	 */
    
    public int connect() throws IOException, URISyntaxException, ExecutionException, InterruptedException {
    	URI uri = new URI(testSystemUrl);
    	return connect(uri, login, password);
    }
    
    public int connect(URI uri, String username, String password) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        return connect(uri, username, password, null);
    }
    
    
	private int connect(URI uri, String username, String password, HttpHost proxyHost) 
			throws IOException, URISyntaxException, ExecutionException, InterruptedException{
		this.username = username;
        String host = uri.getHost();
        int port = getPort(uri.toURL());
        String scheme = HTTP;
        if (port == 443) scheme = HTTPS;
        HttpHost target = new HttpHost(host, port, scheme);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials(username, password));
        httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local
        // auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(target, basicAuth);
        // Add AuthCache to the execution context
        clientContext = HttpClientContext.create();
        clientContext.setAuthCache(authCache);
        this.baseUri = buildBaseURI(uri);

        // setzen des Proxies
        if(proxyHost != null){
            this.proxyHost = proxyHost;
            requestConfig = RequestConfig.custom().setProxy(proxyHost).build();
        }

        URIBuilder uriBuilder = URIHelper.buildPath(baseUri, USER);
        uriBuilder.addParameter(USERNAME, username);
        HttpGet method = HttpMethodFactory.createGetMethod(uriBuilder.build());
        CloseableHttpResponse response = httpclient.execute(method, clientContext);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
        	System.out.println("we are able to login");
            // Get the Cache for the CustomFields, need to deserialize the customFields in Issue Json
        }
        response.close();
        return statusCode;

	}
	
	
	 private int getPort(URL endpointUrl) {
	        int port = (endpointUrl.getPort() != -1 ? endpointUrl.getPort() : endpointUrl.getDefaultPort());
	        if (port != -1) {
	            return port;
	        }
	        if (HTTPS.equals(endpointUrl.getProtocol())) {
	            return 443;
	        }
	        return 80;
	 }
	 public URI getBaseUri() {
	        return baseUri;
	    }
	 public HttpClientContext getClientContext() {
	        return clientContext;
	    }
	 public CloseableHttpClient getClient() {
	        return httpclient;
	    }
	 private URI buildBaseURI(URI uri) throws URISyntaxException {
	        String path = uri.getPath();
	        if (path.isEmpty() == false) {
	            if (path.endsWith("/")) {
	                path = path.substring(0, path.length() - 1);
	            }
	            path = path.concat(RestPathConstants.BASE_REST_PATH);
	        } else {
	            path = RestPathConstants.BASE_REST_PATH;
	        }
	        return new URIBuilder(uri).setPath(path).build();
	    }

	public static RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public static void setRequestConfig(RequestConfig requestConfig) {
		JiraRestClientService.requestConfig = requestConfig;
	}

	public IssueClient getIssueClient() {
        if (issueClient == null) {
            issueClient = new IssueClientImpl(this);
        }
        return issueClient;
    }

	public void createTicket(IssueBean issue) throws RestException, IOException, ExecutionException, InterruptedException, URISyntaxException {
		
		/*
		 * IssueBean issue = new IssueBean(); FieldsBean fields = new FieldsBean();
		 * fields.setDescription("Test Description"); fields.setSummary("Test Title");
		 * ProjectBean project = new ProjectBean(); project.setKey("WEMSPRT");
		 * fields.setProject(project); IssuetypeBean issueType = new IssuetypeBean();
		 * issueType.setName("Task"); fields.setIssuetype(issueType); PriorityBean
		 * priority = new PriorityBean();
		 * priority.setName(JsonConstants.PRIORITY_MAJOR); fields.setPriority(priority);
		 * fields.setDuedate("2015-08-01"); UserBean userBean = new UserBean();
		 * userBean.setName("admin"); fields.setAssignee(userBean);
		 */
		/*
		 * List<ComponentBean> componentBeen = new ArrayList<>(); ComponentBean
		 * componentBean1 = new ComponentBean(); componentBean1.setName("Komponente 1");
		 * componentBeen.add(componentBean1); ComponentBean componentBean2 = new
		 * ComponentBean(); componentBean2.setName("Komponente 2");
		 * componentBeen.add(componentBean2); fields.setComponents(componentBeen);
		 * 
		 * List<VersionBean> versionBeen = new ArrayList<>(); VersionBean versionBean1 =
		 * new VersionBean(); versionBean1.setName("1.1");
		 * versionBeen.add(versionBean1); VersionBean versionBean2 = new VersionBean();
		 * versionBean2.setName("1.0"); versionBeen.add(versionBean2);
		 * fields.setVersions(versionBeen); fields.setFixVersions(versionBeen);
		 * 
		 *  List<String> labels = new ArrayList<>();
		 * labels.add("foobar"); labels.add("inubit"); fields.setLabels(labels);
		 * issue.setFields(fields);
		 */
	         
	        issueClient= getIssueClient();
	        IssueResponse issueResponse= issueClient.createIssue(issue);
        	
	        if (issueResponse != null) {
	            String issueKey = issueResponse.getKey();
	            if (issueKey != null) {
	                System.out.println(issueKey);
	            } else {
	                System.out.println(issueResponse.getError());
	            }
	        }


	    }
		
	}
