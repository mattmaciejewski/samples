
package com.nm.cig.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.nm.cig.data.ForumPost;
import com.nm.cig.data.PassengerPost;

/**
 * @author Rupinder Singh
 */
public class ConverterItemProcessor implements ItemProcessor<PassengerPost, ForumPost> {
	
	private static final Logger log = LoggerFactory.getLogger(ConverterItemProcessor.class);

	@Override
	public ForumPost process(PassengerPost item) throws Exception {
		
		return new ForumPost(item.getUsername(), item.getEmailAddr(), item.getUserType(),
				item.getPostTime(), item.getMessage(), item.getLikeCount(), item.getStep());	
	}
}
