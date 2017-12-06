package com.nm.cig.data;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

/**
 * @author Rupinder Singh
 */
public class PassengerPostFieldSetMapper implements FieldSetMapper<PassengerPost> {

	@Override
	public PassengerPost mapFieldSet(FieldSet fieldSet) throws BindException {

	
	return new PassengerPost(fieldSet.readString("comment ID"), fieldSet.readString("Parent Comment ID"), 
				fieldSet.readString("Comment Level"),
				fieldSet.readString("Username"), fieldSet.readString("Email"),
				fieldSet.readString("User Type"),
				fieldSet.readString("Post Time"), fieldSet.readString("Message"),
				fieldSet.readString("Votes"), fieldSet.readString("Attachments Count"),
				fieldSet.readString("Attachment Links"));

	}
}

