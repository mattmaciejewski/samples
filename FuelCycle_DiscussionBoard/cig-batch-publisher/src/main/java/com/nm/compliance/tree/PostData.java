package com.nm.compliance.tree;

import java.util.SortedMap;

public class PostData {
	private String messageId;
	private int processedStatus;

	private PostMetaData metadata;
	private SortedMap<String, PostData> childMessages;

	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public PostMetaData getMetadata() {
		return metadata;
	}

	public void setMetadata(PostMetaData metadata) {
		this.metadata = metadata;
	}

	public SortedMap<String, PostData> getChildMessages() {
		return childMessages;
	}

	public void setChildMessages(SortedMap<String, PostData> childMessages) {
		this.childMessages = childMessages;
	}

	public int getProcessedStatus() {
		return processedStatus;
	}

	public void setProcessedStatus(int processedStatus) {
		this.processedStatus = processedStatus;
	}

}
