package com.excel.jira.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.excel.jira.domain.beans.IssueBean;

import static java.lang.System.exit;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class SpringBootConsoleApplication implements CommandLineRunner {

    @Autowired
    private JiraRestClientService jiraRestClientService;
    @Autowired
    private ReadExcelService readExcelService;
    
    
    
    public static void main(String[] args) throws Exception {

        //disabled banner, don't want to see the spring logo
        SpringApplication app = new SpringApplication(SpringBootConsoleApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);

        //SpringApplication.run(SpringBootConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    		List<IssueBean> issueData=readExcelService.readJIRADetail(args[0]);
    		jiraRestClientService.connect();
    		for (IssueBean issueBean : issueData) {
    			jiraRestClientService.createTicket(issueBean);
    		}

        exit(0);
    }
}