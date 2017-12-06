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
import org.springframework.core.io.Resource;

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

    @Value("file://WST-STSQ-000968/D$/EMBARK_Chat/EMBARK_Chat_Feed/NM_*.csv")
	//@Value("file://WSP-STSQ-000967/D$/Passenger_Chat/Passenger_Chat_Feed/NM_*.csv")

	private Resource[] resources;

	@Bean
	@StepScope
	public FlatFileItemReader<PassengerPost> passengerPostItemReader() {
		FlatFileItemReader<PassengerPost> reader = new FlatFileItemReader<PassengerPost>();
		final SimpleDateFormat sdf = new SimpleDateFormat("M-dd-YY");
		Date date = new Date(System.currentTimeMillis());	
	    String sdfDate = sdf.format(date).replaceAll("-", ".");	    
	    System.out.println(reader);
		reader.setLinesToSkip(2);
		reader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
		reader.setLineMapper(new DefaultLineMapper<PassengerPost>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] {"Username", "Email", "User Type", "Post Time", "Message",
						"Like Count", "Step"});
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
        writer.setSql("INSERT INTO dbo.EMBARK_CHAT (LastModifiedDtm, LastModifiedByNam, RowCreateDtm, RowCreatedByNam, UsernameTxt, MemberEmailAddrNam, UserTypeTxt, PostDtm, MessageTxt, LikeCountNum, StepTxt, ProcessStatusInd)" +
    			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    	writer.setDataSource(dataSource);
        return writer;
	}
	
	@Bean
	public ItemPreparedStatementSetter<ForumPost> setter() {
		return (item, ps) -> {
			DateTime date = (DateTime) DateParserUtil.parseDate(item.getPostTime());
			Timestamp timeStamp = new Timestamp(date.getMillis());
			ps.setTimestamp(1, DateParserUtil.getCurrentTimeStamp()); // LastModifiedDtm
			ps.setString(2, "SYSTEM"); // LastModifiedByNam
			ps.setTimestamp(3, DateParserUtil.getCurrentTimeStamp()); // RowCreateDtm
			ps.setString(4, "SYSTEM"); // RowCreateByNam
			ps.setString(5, item.getUsername()); // UsernameTxt
			if(item.getEmailAddr().equals("")){
				ps.setString(6, "");
			}else{
				ps.setString(6, item.getEmailAddr());
			} // MemberEmailAddrNam
			ps.setString(7, item.getUserType()); // UserTypeTxt
			ps.setTimestamp(8, timeStamp); // PostDtm
			ps.setString(9, item.getMessage()); // MessageTxt
			ps.setString(10, item.getLikeCount()); // LikeCountNum
			ps.setString(11, item.getStep()); // StepTxt
			ps.setInt(12, 0); // ProcessStatusInd 

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
