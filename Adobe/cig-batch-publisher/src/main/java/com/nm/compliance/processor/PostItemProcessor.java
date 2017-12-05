package com.nm.compliance.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.nm.compliance.data.ThreadData;
import com.nm.compliance.tree.PostComparator;
import com.nm.compliance.tree.PostData;
import com.nm.compliance.tree.PostMetaData;

public class PostItemProcessor implements ItemProcessor<ThreadData, ThreadData> {
	@Autowired
	DataSource messageDataSource;

	public PostItemProcessor(DataSource messageDatasource) {
		// TODO Auto-generated constructor stub
		this.messageDataSource = messageDatasource;
	}

	@Override
	public ThreadData process(ThreadData item) throws Exception {
		// TODO Auto-generated method stub

		Connection conn = messageDataSource.getConnection();
		Statement selectStatement = conn.createStatement();
		Statement selectStatement2 = conn.createStatement();
		System.out.println("Reading for Thread Name: "+item.getThreadName());
		ResultSet rs = selectStatement
				.executeQuery("SELECT MEET.MeetingId, MEET.MeetingUrl, MEET.MeetingName, MESS.MessageId, MESS.MessageTxt, MESS.MessageFrom, MESS.MessageDtm, MESS.ProcessStatusInd FROM dbo.ADOBE_CONNECT_MEETING MEET INNER JOIN dbo.ADOBE_CONNECT_MESSAGE MESS ON MEET.MeetingId = MESS.MeetingId AND MESS.ProcessStatusInd = 0 AND MEET.MeetingName='" + item.getThreadName().replaceAll("'", "''")+"'" + "order by MessageDtm ASC");
		
		ResultSet rs2 = selectStatement2.executeQuery("SELECT USERS.UserId, USERS.Username, USERS.UserEmail, USERS.UserLogin, MEET.MeetingId FROM dbo.ADOBE_CONNECT_USERS USERS INNER JOIN dbo.ADOBE_CONNECT_MEETING MEET ON MEET.MeetingID = USERS.MeetingID");
		Statement stmt = null;
		stmt = conn.createStatement();
		int dataRows = rs.getRow();
		int dataRows2 = rs2.getRow();
		TreeMap<String, PostData> map = item.getMessageMap();
		if (map == null)
			map = new TreeMap<String, PostData>(new PostComparator());
		
		while (rs.next()) {
			try {
				
				PostMetaData postMetaData = new PostMetaData();
				
				postMetaData.setMeetingId(rs.getString("MeetingId"));
				/*postMetaData.setParentIdNum(rs.getString("ParentIdNum"));
				if (rs.wasNull())
					postMetaData.setParentIdNum("0");
				*/postMetaData.setMeetingUrl(rs.getString("MeetingUrl"));
				postMetaData.setMeetingName(rs.getString("MeetingName"));
				postMetaData.setMessageId(rs.getString("MessageId"));
				postMetaData.setMessageTxt(rs.getString("MessageTxt"));
				postMetaData.setMessageFrom(rs.getString("MessageFrom"));
	 			postMetaData.setMessageDtm(rs.getTimestamp("MessageDtm"));
		 
				PostData postData = new PostData();
				postData.setProcessedStatus(rs.getInt("ProcessStatusInd"));
				postData.setMetadata(postMetaData);
				postData.setChildMessages(new TreeMap<String, PostData>(new PostComparator()));
				postData.setMessageId(postMetaData.getMessageId());
				map.put(postMetaData.getMessageId(), postData);
				//String stepTxt = String.valueOf(postMetaData.getCommentIdNum());
				//postData.setMessageId(stepTxt);
				
				//map.put(stepTxt, postData);
				
				stmt.executeUpdate("Update dbo.ADOBE_CONNECT_MESSAGE Set ProcessStatusInd = 1 where MessageId ='" + postMetaData.getMessageId() + "'");
				stmt.executeUpdate("Update dbo.ADOBE_CONNECT_MEETING Set ProcessStatusInd = 1 where MeetingId = '" + postMetaData.getMeetingId() + "'");
				
				dataRows++;

			} catch (Exception ex) {
				System.out.println(ex.getStackTrace());
			}
			item.setMessageMap(map);
			//System.out.println(rs.getRow());
		}
		/*while (rs2.next()){
			try{
				item.setRecipients(rs2.getString("Username"));
				dataRows2++;
				
			}catch (Exception ex) {
				System.out.println(ex.getStackTrace());
			}
		}
		*/
		System.out.println("The Total Number of Processed Rows per Thread Name: " + dataRows);
		PostData rootPostData = createHeirarchy(item);
		StringBuilder textOutPut = new StringBuilder();		textOutPut.append("Thread :").append(item.getThreadName()).append("\n");

		textOutPut.append(getTextForPost(rootPostData, item, 0));
		item.setPrintableString(textOutPut.toString());
		// System.out.println("root :" + rootPostData.getMessageId() + ":" +
		// rootPostData.getMessageId() + ":"+
		// rootPostData.getMetadata().getReplyToIdTxt());
		
		return item;
	}

/*	private String getTextForPost(PostData rootPostData, ThreadData item, int tabStops) {
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
		//builder.append(tabPrepender).append("Tags :").append(postMetaData.getAllTagsTxt()).append("\n");
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
*/	private String getTextForPost(PostData rootPostData, ThreadData item, int tabStops) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		String tabPrepender = "";
		for (int i = 0; i < tabStops; i++)
			tabPrepender += "\t";
		PostMetaData postMetaData = rootPostData.getMetadata();
		if(postMetaData.getMessageFrom().equalsIgnoreCase("")){
			builder.append(tabPrepender).append(postMetaData.getMessageFrom()).append("\t")
			.append(postMetaData.getMessageDtm().toLocaleString()).append("\n");
		}else{
		builder.append(tabPrepender).append(postMetaData.getMessageFrom()).append("\t")
				.append(postMetaData.getMessageDtm().toLocaleString()).append("\n");
		}
		builder.append("\n");
		builder.append(tabPrepender).append(postMetaData.getMessageTxt());
		builder.append("\n");
		//builder.append(tabPrepender).append("Tags :").append(postMetaData.getAllTagsTxt()).append("\n");
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
			//String parentKey = childPost.getMetadata().getPostIdNum();
			recipientEmails.add(childPost.getMetadata().getMessageFrom());
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
		Iterator iter= recipientEmails.iterator();
		while(iter.hasNext())
			recipientEmailList+=iter.next()+",";
		item.setRecipients(recipientEmailList);
		return rootData;
	}

/*	private PostData createHeirarchy(ThreadData item) {
		
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
		Iterator iter= recipientEmails.iterator();
		while(iter.hasNext())
			recipientEmailList+=iter.next()+",";
		item.setRecipients(recipientEmailList);
		return rootData;
	}*/
	
}
