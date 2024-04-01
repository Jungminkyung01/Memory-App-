package com.nezip.bizcard1;

public class PostData 
{

	String POST_NO, POST_ADDR, USE_ADDR;
	

	PostData(String postNo, String postAddr, String useAddr)
	{

		setPostNo(postNo);
		setPostAddr(postAddr);
		setUseAddr(useAddr);
		
	}
	
	public String getPostNo() {
		return POST_NO;
	}
	public String getPostAddr() {
		return POST_ADDR;
	}
	public String getUseAddr() {
		return USE_ADDR;
	}
	
	public void setPostNo(String postNo) {
		POST_NO = postNo;
	}
	public void setPostAddr(String postAddr) {
		POST_ADDR = postAddr;
	}
	public void setUseAddr(String useAddr) {
		USE_ADDR = useAddr;
	}
	

}
