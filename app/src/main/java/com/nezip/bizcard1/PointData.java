package com.nezip.bizcard1;

public class PointData {

	String ID, USER_EMAIL, USER_NAME, POINT, INPUT_NAME, INPUT_TIME, CHECK_OK,
			INSERT_DATE;

	PointData(String id, String userEmail, String userName, String point,
			String inputName, String inputTime, String checkOk,
			String insertDate) {

		setId(id);
		setUserEmail(userEmail);
		setUserName(userName);
		setPoint(point);
		setInputTime(inputTime);
		setInputName(inputName);
		setCheckOk(checkOk);
		setInsertDate(insertDate);

	}

	public String getUserEmail() {
		return USER_EMAIL;
	}

	public String getUserName() {
		return USER_NAME;
	}

	public String getPoint() {
		return POINT;
	}

	public String getId() {
		return ID;
	}

	public String getInputTime() {
		return INPUT_TIME;
	}

	public String getInputName() {
		return INPUT_NAME;
	}

	public String getCheckOk() {
		return CHECK_OK;
	}

	public String getInsertDate() {
		return INSERT_DATE;
	}

	public void setUserEmail(String userEmail) {
		USER_EMAIL = userEmail;
	}

	public void setUserName(String userName) {
		USER_NAME = userName;
	}

	public void setPoint(String point) {
		POINT = point;
	}

	public void setId(String id) {
		ID = id;
	}

	public void setInputTime(String inputTime) {
		INPUT_TIME = inputTime;
	}

	public void setInputName(String inputName) {
		INPUT_NAME = inputName;
	}

	public void setCheckOk(String checkOk) {
		CHECK_OK = checkOk;
	}

	public void setInsertDate(String insertDate) {
		INSERT_DATE = insertDate;
	}
}
