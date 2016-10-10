package com.fx.scanapp;

import com.fx.scanapp.CHG_CLS.CLSChain;
import com.fx.scanapp.CHG_KP.KPChain;
import com.fx.scanapp.CHG_WO.CHGWOChain;
import com.fx.scanapp.Init.InitChain;
import com.fx.scanapp.SCARCITY.SCARCITYChain;

public class ResponsibiltyFactory {
	/**
	 * author:������ createdate:2016-9-18 description:������þ�̬����ģʽ���ɸ��ݲ�ͬ�����������Ӧ����������
	 * ���͸���ҵ���߼�֮�����Ϲ�ϵ
	 */
	private static ResponsibiltyFactory factory;

	public static TaskNode TaskChain;

	public ResponsibiltyFactory() {
	}

	public static ResponsibiltyFactory getInstance() {// ��ȡ����
		if (factory != null) {
			return factory;
		} else {
			return new ResponsibiltyFactory();
		}

	}

	public TaskNode creatProduct(String action)// ������Ӧ������
	{
		if (action.equals("ACTION-INIT"))// ������ʼ������
		{
			InitChain initchain = new InitChain();
			TaskChain = initchain.getChain();
			// ɨ�赽ACTION_Huanliao&&cm_status=0�Ų�������
			// ������������0��2��3��4���
		}
		  if(action.equals("ACTION-CHG-CLS")&&(Machine.getInstance().cmand_status==0))//������������
		 {
		     CLSChain clschain=new CLSChain();
		     TaskChain=clschain.getChain();
		 }
		  if(action.equals("ACTION-CHG-WO")&&Machine.getInstance().cmand_status==0)//������������
		 {
		     CHGWOChain CHGWOch=new CHGWOChain();
		     TaskChain=CHGWOch.getChain();
		 }
		 if ((action.equals("ACTION-CHG-KP"))&& (Machine.getInstance().cmand_status == 0))// ������������
		{
			KPChain kpchain = new KPChain();
			TaskChain = kpchain.getChain();
		}
		 if(action.equals("ACTION-SCARCITY")&&Machine.getInstance().cmand_status==0)//����ȱ������
		 {
		    SCARCITYChain scarcitychainChain=new SCARCITYChain();
		    TaskChain=scarcitychainChain.getChain();
		 }
		return TaskChain;
	}
}
