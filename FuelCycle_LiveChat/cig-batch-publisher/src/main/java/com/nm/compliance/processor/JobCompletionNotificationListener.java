package com.nm.compliance.processor;

import java.io.File;
import java.util.HashSet;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.ldap.core.LdapAttributes;
import org.springframework.ldap.ldif.parser.LdifParser;

public class JobCompletionNotificationListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("*******************READING LDIF FILE***********");
		jobExecution.getExecutionContext().put("HOMailList", getMailFromLdif());
		System.out.println("*******************JOB BEGINS***********");

	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		System.out.println("*******************JOB ENDS***********");

	}

	private HashSet<String> getMailFromLdif() {
		HashSet<String> mailHOList = new HashSet<String>();
		try {

			LdifParser parser = new LdifParser(new File("//WST-STSQ-000968/D$/LDIF_File/HOEmailID.ldif"));
			parser.open();
			while (parser.hasMoreRecords()) {
				LdapAttributes attr = parser.getRecord();
				if (attr.get("mail") != null) {

					mailHOList.add(attr.get("mail").toString().substring(5).trim());
				}
			}
		} catch (Exception e) {
			System.out.println("Reading LDIF error");
		}
		return mailHOList;
	}
}
