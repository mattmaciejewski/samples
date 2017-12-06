package com.nm.compliance.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
		ResultSet rs = selectStatement
				// .executeQuery("select * from dbo.FUEL_CYCLE_DISC_BOARD where CommentIdNum in
				// (select MIN(CommentIdNum) from dbo.FUEL_CYCLE_DISC_BOARD where
				// PROCESSSTATUSIND = 0)" );
				.executeQuery(
						"select * from dbo.FUEL_CYCLE_DISC_BOARD where ProcessStatusInd = 0 order by PostDtm ASC");
		// + item.getThreadName().replaceAll("'", "''") + "'");
		Statement stmt = null;
		stmt = conn.createStatement();
		int dataRows = rs.getRow();
		TreeMap<String, PostData> map = item.getMessageMap();
		if (map == null)
			map = new TreeMap<String, PostData>(new PostComparator());

		while (rs.next()) {
			try {

				PostMetaData postMetaData = new PostMetaData();

				postMetaData.setCommentIdNum(rs.getString("CommentIdNum"));
				postMetaData.setParentIdNum(rs.getString("ParentIdNum"));
				if (rs.wasNull())
					postMetaData.setParentIdNum("0");
				postMetaData.setCommentLevel(rs.getString("CommentLevel"));
				postMetaData.setEmailAddr(rs.getString("MemberEmailAddrNam"));
				postMetaData.setUsername(rs.getString("UsernameTxt"));
				// postMetaData.setStep(rs.getString("STEPTXT"));
				postMetaData.setUserType(rs.getString("UserTypeTxT"));
				// postMetaData.setLikeCount(rs.getString("LIKECOUNTNUM"));
				postMetaData.setMessage(rs.getString("MessageTxt"));
				postMetaData.setVotes(rs.getString("Votes"));
				postMetaData.setAttachmentsCount(rs.getString("AttachmentsCount"));
				postMetaData.setAttachmentLinks(rs.getString("AttachmentLinks"));
				postMetaData.setPostDtm(rs.getTimestamp("PostDtm"));

				PostData postData = new PostData();
				postData.setProcessedStatus(rs.getInt("ProcessStatusInd"));
				postData.setMetadata(postMetaData);
				postData.setChildMessages(new TreeMap<String, PostData>(new PostComparator()));
				postData.setMessageId(postMetaData.getCommentIdNum());
				map.put(postMetaData.getCommentIdNum(), postData);
				// String stepTxt = String.valueOf(postMetaData.getCommentIdNum());
				// postData.setMessageId(stepTxt);

				// map.put(stepTxt, postData);

				stmt.executeUpdate("Update dbo.FUEL_CYCLE_DISC_BOARD Set ProcessStatusInd = 1 where CommentIDNum='"
						+ postMetaData.getCommentIdNum() + "'");
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
		builder.append(tabPrepender).append(postMetaData.getEmailAddr()).append("\t")
				.append(postMetaData.getPostDtm().toLocaleString()).append("\n");
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

	/*
	 * private String getTextForPost(PostData rootPostData, ThreadData item, int
	 * tabStops) { // TODO Auto-generated method stub StringBuilder builder = new
	 * StringBuilder(); String tabPrepender = ""; for (int i = 0; i < tabStops; i++)
	 * tabPrepender += "\t"; PostMetaData postMetaData = rootPostData.getMetadata();
	 * if(postMetaData.getEmailAddr().equalsIgnoreCase("")){
	 * builder.append(tabPrepender).append(postMetaData.getUsername()).append("\t")
	 * .append(postMetaData.getPostDtm().toLocaleString()).append("\n"); }else{
	 * builder.append(tabPrepender).append(postMetaData.getEmailAddr()).append("\t")
	 * .append(postMetaData.getPostDtm().toLocaleString()).append("\n"); }
	 * builder.append("\n");
	 * builder.append(tabPrepender).append(postMetaData.getMessage());
	 * builder.append("\n");
	 * //builder.append(tabPrepender).append("Tags :").append(postMetaData.
	 * getAllTagsTxt()).append("\n");
	 * builder.append(tabPrepender).append("Replies :" +
	 * rootPostData.getChildMessages().size()).append("\n\n"); Set<?> childSet =
	 * rootPostData.getChildMessages().entrySet(); if (childSet != null) {
	 * Iterator<?> iterator = childSet.iterator(); while (iterator.hasNext()) {
	 * Map.Entry mentry = (Map.Entry) iterator.next();
	 * builder.append(getTextForPost((PostData) mentry.getValue(), item, tabStops +
	 * 1)); } }
	 * 
	 * return builder.toString(); }
	 * 
	 * private PostData createHeirarchy(ThreadData item) {
	 * 
	 * PostData rootData = null; Set<String> keySet = item.getMessageMap().keySet();
	 * HashSet<String> recipientEmails = new HashSet<String>(); Integer parentKey =
	 * 0; for (String key : keySet) { Integer intKey = Integer.valueOf(key);
	 * PostData childPost = item.getMessageMap().get(key); //String parentKey =
	 * childPost.getMetadata().getPostIdNum();
	 * recipientEmails.add(childPost.getMetadata().getEmailAddr()); if (parentKey ==
	 * 0) { rootData = childPost; item.setRootPostData(rootData); parentKey =
	 * intKey; } else { PostData parentData =
	 * item.getMessageMap().get(parentKey.toString());
	 * 
	 * parentData.getChildMessages().put(childPost.getMessageId(), childPost); } }
	 * 
	 * String recipientEmailList = ""; Iterator iter= recipientEmails.iterator();
	 * while(iter.hasNext()) recipientEmailList+=iter.next()+",";
	 * item.setRecipients(recipientEmailList); return rootData; }
	 */
	private PostData createHeirarchy(ThreadData item) {

		PostData rootData = null;
		Set<String> keySet = item.getMessageMap().keySet();
		HashSet<String> recipientEmails = new HashSet<String>();
		for (String key : keySet) {
			PostData childPost = item.getMessageMap().get(key);
			String parentKey = childPost.getMetadata().getParentIdNum();
			String commentLevel = childPost.getMetadata().getCommentLevel();
			recipientEmails.add(childPost.getMetadata().getEmailAddr());
			if (parentKey.equals("0") && commentLevel.equals("0")) {
				rootData = childPost;
				item.setRootPostData(rootData);
			} else {
				PostData parentData = item.getMessageMap().get(parentKey);

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

		hoMailId = (HashSet<String>) stepExecution.getJobExecution().getExecutionContext().get("HOMailList");

	}

}
