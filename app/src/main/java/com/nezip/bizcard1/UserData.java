package com.nezip.bizcard1;

public class UserData 
{

	String USER_EMAIL, USER_NAME, COMPANY_NAME, DEPT, GRADE, OFFICE_ADDR, UPMOO, 
	OFFICE_TEL, OFFICE_FAX, MOBILE, USER_USE_DT,
	WORK_EMAIL, TOP_PHONE, HOME_PAGE, KEYWORD, COMPANY_LOGO, MY_PHOTO, ID, REG_ID, HTML;
	

	UserData(String userEmail, String userName, String companyName, String dept, String grade, 
			 String officeAddr, String upmoo, String officeTel, String officeFax, String mobile, String userUseDt,
			 String workEmail, String topPhone, String homePage, String keyword, String companyLogo, 
			 String myPhoto, String id, String regId
			   )
	{

		setUserEmail(userEmail) ;
		setUserName(userName) ;
		setCompanyName(companyName) ;
		setDept(dept);
		setGu(grade);
		setOfficeAddr(officeAddr);
		setMessage(upmoo);

		setOfficeTel(officeTel);
		setOfficeFax(officeFax);
		setMobile(mobile);
		setUserUseDt(userUseDt);

		setWorkEmail(workEmail);
		setTopPhone(topPhone);
		setHomePage(homePage);
		setKeyword(keyword);
		setCompanyLogo(companyLogo);
		setMyPhoto(myPhoto);
		setId(id);
		setRegId(regId);
		
	}
	
	public String getUserEmail() {
		return USER_EMAIL;
	}
	public String getUserName() {
		return USER_NAME;
	}
	public String getCompanyName() {
		return COMPANY_NAME;
	}
	public String getdept() {
		return DEPT;
	}
	public String getgrade() {
		return GRADE;
	}
	public String getOfficeAddr() {
		return OFFICE_ADDR;
	}
	public String getUpmoo() {
		return UPMOO;
	}	
	public String getOfficeTel() {
		return OFFICE_TEL;
	}
	public String getOfficeFax() {
		return OFFICE_FAX;
	}
	public String getMobile() {
		return MOBILE;
	}
	public String getUserUseDt() {
		return USER_USE_DT;
	}

	public String getWorkEmail() {
		return WORK_EMAIL;
	}
	public String getTopPhone() {
		return TOP_PHONE;
	}
	public String getHomePage() {
		return HOME_PAGE;
	}
	public String getKeyword() {
		return KEYWORD;
	}
	public String getCompanyLogo() {
		return COMPANY_LOGO;
	}
	public String getMyPhoto() {
		return MY_PHOTO;
	}
	public String getId() {
		return ID;
	}
	public String getRegId() {
		return REG_ID;
	}
	public String getHtml() {
		return HTML;
	}


	public void setUserEmail(String userEmail) {
		USER_EMAIL = userEmail;
	}
	public void setUserName(String userName) {
		USER_NAME = userName;
	}
	public void setCompanyName(String companyName) {
		COMPANY_NAME = companyName;
	}
	public void setDept(String dept) {
		DEPT = dept;
	}
	public void setGu(String grade) {
		GRADE = grade;
	}
	public void setOfficeAddr(String officeAddr) {
		OFFICE_ADDR = officeAddr;
	}
	public void setMessage(String upmoo) {
		UPMOO = upmoo;
	}	
	public void setOfficeTel(String officeTel) {
		OFFICE_TEL = officeTel;
	}
	public void setOfficeFax(String officeFax) {
		OFFICE_FAX = officeFax;
	}
	public void setMobile(String mobile) {
		MOBILE = mobile;
	}	
	public void setUserUseDt(String userUseDt) {
		USER_USE_DT = userUseDt;
	}
	public void setWorkEmail(String WorkEmail) {
		WORK_EMAIL = WorkEmail;
	}
	public void setTopPhone(String TopPhone) {
		TOP_PHONE = TopPhone;
	}
	public void setHomePage(String HomePage) {
		HOME_PAGE = HomePage;
	}
	public void setKeyword(String Keyword) {
		KEYWORD = Keyword;
	}
	public void setCompanyLogo(String companyLogo) {
		COMPANY_LOGO = companyLogo;
	}
	public void setMyPhoto(String myPhoto) {
		MY_PHOTO = myPhoto;
	}
	public void setId(String id) {
		ID = id;
	}
	public void setRegId(String regId) {
		REG_ID = regId;
	}

	public void setHtml(String html) {
		HTML = html;
	}

}
