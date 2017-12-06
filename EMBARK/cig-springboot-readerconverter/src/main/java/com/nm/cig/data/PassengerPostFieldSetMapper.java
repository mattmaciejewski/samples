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

	
	return new PassengerPost(fieldSet.readString("Username"), fieldSet.readString("Email"),
				fieldSet.readString("User Type"),
				fieldSet.readString("Post Time"), fieldSet.readString("Message"),
				fieldSet.readString("Like Count"),fieldSet.readString("Step"));

	}
}

