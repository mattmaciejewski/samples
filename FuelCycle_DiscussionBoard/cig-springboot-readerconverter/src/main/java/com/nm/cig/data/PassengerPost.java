package com.nm.cig.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PassengerPost {
	private String commentID;// first line of CSV
	private String parentCommentID;
	private String commentLevel;
	private String username; 
	private String emailAddr;
	private String userType;
	private String postTime;
	private String message;
	private String votes;
	private String attachmentsCount;
	private String attachmentLinks;
	//private String step;

	/*
	 * Database Columns
		ForumTxt	VARCHAR(100),
		MemberIdTxt    VARCHAR(100),
		MemberStatusTxt     VARCHAR(50),
		MemberEmailAddrNam    VARCHAR(250),
		MemberScreenNameTxt    VARCHAR(25),
		ThreadNameTxt    VARCHAR(500),
		PostIdTxt    VARCHAR(50),
		ReplyToIdTxt    VARCHAR(50),
		PostStatusTxt    VARCHAR(25),
		PostContentTxt    VARCHAR(5000),
		AllTagsTxt    VARCHAR(500),
		AgreesNum SMALLINT,
		StarredInd SMALLINT,
		PostDtm DATE
	 */


	public PassengerPost (String commentID, String parentCommentID, String commentLevel, String username, String emailAddr, String userType, String postTime, String message,
			String votes, String attachmentsCount, String attachmentLinks) { //REMOVED STEP
		super();
		this.commentID = commentID;
		this.parentCommentID = parentCommentID;
		this.commentLevel = commentLevel;
		this.username = username;
		this.emailAddr = emailAddr;
		this.userType = userType;
		this.postTime = postTime;
		this.message = message;
		this.votes = votes;
		this.attachmentsCount = attachmentsCount;
		this.attachmentLinks = attachmentLinks;
		//this.step = step;
	}

	public PassengerPost() {

	}

	public String getCommentID() {
		return commentID;
	}

	public void setCommentID(String commentID) {
		this.commentID = commentID;
	}

	public String getParentCommentID() {
		return parentCommentID;
	}

	public void setParentCommentID(String parentCommentID) {
		this.parentCommentID = parentCommentID;
	}

	public String getCommentLevel() {
		return commentLevel;
	}

	public void setCommentLevel(String commentLevel) {
		this.commentLevel = commentLevel;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getVotes() {
		return votes;
	}

	public void setVotes(String votes) {
		this.votes = votes;
	}

	public String getAttachmentsCount() {
		return attachmentsCount;
	}

	public void setAttachmentsCount(String attachmentsCount) {
		this.attachmentsCount = attachmentsCount;
	}

	public String getAttachmentLinks() {
		return attachmentLinks;
	}

	public void setAttachmentLinks(String attachmentLinks) {
		this.attachmentLinks = attachmentLinks;
	}
	
	/*public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}
*/
	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		//Staff obj = new Staff();

		//Object to JSON in String
		String jsonInString="";
		try {
			jsonInString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonInString;
	}

}

