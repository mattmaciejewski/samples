package com.nm.cig.data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "transcript")
public class Meeting {

	private String meetingScoId;
	private String meetingName;
	private String meetingUrl;
	private Messages messages;
	

	@XmlElement(name = "meetingScoId")
	public String getMeetingScoId() {
		return meetingScoId;
	}
	public void setMeetingScoId(String meetingScoId) {
		this.meetingScoId = meetingScoId;
	}
	@XmlElement(name = "meetingName")
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	@XmlElement(name = "meetingUrl")
	public String getMeetingUrl() {
		return meetingUrl;
	}
	public void setMeetingUrl(String meetingUrl) {
		this.meetingUrl = meetingUrl;
	}
	@XmlElement(name = "messages")
	public Messages getMessages() {
		return messages;
	}
	public void setMessages(Messages messages) {
		this.messages = messages;
	}
	
}

	
	
