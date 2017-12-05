package com.nm.cig.data;

import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "messages")
public class Messages {
	
	private List<Message> message;

	@XmlElement(name = "message")
	public List<Message> getMessage() {
		return message;
	}

	public void setMessage(List<Message> message) {
		this.message = message;
	}
	
	

}
