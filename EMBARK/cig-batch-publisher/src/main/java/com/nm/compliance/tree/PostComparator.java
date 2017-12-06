package com.nm.compliance.tree;

import java.util.Comparator;

public class PostComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		int o1Key=0, o2Key=0;
		try 
		{
			o1Key = Integer.parseInt(o1);
		}catch(Exception e)
		{}
		try 
		{
			o2Key = Integer.parseInt(o2);
		}catch(Exception e)
		{}	
		
		return o1Key-o2Key;
	}

}
