package com.nm.compliance.tree;

import java.util.Date;

public class PostMetaData {
	
	private String meetingId;
	private String meetingUrl;
	private String meetingName;
	private String messageId;
	private String messageTxt;
	private String messageFrom;
	private Date messageDtm;
	
	public String getMeetingId() {
		return meetingId;
	}
	
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	
	public String getMeetingUrl() {
		return meetingUrl;
	}
	
	public void setMeetingUrl(String meetingUrl) {
		this.meetingUrl = meetingUrl;
	}
	
	public String getMeetingName() {
		return meetingName;
	}
	
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	
	public String getMessageId() {
		return messageId;
	}
	
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public String getMessageTxt() {
		return messageTxt;
	}
	
	public void setMessageTxt(String messageTxt) {
		this.messageTxt = messageTxt;
	}
	
	public String getMessageFrom() {
		return messageFrom;
	}
	
	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}
	
	public Date getMessageDtm() {
		return messageDtm;
	}
	
	public void setMessageDtm(Date messageDtm) {
		this.messageDtm = messageDtm;
	}

}
