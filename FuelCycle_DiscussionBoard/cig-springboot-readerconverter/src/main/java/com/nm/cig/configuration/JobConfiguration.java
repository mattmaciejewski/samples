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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourceArrayPropertyEditor;

import com.nm.cig.data.ForumPost;
import com.nm.cig.data.PassengerPost;
import com.nm.cig.processor.ConverterItemProcessor;
import com.nm.cig.util.DateParserUtil;


/**
 * @author Rupinder Singh
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
	
    @Value("file://WST-STSQ-000968/D$/Passenger_Discussion/Passenger_Discussion_Feed/NM_*.csv")
	//@Value("file://WSP-STSQ-000967/D$/Passenger_Discussion/Passenger_Discussion_Feed/NM_*.csv")
	
	private Resource[] resources;
	
	@Bean
	@StepScope
	public FlatFileItemReader<PassengerPost> passengerPostItemReader() {
		FlatFileItemReader<PassengerPost> reader = new FlatFileItemReader<PassengerPost>();
		final SimpleDateFormat sdf = new SimpleDateFormat("M-dd-YY");
		Date date = new Date(System.currentTimeMillis());	
	    String sdfDate = sdf.format(date).replaceAll("-", ".");	    
		System.out.println(reader);
		reader.setLinesToSkip(1);
		reader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
		reader.setLineMapper(new DefaultLineMapper<PassengerPost>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] {"comment ID", "Parent Comment ID", "Comment Level", "Username", "Email", "User Type", "Post Time", "Message",
						"Votes", "Attachments Count", "Attachment Links"}); //REMOVED STEP
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<PassengerPost>() {{
				setTargetType(PassengerPost.class);
			}});
		}});

		return reader;
	}
	
	@Bean
	public MultiResourceItemReader<PassengerPost> multiResourceItemReader(){
		MultiResourceItemReader<PassengerPost> multi = new MultiResourceItemReader<PassengerPost>();
		multi.setResources(resources);
		multi.setDelegate(passengerPostItemReader());
		
		return multi;
	}
	
	@Bean
	public ItemWriter<ForumPost> writer(DataSource dataSource, ItemPreparedStatementSetter<ForumPost> setter) {
        JdbcBatchItemWriter<ForumPost> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setItemPreparedStatementSetter(setter);
        writer.setSql("INSERT INTO dbo.FUEL_CYCLE_DISC_BOARD (CommentIdNum, ParentIDNum, CommentLevel, LastModifiedDtm, LastModifiedByNam, RowCreateDtm, RowCreatedByNam, UsernameTxt, MemberEmailAddrNam, UserTypeTxt, PostDtm, MessageTxt, Votes, AttachmentsCount, AttachmentLinks, ProcessStatusInd, StepTxt)" +
    			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    	writer.setDataSource(dataSource);
        return writer;
	}
	
	@Bean
	public ItemPreparedStatementSetter<ForumPost> setter() {
		return (item, ps) -> {
			DateTime date = (DateTime) DateParserUtil.parseDate(item.getPostTime());
			Timestamp timeStamp = new Timestamp(date.getMillis());
			ps.setString(1, item.getCommentID()); //CommentID
			if(item.getParentCommentID().equals("")){
				ps.setString(2, "0");
			}else{
				ps.setString(2, item.getParentCommentID());
			}//ParentCommentID
			ps.setString(3, item.getCommentLevel());  //CommentLevel
			ps.setTimestamp(4, DateParserUtil.getCurrentTimeStamp()); // LastModifiedDtm
			ps.setString(5, "SYSTEM"); // LastModifiedByNam
			ps.setTimestamp(6, DateParserUtil.getCurrentTimeStamp()); // RowCreateDtm
			ps.setString(7, "SYSTEM"); // RowCreateByNam
			ps.setString(8, item.getUsername()); // UsernameTxt
			if(item.getEmailAddr().equals("")){
				ps.setString(9, "");
			}else{
				ps.setString(9, item.getEmailAddr());
			} // MemberEmailAddrNam
			ps.setString(10, item.getUserType()); // UserTypeTxt
			ps.setTimestamp(11, timeStamp); // PostDtm
			ps.setString(12, item.getMessage().replaceAll("ÿ", " ")); // MessageTxt
			ps.setString(13, item.getVotes()); // Votes
			ps.setString(14, item.getAttachmentsCount());  //AttachmentsCount
			ps.setString(15, item.getAttachmentLinks());  //AttachmentLinks
			//ps.setString(11, item.getStep()); // StepTxt REMOVED STEP
			ps.setInt(16, 0); // ProcessStatusInd 
			ps.setString(17, "Discussion Board");

		};
			
	}
	

	@Bean
	public ConverterItemProcessor itemProcessor() {
		return new ConverterItemProcessor();
	}
	
	@Bean
	public Step step1(ItemWriter<ForumPost> writer) {
		return stepBuilderFactory.get("step1")
				.<PassengerPost, ForumPost>chunk(5000)
				//.reader(passengerPostItemReader())
				.reader(multiResourceItemReader())
				.processor(itemProcessor())
				.writer(writer)
				.listener(new StepListener())
				.build();
	}

	@Bean
	public Job job(Step s1) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());	
    	//Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    String sdfDate = sdf.format(date);
		return jobBuilderFactory.get("job " + sdfDate)
				.start(s1)
				.build();
	}
}
