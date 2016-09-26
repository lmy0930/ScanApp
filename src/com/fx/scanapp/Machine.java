package com.fx.scanapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.fx.scanapp.DateType.Condition;
import com.fx.scanapp.DateType.EMPType;
import com.fx.scanapp.DateType.Material;
import com.fx.scanapp.DateType.NewDataSet;

public class Machine extends Observable {
	private static Machine machine;
	
	public static int cmand_status=1;
	public static int cmand_bstatus=0;
	
    
	public String user;//用户
	public String masterID;//料表序列号
	public String inputmsg;//输入信息
	public String nextdo;//下一步指示操作
	public String PWD;//密码
	public String materialID;
	public String station;//料站
	public String Trsn;//物料Trsn信息
	public String material;//五合一条码；
	public List<NewDataSet> list;
	public List<Condition> clist;
	public List<Material> data_KpNumberInSEQ;
	public EMPType mlist;//送料人权限
	public String 换料Model;
	public String 换料面别;

	

	public Machine() {
	}

	public static Machine getInstance() {
		if (machine == null) {
			machine = new Machine();
		}
		return machine;
	}

	public void execute() {
		setChanged();
		notifyObservers();
	}

}
