package com.nm.compliance.processor;

import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


import org.springframework.batch.item.ItemWriter;

import com.nm.compliance.data.ThreadData;

public class PostItemWriter implements ItemWriter<ThreadData> {
	
    @Override
    public void write(List<? extends ThreadData> items) throws Exception {
    	
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(System.currentTimeMillis());	
    	//Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    String sdfDate = sdf.format(date);
    	
    	for (ThreadData threadData : items) {
			FileWriter fileWriter = new FileWriter("//WST-STSQ-000968/D$/Adobe/Adobe_EML/" + threadData.getThreadName().replace("?", "") + "_" + sdfDate +".eml");
			//FileWriter fileWriter = new FileWriter("//WSP-STSQ-000967/D$/Passenger_Discussion/Passenger_Discussion_EML/" + threadData.getThreadName().replace("?", "") + "_" + sdfDate +".eml");
            fileWriter.write("x-sender: \r\n");
            fileWriter.write("x-receiver: otherecom@nml.digitalsafe.net\r\n");
			fileWriter.write("Date: "+threadData.getRootPostData().getMetadata().getMessageDtm()+"\r\n");
            fileWriter.write("From: "+ threadData.getRootPostData().getMetadata().getMessageFrom()+"\r\n");
            fileWriter.write("To: "+ threadData.getRecipients()+"\r\n");
            //fileWriter.write("Message-ID: <"+ threadData.getRootPostData().getMetadata().getMemberIdTxt()+">\r\n");
            fileWriter.write("Subject: "+threadData.getThreadName()+"\r\n");
            fileWriter.write("MIME-Version: 1.0\r\n");
            fileWriter.write("Content-Type: text/plain; charset=UTF-8\r\n"); 
            fileWriter.write("Content-Transfer-Encoding: quoted-printable\r\n"); 
            fileWriter.write("X-FaceTime-IMA-interID: "+ threadData.getRootPostData().getMessageId()+"\r\n");
            String uniqueId= UUID.randomUUID().toString();
            fileWriter.write("X-FaceTime-IMA-employeeID: "+threadData.getRootPostData().getMetadata().getMessageFrom()+"\r\n");
            fileWriter.write("X-FaceTime-IMA-buddyName: "+    threadData.getRootPostData().getMetadata().getMessageFrom()+"\r\n");
            fileWriter.write("X-FaceTime-IMA-supervisedFlag: false\r\n");
            fileWriter.write("X-FaceTime-IMA-internalFlag: true\r\n");
            fileWriter.write("X-FaceTime-IMA-exportID-interID: "+uniqueId+"\r\n");
            fileWriter.write("Message-ID: "+uniqueId+"\r\n");
            fileWriter.write("X-FaceTime-IMA-participants: "+threadData.getRecipients() +"\r\n");
            fileWriter.write("X-FaceTime-IMA-partsEmployeeIDs: "+ threadData.getRecipients() +"\r\n");
            fileWriter.write("X-FaceTime-IMA-networkName: AdobeConnect\r\n");
            fileWriter.write("X-FaceTime-IMA-startTime: "+ threadData.getRootPostData().getMetadata().getMessageDtm()+"\r\n");
            fileWriter.write("X-ZANTAZ-CONTENT-TYPE: Adobe\r\n");
            fileWriter.write("X-HearsaySocial-Network: AdobeConnect\r\n");
            fileWriter.write("X-HearsaySocial-ActivityType: Chat\r\n");
            fileWriter.write("X-HearsaySocial-PreApproved: false\r\n");
            fileWriter.write("X-FaceTime-IMA-parentURL:\r\n");
            fileWriter.write("X-FaceTime-IMA-resourceName: Chat\r\n");
            fileWriter.write("X-HearsaySocial-Target: \r\n");
            fileWriter.write("X-FaceTime-IMA-parentObjectID:\r\n");
            fileWriter.write("X-FaceTime-IMA-dateOfActivity: "+threadData.getRootPostData().getMetadata().getMessageDtm()+"\r\n\r\n\r\n");
            
            
            fileWriter.write(threadData.getPrintableString());
            fileWriter.close();
        }

    }

}