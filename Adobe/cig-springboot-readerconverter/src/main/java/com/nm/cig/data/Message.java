package com.nm.cig.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
public class Message {
	
	private String meetingId;
	private FromUser fromUser;
	private List<ToUser> toUser;
	private String text;
	private String timestamp;
	
	public Message(String meetingId, FromUser fromUser, List<ToUser> toUser, String text, String timestamp) {
		super();
		this.meetingId = meetingId;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.text = text;
		this.timestamp = timestamp;
	}
	
	public Message(){
		
	}
	
	@XmlElement(name = "from")
	public FromUser getFromUser() {
		return fromUser;
	}
	public void setFromUser(FromUser fromUser) {
		this.fromUser = fromUser;
	}
	@XmlElement(name = "to")
	public List<ToUser> getToUser() {
		return toUser;
	}
	public void setToUser(List<ToUser> toUser) {
		this.toUser = toUser;
	}
	@XmlElement(name = "text")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@XmlElement(name = "timestamp")
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	
}
