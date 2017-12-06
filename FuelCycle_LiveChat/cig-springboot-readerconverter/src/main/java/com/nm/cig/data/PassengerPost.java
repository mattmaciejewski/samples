package com.nm.cig.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PassengerPost {
	private String username; // first line of CSV
	private String emailAddr;
	private String userType;
	private String postTime;
	private String message;
	private String likeCount;
	private String step;

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


	public PassengerPost(String username, String emailAddr, String userType, String postTime, String message,
			String likeCount, String step) {
		super();
		this.username = username;
		this.emailAddr = emailAddr;
		this.userType = userType;
		this.postTime = postTime;
		this.message = message;
		this.likeCount = likeCount;
		this.step = step;
	}

	public PassengerPost() {

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

	public String getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

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

