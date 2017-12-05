package com.nm.cig.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.nm.cig.util.DateParserUtil;

public class MeetingItemWriter<T> implements ItemWriter<T>{
	
	@Autowired
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	String sql = "insert into dbo.ADOBE_CONNECT_MESSAGE (MeetingId, MessageTxt, MessageFrom, MessageDtm, LastModifiedDtm, LastModifiedByNam, RowCreateDtm, RowCreatedByNam, ProcessStatusInd) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public void setDataSource(DataSource dataSource) {
		  this.dataSource = dataSource;
		  this.jdbcTemplate = new JdbcTemplate(this.dataSource);
		 }

		 public void insertMessages(Message singleMessage) {
		  JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		  jdbcTemplate.update(sql, new PreparedStatementSetter() {

		   public void setValues(PreparedStatement ps)
		     throws SQLException {
		    
		    DateTime messageTimestamp = (DateTime) DateParserUtil.parseUnixDate(singleMessage.getTimestamp());			
			Timestamp timestamp = new Timestamp(messageTimestamp.getMillis());
			ps.setString(1, singleMessage.getMeetingId());
			ps.setString(2,  singleMessage.getText());//MeetingId
			ps.setString(3, singleMessage.getFromUser().getName()); //MeetingName
			ps.setTimestamp(4, timestamp); //MeetingUrl
			ps.setTimestamp(5, DateParserUtil.getCurrentTimeStamp()); // LastModifiedDtm
			ps.setString(6, "SYSTEM"); // LastModifiedByNam
			ps.setTimestamp(7, DateParserUtil.getCurrentTimeStamp()); // RowCreateDtm
			ps.setString(8, "SYSTEM"); // RowCreatedByNam
			ps.setString(9, "0");
			System.out.println("Information has been read for Messages");
		   }
		  });

		 }
	

	
	public void write(List<? extends T> items) throws Exception {
		List<Meeting> meeting = (List<Meeting>)items;
		List<Meeting> allMeetings = new ArrayList<Meeting>();
		List<Message> allMessages = new ArrayList<Message>();
		List<Message> message = new ArrayList<Message>();
		List<Messages> messages = (List<Messages>)items;

		for (Meeting singleMeeting: meeting){
			allMeetings.add(singleMeeting);
			message = singleMeeting.getMessages().getMessage();
			for (Message singleMessage: message){
				singleMessage.setMeetingId(singleMeeting.getMeetingScoId());
				allMessages.add(singleMessage);
				insertMessages(singleMessage);
			}
			
		}
		
	}
	
}
	
