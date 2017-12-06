
package com.nm.compliance.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.nm.compliance.data.ThreadData;


@Configuration
@EnableBatchProcessing
public class PostConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	DataSource messageDataSource;;

	
	@Bean
	public JdbcCursorItemReader<ThreadData> reader()
	{
		JdbcCursorItemReader<ThreadData> forumReader = new JdbcCursorItemReader<ThreadData>();
	 
		//messageDataSource= new DriverManagerDataSource("jdbc:sqlserver://WSP-STSQ-000967;databaseName=ECPCIGP","VNTGSKPP","xxxxxx");
		messageDataSource= new DriverManagerDataSource("jdbc:sqlserver://WST-STSQ-000968;databaseName=ECPCIG","VNTGSKPT","xxxxxx");
		System.out.println("Initializing reader:");
		forumReader.setDataSource(messageDataSource);
		forumReader.setSql("Select distinct StepTxt from dbo.FUEL_CYCLE_CHAT WHERE PROCESSSTATUSIND=0");
		forumReader.setRowMapper(new RowMapper<ThreadData>() {
			
			@Override
			public ThreadData mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				ThreadData messageRawData = new ThreadData();
			 
				messageRawData.setThreadName(rs.getString("StepTxt"));
			 
				return messageRawData;
			}
		});
		return forumReader;
	}
	
	@Bean
	public PostItemProcessor processor(DataSource messageDatasource) {
		return new PostItemProcessor(messageDatasource);
	}
	      
	@Bean
	public PostItemWriter writer()
	{
		PostItemWriter writer = new PostItemWriter();
		return writer;
	}
	
	 @Bean
	    public JobExecutionListener listener() {
	        return new JobCompletionNotificationListener();
	    }
	 
	 @Bean
	    public Job importUserJob() {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());	
	    	String sdfDate = sdf.format(date);
	        return jobBuilderFactory.get("processMessages " + sdfDate)
	                .incrementer(new RunIdIncrementer())
	                .listener(listener())
	                .flow(step1())
	                .end()
	                .build();
	    }

	    @Bean
	    public Step step1() {
	        return stepBuilderFactory.get("step1")
	                .<ThreadData, ThreadData> chunk(1)
	                .reader(reader())
	                .processor(processor(messageDataSource))
	                .writer(writer())
	                .build();
	    }	    
	    
}
