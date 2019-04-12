package com.excel.jira.application;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.excel.jira.domain.beans.FieldsBean;
import com.excel.jira.domain.beans.IssueBean;
import com.excel.jira.domain.beans.IssuetypeBean;
import com.excel.jira.domain.beans.PriorityBean;
import com.excel.jira.domain.beans.ProjectBean;
import com.excel.jira.domain.beans.UserBean;

@Service
public class ReadExcelService {
	
		public List<IssueBean> readJIRADetail(String filePath) {
			List<IssueBean> list = new ArrayList<IssueBean>();
			try {
				FileInputStream file = new FileInputStream(new File(filePath));

				// Create Workbook instance holding reference to .xlsx file
				XSSFWorkbook workbook = new XSSFWorkbook(file);

				// Get first/desired sheet from the workbook
				XSSFSheet sheet = workbook.getSheetAt(0);

				// Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				int rowCount = 0;
				while (rowIterator.hasNext()) {
					if (++rowCount == 1) {
						rowIterator.next();
						continue;
					}
					Row row = rowIterator.next();
					// For each row, iterate through all the columns

					IssueBean issue = new IssueBean();

					//IssueBean,FieldsBean,ProjectBean,IssuetypeBean,PriorityBean,UserBean
			        FieldsBean fields = new FieldsBean();
			        if (null != row.getCell(2))
			        fields.setDescription(row.getCell(2).getStringCellValue());
			        if (null != row.getCell(3))
			        fields.setSummary(row.getCell(3).getStringCellValue());
			        ProjectBean project = new ProjectBean();
			        if (null != row.getCell(0))
			        project.setKey(row.getCell(0).getStringCellValue());
			        fields.setProject(project);
			        IssuetypeBean issueType = new IssuetypeBean();
			        if (null != row.getCell(1))
			        issueType.setName(row.getCell(1).getStringCellValue());
			        fields.setIssuetype(issueType);
			        PriorityBean priority = new PriorityBean();
			        if (null != row.getCell(4))
			        priority.setName(row.getCell(4).getStringCellValue());
			        fields.setPriority(priority);
			        UserBean userBean = new UserBean();
			        if (null != row.getCell(6))
			        userBean.setName(row.getCell(6).getStringCellValue());
					fields.setAssignee(userBean);
					issue.setFields(fields);
					list.add(issue);

				}
				System.out.println(list);
				file.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}

}
