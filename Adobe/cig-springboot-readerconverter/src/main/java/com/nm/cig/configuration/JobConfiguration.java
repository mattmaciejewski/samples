/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nm.cig.configuration;

import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.nm.cig.data.Meeting;
import com.nm.cig.data.MeetingItemWriter;
import com.nm.cig.data.Message;
import com.nm.cig.data.ToUser;
import com.nm.cig.data.UserItemWriter;
import com.nm.cig.processor.UserItemProcessor;
import com.nm.cig.util.DateParserUtil;


/**
 * @author Matthew Maciejewski
 */
@Configuration
@EnableBatchProcessing
public class JobConfiguration {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;
	
	@Value("file://WST-STSQ-000968/D$/Passenger_Discussion/Passenger_Discussion_Feed/*.xml")
	private Resource[] resources;
	
	//public String meetingId;
	
	@Bean
	public StaxEventItemReader<Meeting> meetingReader(){
		StaxEventItemReader<Meeting> reader = new StaxEventItemReader<Meeting>();
		reader.setFragmentRootElementName("transcript");
		
		Jaxb2Marshaller meetingMarshaller = new Jaxb2Marshaller();
		meetingMarshaller.setClassesToBeBound(Meeting.class);
		
		reader.setUnmarshaller(meetingMarshaller);
		System.out.println("This Item Reader has completed");
		System.out.println(reader.toString());
		
		return reader;
	}
	
/*	@Bean
	public StaxEventItemReader<Message> messageReader(){
		StaxEventItemReader<Message> reader = new StaxEventItemReader<Message>();
		reader.setFragmentRootElementName("message");
		
		Jaxb2Marshaller meetingMarshaller = new Jaxb2Marshaller();
		meetingMarshaller.setClassesToBeBound(Message.class);
		
		reader.setUnmarshaller(meetingMarshaller);
		System.out.println("This Item Reader has completed");
		
		return reader;
	}
	
/*	@Bean
	public StaxEventItemReader<ToUser> userReader(){
		StaxEventItemReader<ToUser> reader = new StaxEventItemReader<ToUser>();
		reader.setFragmentRootElementName("to");
		
		Jaxb2Marshaller meetingMarshaller = new Jaxb2Marshaller();
		meetingMarshaller.setClassesToBeBound(ToUser.class);
		
		reader.setUnmarshaller(meetingMarshaller);
		System.out.println("This Item Reader has completed");
		
		return reader;
	}	
*/	
	@Bean
	public MultiResourceItemReader<Meeting> multiResourceMeetingItemReader(){
		MultiResourceItemReader<Meeting> multi = new MultiResourceItemReader<Meeting>();
		multi.setResources(resources);
		multi.setDelegate(meetingReader());
		
		return multi;
	}
	
/*	@Bean
	public MultiResourceItemReader<Message> multiResourceMessageItemReader(){
		MultiResourceItemReader<Message> multi = new MultiResourceItemReader<Message>();
		multi.setResources(resources);
		multi.setDelegate(messageReader());
		
		return multi;
	}
*/	
/*	@Bean
	public MultiResourceItemReader<ToUser> multiResourceUserItemReader(){
		MultiResourceItemReader<ToUser> multi = new MultiResourceItemReader<ToUser>();
		multi.setResources(resources);
		multi.setDelegate(userReader());
		
		return multi;
	}
*/
	
