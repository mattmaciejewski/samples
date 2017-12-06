package com.nm.compliance.processor;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.nm.compliance.data.ThreadData;
import com.nm.compliance.tree.PostComparator;
import com.nm.compliance.tree.PostData;
import com.nm.compliance.tree.PostMetaData;

public class PostItemProcessor implements ItemProcessor<ThreadData, ThreadData> {
	@Autowired
	DataSource messageDataSource;

	private HashSet<String> hoMailId;

	public PostItemProcessor(DataSource messageDatasource) {
		// TODO Auto-generated constructor stub
		this.messageDataSource = messageDatasource;
	}

	@Override
	public ThreadData process(ThreadData item) throws Exception {
		// TODO Auto-generated method stub

		Connection conn = messageDataSource.getConnection();
		Statement selectStatement = conn.createStatement();
		System.out.println("Reading for Thread Name: " + item.getThreadName());
		ResultSet rs = selectStatement.executeQuery(
				"Select * from dbo.FUEL_CYCLE_CHAT where StepTxt='" + item.getThreadName().replaceAll("'", "''") + "'" + "and ProcessStatusInd = 0");
		Statement stmt = null;
		stmt = conn.createStatement();
		int dataRows = rs.getRow();
		TreeMap<String, PostData> map = item.getMessageMap();
		if (map == null)
			map = new TreeMap<String, PostData>(new PostComparator());

		while (rs.next()) {
			try {

				PostMetaData postMetaData = new PostMetaData();

				postMetaData.setPostIdNum(rs.getInt("POSTIDNUM"));
				postMetaData.setEmailAddr(rs.getString("MEMBEREMAILADDRNAM"));
				postMetaData.setUsername(rs.getString("USERNAMETXT"));
				postMetaData.setStep(rs.getString("STEPTXT"));
				postMetaData.setUserType(rs.getString("USERTYPETXT"));
				postMetaData.setLikeCount(rs.getString("LIKECOUNTNUM"));
				postMetaData.setMessage(rs.getString("MESSAGETXT"));
				postMetaData.setPostDtm(rs.getTimestamp("POSTDTM"));

				PostData postData = new PostData();
				postData.setProcessedStatus(rs.getInt("PROCESSSTATUSIND"));
				postData.setMetadata(postMetaData);
				postData.setChildMessages(new TreeMap<String, PostData>(new PostComparator()));
				String stepTxt = String.valueOf(postMetaData.getPostIdNum());
				postData.setMessageId(stepTxt);
				map.put(stepTxt, postData);

				stmt.executeUpdate("Update dbo.FUEL_CYCLE_CHAT Set ProcessStatusInd = 1 where StepTxt='"
						+ item.getThreadName().replaceAll("'", "''") + "'");
				dataRows++;

			} catch (Exception ex) {
				System.out.println(ex.getStackTrace());
			}
			item.setMessageMap(map);
			// System.out.println(rs.getRow());
		}
		System.out.println("The Total Number of Processed Rows per Thread Name: " + dataRows);
		PostData rootPostData = createHeirarchy(item);

		// Code changes for HO Filtering
		if (checkHOUSer(item, hoMailId)) {
			StringBuilder textOutPut = new StringBuilder();
			textOutPut.append("Thread :").append(item.getThreadName()).append("\n");

			textOutPut.append(getTextForPost(rootPostData, item, 0));

			item.setPrintableString(textOutPut.toString());
			// System.out.println("root :" + rootPostData.getMessageId() + ":" +
			// rootPostData.getMessageId() + ":"+
			// rootPostData.getMetadata().getReplyToIdTxt());

			// stmt.executeUpdate("Update dbo.FUEL_CYCLE_CHAT Set ProcessStatusInd = 1 where
			// StepTxt='" + item.getThreadName().replaceAll("'", "''") + "'");
			// System.out.println(item.getRootPostData().getMessageId());

			return item;
		}

		else {
			System.out.println("No HO users found for "+item.getThreadName());
			return null;
		}

	}

	private String getTextForPost(PostData rootPostData, ThreadData item, int tabStops) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		String tabPrepender = "";
		for (int i = 0; i < tabStops; i++)
			tabPrepender += "\t";
		PostMetaData postMetaData = rootPostData.getMetadata();
		if (postMetaData.getEmailAddr().equalsIgnoreCase("")) {
			builder.append(tabPrepender).append(postMetaData.getUsername()).append("\t")
					.append(postMetaData.getPostDtm().toLocaleString()).append("\n");
		} else {
			builder.append(tabPrepender).append(postMetaData.getEmailAddr()).append("\t")
					.append(postMetaData.getPostDtm().toLocaleString()).append("\n");
		}
		builder.append("\n");
		builder.append(tabPrepender).append(postMetaData.getMessage());
		builder.append("\n");
		// builder.append(tabPrepender).append("Tags
		// :").append(postMetaData.getAllTagsTxt()).append("\n");
		builder.append(tabPrepender).append("Replies :" + rootPostData.getChildMessages().size()).append("\n\n");
		Set<?> childSet = rootPostData.getChildMessages().entrySet();
		if (childSet != null) {
			Iterator<?> iterator = childSet.iterator();
			while (iterator.hasNext()) {
				Map.Entry mentry = (Map.Entry) iterator.next();
				builder.append(getTextForPost((PostData) mentry.getValue(), item, tabStops + 1));
			}
		}

		return builder.toString();
	}

	private PostData createHeirarchy(ThreadData item) {

		PostData rootData = null;
		Set<String> keySet = item.getMessageMap().keySet();
		HashSet<String> recipientEmails = new HashSet<String>();
		Integer parentKey = 0;
		for (String key : keySet) {
			Integer intKey = Integer.valueOf(key);
			PostData childPost = item.getMessageMap().get(key);
			// String parentKey = childPost.getMetadata().getPostIdNum();
			recipientEmails.add(childPost.getMetadata().getEmailAddr());
			if (parentKey == 0) {
				rootData = childPost;
				item.setRootPostData(rootData);
				parentKey = intKey;
			} else {
				PostData parentData = item.getMessageMap().get(parentKey.toString());

				parentData.getChildMessages().put(childPost.getMessageId(), childPost);
			}
		}

		String recipientEmailList = "";
		Iterator iter = recipientEmails.iterator();
		while (iter.hasNext())
			recipientEmailList += iter.next() + ",";
		item.setRecipients(recipientEmailList);
		return rootData;
	}

	private boolean checkHOUSer(ThreadData item, Set<String> mailHOList) {

		Set<String> recepientsList = new HashSet<String>(Arrays.asList(item.getRecipients().split(",")));

		for (String mailId : recepientsList) {
			if (mailHOList.contains(mailId)) {
				return true;
			}
		}

		return false;
	}

		
	@BeforeStep
	public void initializeValues(StepExecution stepExecution) {

		hoMailId = (HashSet<String>)stepExecution.getJobExecution().getExecutionContext().get("HOMailList");
		
	}

}
