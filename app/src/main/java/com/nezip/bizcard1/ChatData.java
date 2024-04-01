package com.nezip.bizcard1;

import android.view.View;

public class ChatData {
	int POSITION;
	String ID;
	String USER_EMAIL1;
	String USER_EMAIL2;

	String USER_NAME1;
	String USER_NAME2;

	String MESSAGE;
	String CREATE_DATE;
	View ROW_VIEW;

	ChatData(int position, String id, String userEmail1, String userEmail2,
			String userName1, String userName2, String message,
			String createDate) {
		setPosition(position);
		setId(id);
		setUserEmail1(userEmail1);
		setUserEmail2(userEmail2);
		setUserName1(userName1);
		setUserName2(userName2);
		setMessage(message);
		setCreateDate(createDate);
		setRowView(null);
	}

	public int getPosition() {
		return POSITION;
	}

	public String getId() {
		return ID;
	}

	public String getUserEmail1() {
		return USER_EMAIL1;
	}

	public String getUserEmail2() {
		return USER_EMAIL2;
	}

	public String getUserName1() {
		return USER_NAME1;
	}

	public String getUserName2() {
		return USER_NAME2;
	}

	public String getMessage() {
		return MESSAGE;
	}

	public String getCreateDate() {
		return CREATE_DATE;
	}

	public View getRowView() {
		return ROW_VIEW;
	}
	public void setPosition(int position) {
		POSITION = position;
	}

	public void setId(String id) {
		ID = id;
	}

	public void setUserEmail1(String userEmail1) {
		USER_EMAIL1 = userEmail1;
	}

	public void setUserEmail2(String userEmail2) {
		USER_EMAIL2 = userEmail2;
	}

	public void setUserName1(String userName1) {
		USER_NAME1 = userName1;
	}

	public void setUserName2(String userName2) {
		USER_NAME2 = userName2;
	}

	public void setMessage(String message) {
		MESSAGE = message;
	}

	public void setCreateDate(String createDate) {
		CREATE_DATE = createDate;
	}
	public void setRowView(View rowView) {
		ROW_VIEW = rowView;
	}

}