	@Bean
	public JdbcBatchItemWriter<Meeting> meetingWriter(ItemPreparedStatementSetter<Meeting> meetingSetter){
		JdbcBatchItemWriter<Meeting> meetingWriter = new JdbcBatchItemWriter<Meeting>();
		meetingWriter.setDataSource(dataSource);
		meetingWriter.setSql("insert into dbo.ADOBE_CONNECT_MEETING (MeetingId, MeetingUrl, MeetingName, PrimaryIndicator, LastModifiedDtm, LastModifiedByNam, RowCreateDtm, RowCreatedByNam, ProcessStatusInd) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		meetingWriter.setItemPreparedStatementSetter(meetingSetter);
		System.out.println("Meeting Writer has completed!");
		return meetingWriter;
	}
	
	@Bean
	public MeetingItemWriter<List<Message>> meetingMessageItemWriter(){
		MeetingItemWriter<List<Message>> writer = new MeetingItemWriter<List<Message>>();
		System.out.println(writer);
		return writer;
	}
	
	@Bean
	public UserItemWriter<List<ToUser>> meetingUserItemWriter(){
		UserItemWriter<List<ToUser>> writer = new UserItemWriter<List<ToUser>>();
		System.out.println(writer);
		return writer;
	}
/*	@Bean
	public JdbcBatchItemWriter<Message> messageWriter(ItemPreparedStatementSetter<Message> messageSetter){
		JdbcBatchItemWriter<Message> writer = new JdbcBatchItemWriter<Message>();
		writer.setDataSource(dataSource);
		writer.setSql("insert into dbo.ADOBE_CONNECT_MESSAGE (MeetingId, MessageTxt, MessageFrom, MessageDtm, LastModifiedDtm, LastModifiedByNam, RowCreateDtm, RowCreatedByNam) values (?, ?, ?, ?, ?, ?, ?, ?)");
		//for every message from Array{
		writer.setItemPreparedStatementSetter(messageSetter);
		//}
		System.out.println("Message Writer has completed!");
		return writer;
	}
*/	
/*	@Bean
	public JdbcBatchItemWriter<ToUser> userWriter(ItemPreparedStatementSetter<ToUser> userSetter){
		JdbcBatchItemWriter<ToUser> writer = new JdbcBatchItemWriter<ToUser>();
		writer.setDataSource(dataSource);
		writer.setSql("insert into dbo.ADOBE_CONNECT_USERS (MeetingId, UserName, UserEmail, UserLogin, UserUUID, LastModifiedDtm, LastModifiedByNam, RowCreateDtm, RowCreatedByNam, ProcessStatusInd) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		writer.setItemPreparedStatementSetter(userSetter);
		System.out.println("User Writer has completed!");
		return writer;
	}
*/	
	@Bean
	public ItemPreparedStatementSetter<Meeting> meetingSetter() {
		return (item, ps) -> {
			//meetingId = item.getMeetingScoId();
			ps.setString(1,  item.getMeetingScoId());//MeetingId
			ps.setString(2, item.getMeetingUrl()); //MeetingName
			ps.setString(3, item.getMeetingName()); //MeetingUrl
			ps.setString(4, "Y");
			ps.setTimestamp(5, DateParserUtil.getCurrentTimeStamp()); // LastModifiedDtm
			ps.setString(6, "SYSTEM"); // LastModifiedByNam
			ps.setTimestamp(7, DateParserUtil.getCurrentTimeStamp()); // RowCreateDtm
			ps.setString(8, "SYSTEM"); // RowCreatedByNam
			ps.setString(9, "0");
			System.out.println("Information has been read for Meeting");
		};
			
	}
	
/*	@Bean
	public ItemPreparedStatementSetter<Message> messageSetter() {
		return (item, ps) -> {

			DateTime messageTimestamp = (DateTime) DateParserUtil.parseUnixDate(item.getTimestamp());			
			Timestamp timestamp = new Timestamp(messageTimestamp.getMillis());
			ps.setString(1, item.getMeetingId());
			ps.setString(2,  item.getText());//MeetingId
			ps.setString(3, item.getFromUser().getName()); //MeetingName
			ps.setTimestamp(4, timestamp); //MeetingUrl
			ps.setTimestamp(5, DateParserUtil.getCurrentTimeStamp()); // LastModifiedDtm
			ps.setString(6, "SYSTEM"); // LastModifiedByNam
			ps.setTimestamp(7, DateParserUtil.getCurrentTimeStamp()); // RowCreateDtm
			ps.setString(8, "SYSTEM"); // RowCreatedByNam
			System.out.println("Information has been read for Messages");
			
			};
			
	}
	
	@Bean
	public UserItemProcessor itemProcessor(){
		return new UserItemProcessor();
	}
	
/*	@Bean
	public MessageItemProcessor messageItemProcessor(){
		return new MessageItemProcessor();
	}
	
	@Bean
	public ItemPreparedStatementSetter<ToUser> userSetter() {
		return (item, ps) -> {
				//ps.setString(1, meetingId);
				ps.setString(2, item.getName());//UserName
				ps.setString(3, item.getEmail()); //UserEmail
				ps.setString(4, item.getLogin()); //UserLogin
				ps.setString(5, "");//UserUUID
				ps.setTimestamp(6, DateParserUtil.getCurrentTimeStamp()); // LastModifiedDtm
				ps.setString(7, "SYSTEM"); // LastModifiedByNam
				ps.setTimestamp(8, DateParserUtil.getCurrentTimeStamp()); // RowCreateDtm
				ps.setString(9, "SYSTEM"); // RowCreatedByNam
				System.out.println("Information has been read for Users");
			};
	}
*/	
	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1")
				.<Meeting, Meeting> chunk(20)
				.reader(multiResourceMeetingItemReader())
				.writer(meetingWriter(meetingSetter()))
				.listener(new StepListener())
				.build();
		
	}
	
	@Bean
	public Step step2(){
		return stepBuilderFactory.get("step2")
				.<Meeting, List<Message>> chunk(1000)
				.reader(multiResourceMeetingItemReader())
				//.processor(messageItemProcessor())
				//.writer(messageWriter(messageSetter()))
				.writer(meetingMessageItemWriter())
				.listener(new StepListener())
				.build();
		
	}
	
	@Bean
	public Step step3(){
		return stepBuilderFactory.get("step3")
				.<Meeting, List<ToUser>> chunk(1000)
				.reader(multiResourceMeetingItemReader())
				//.processor(itemProcessor())
				.writer(meetingUserItemWriter())
				.listener(new StepListener())
				.build();
		
	}
	
	@Bean
	public Job importMeetingJob(){
		return jobBuilderFactory.get("importMeetingJob")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.next(step2())
				.next(step3())
				//.next(step4())
				.build();
	}
	
 
}
