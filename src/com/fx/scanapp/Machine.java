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
	
    
	public String user;//�û�
	public String masterID;//�ϱ����к�
	public String inputmsg;//������Ϣ
	public String nextdo;//��һ��ָʾ����
	public String PWD;//����
	public String materialID;
	public String station;//��վ
	public String Trsn;//����Trsn��Ϣ
	public String material;//���һ���룻
	public List<NewDataSet> list;
	public List<Condition> clist;
	public List<Material> data_KpNumberInSEQ;
	public EMPType mlist;//������Ȩ��
	public String ����Model;
	public String �������;

	

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
