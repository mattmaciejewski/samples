package com.nm.compliance.data;

import java.util.TreeMap;

import com.nm.compliance.tree.PostData;

public class ThreadData {
	private String threadName;
	private String printableString;
	private PostData rootPostData;
	private String recipients;
	
	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	
	private TreeMap<String,PostData> messageMap;

	public TreeMap<String,PostData> getMessageMap() {
		return messageMap;
	}

	public void setMessageMap(TreeMap<String,PostData> messageMap) {
		this.messageMap = messageMap;
	}

	public String getPrintableString() {
		return printableString;
	}

	public void setPrintableString(String printableString) {
		this.printableString = printableString;
	}

	public PostData getRootPostData() {
		return rootPostData;
	}

	public void setRootPostData(PostData rootPostData) {
		this.rootPostData = rootPostData;
	}

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

 
	
}
