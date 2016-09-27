package com.fx.scanapp.DateType;

public class NewDataSet {
	private String WOID;
	private String PARTNUMBER;
	private String STATIONNO;
	private String MASTERID;
	private String KPNUMBER;
	private String BOMVER;
	private String SIDE;
	private String MACHINEID;
	private String MODELNAME;

	public void setWOID(String str) {
		WOID = str;
	}

	public void setPARTNUMBER(String str) {
		PARTNUMBER = str;
	}

	public void setSTATIONNO(String str) {
		STATIONNO = str;
	}

	public void setMASTERID(String str) {
		MASTERID = str;
	}

	public void setKPNUMBER(String str) {
		KPNUMBER = str;
	}

	public void setBOMVER(String str) {
		BOMVER = str;
	}

	public void setSIDE(String str) {
		SIDE = str;
	}

	public void setMACHINEID(String str) {
		MACHINEID = str;
	}

	public void setMODELNAME(String str) {
		MODELNAME = str;
	}
	
	public String getSIDE() {
		return SIDE;
	}
	
	public String getMODELNAME() {
		return MODELNAME;
	}

	public String getKPNUMBER() {
		return KPNUMBER;
	}
}
