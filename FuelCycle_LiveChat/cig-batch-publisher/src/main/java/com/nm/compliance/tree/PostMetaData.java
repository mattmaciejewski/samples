package com.nm.compliance.tree;

import java.util.Date;

public class PostMetaData {
	private int postIdNum;
	private String username;
	private String emailAddr;
	private String userType;
	private String message;
	private String likeCount;
	private String step;
	private Date postDtm;

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
	public Date getPostDtm() {
		return postDtm;
	}
	public void setPostDtm(Date postDtm) {
		this.postDtm = postDtm;
	}
	public int getPostIdNum() {
		return postIdNum;
	}
	public void setPostIdNum(int postIdNum) {
		this.postIdNum = postIdNum;
	}
 

}
