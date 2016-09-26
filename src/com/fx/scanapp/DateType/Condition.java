package com.fx.scanapp.DateType;

public class Condition {
	private String STATUS;
	private String MACHINEID;
	private String SIDE;

	public void setSTATUS(String str) {
		STATUS = str;
	}

	public void setSIDE(String str) {
		SIDE = str;
	}

	public void setMACHINEID(String str) {
		MACHINEID = str;
	}
	
	public String getSTATUS() {
		return STATUS;
	}

	public String getSIDE() {
		return SIDE;
	}

	public String getMACHINEID() {
		return MACHINEID;
	}

}
