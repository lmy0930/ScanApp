package com.fx.scanapp.DateType;

public class Condition {
	private String STATUS;
	private String MACHINEID;
	private String SIDE;
	private String MASTERID;
	private String WOID;
	
	public void setMASTERID(String str){
		MASTERID=str;
	}
	
	public String getMASTERID(){
		return MASTERID;
	}
	
	public void setWOID(String str){
		WOID=str;
	}
	
	public String getWOID(){
		return WOID;
	}

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
