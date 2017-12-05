package com.nm.cig.processor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;

import com.nm.cig.data.ToUser;

public class UserItemProcessor implements ItemProcessor <ToUser, ToUser> {

	private Set<ToUser> existingUsers = new HashSet<ToUser>();

	public ToUser process(ToUser item){
		existingUsers.size();
		if(existingUsers.contains(item)){
			return null;
		}
		existingUsers.add(item);
		for(ToUser temp : existingUsers){
			System.out.println(temp.getName());
		}
		return item;
		}
}
