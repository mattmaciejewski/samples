package com.nm.cig.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.nm.cig.processor.UserItemProcessor;
import com.nm.cig.util.DateParserUtil;

public class UserItemWriter<T> implements ItemWriter<T> {

	@Autowired
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	String sql = "insert into dbo.ADOBE_CONNECT_USERS (MeetingId, UserName, UserEmail, UserLogin, UserUUID, LastModifiedDtm, LastModifiedByNam, RowCreateDtm, RowCreatedByNam, ProcessStatusInd) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public void setDataSource(DataSource dataSource) {
		  this.dataSource = dataSource;
		  this.jdbcTemplate = new JdbcTemplate(this.dataSource);
		 }

		 public void insertUsers(ToUser singleUser) {
		  JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		  jdbcTemplate.update(sql, new PreparedStatementSetter() {

		   public void setValues(PreparedStatement ps)
		     throws SQLException {
		    
		    //DateTime messageTimestamp = (DateTime) DateParserUtil.parseUnixDate(singleMessage.getTimestamp());			
			//Timestamp timestamp = new Timestamp(messageTimestamp.getMillis());
			ps.setString(1, singleUser.getMeetingId());
			ps.setString(2, singleUser.getName());//UserName
			ps.setString(3, singleUser.getEmail()); //UserEmail
			ps.setString(4, singleUser.getLogin()); //UserLogin
			ps.setString(5, "");//UserUUID
			ps.setTimestamp(6, DateParserUtil.getCurrentTimeStamp()); // LastModifiedDtm
			ps.setString(7, "SYSTEM"); // LastModifiedByNam
			ps.setTimestamp(8, DateParserUtil.getCurrentTimeStamp()); // RowCreateDtm
			ps.setString(9, "SYSTEM"); // RowCreatedByNam
			ps.setString(10, "0");
			System.out.println("Information has been read for Users");
		   }
		  });

		 }
	
		 private Set<ToUser> existingUsers = new HashSet<ToUser>();

			public ToUser process(ToUser singleUser){
				existingUsers.size();
				if(existingUsers.contains(singleUser)){
					return null;
				}
				existingUsers.add(singleUser);
				for(ToUser temp : existingUsers){
					System.out.println(temp.getName());
				}
				return singleUser;
				}
	public void write(List<? extends T> items) throws Exception {
		List<Meeting> meeting = (List<Meeting>)items;
		List<Meeting> allMeetings = new ArrayList<Meeting>();
		List<Message> allMessages = new ArrayList<Message>();
		List<Message> message = new ArrayList<Message>();
		List<ToUser> user = new ArrayList<ToUser>();
		List<ToUser> allUsers = new ArrayList<ToUser>();

		for (Meeting singleMeeting: meeting){
			allMeetings.add(singleMeeting);
			message = singleMeeting.getMessages().getMessage();
			for (Message singleMessage: message){
				singleMessage.setMeetingId(singleMeeting.getMeetingScoId());
				allMessages.add(singleMessage);
				user = singleMessage.getToUser();
				for (ToUser singleUser: user){
					singleUser.setMeetingId(singleMeeting.getMeetingScoId());
					//process(singleUser);
					if(process(singleUser) != null){
					allUsers.add(process(singleUser));
					insertUsers(singleUser);
					}else{
						//Do Nothing
					}
					
					
				}
			}
			
		}
		
	}
	
}
