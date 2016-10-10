package com.fx.scanapp;

import com.fx.scanapp.CHG_CLS.CLSChain;
import com.fx.scanapp.CHG_KP.KPChain;
import com.fx.scanapp.CHG_WO.CHGWOChain;
import com.fx.scanapp.Init.InitChain;
import com.fx.scanapp.SCARCITY.SCARCITYChain;

public class ResponsibiltyFactory {
	/**
	 * author:刘梦雨 createdate:2016-9-18 description:本类采用静态工厂模式，可根据不同的任务产生对应的任务链，
	 * 降低复杂业务逻辑之间的耦合关系
	 */
	private static ResponsibiltyFactory factory;

	public static TaskNode TaskChain;

	public ResponsibiltyFactory() {
	}

	public static ResponsibiltyFactory getInstance() {// 获取单例
		if (factory != null) {
			return factory;
		} else {
			return new ResponsibiltyFactory();
		}

	}

	public TaskNode creatProduct(String action)// 生产对应任务链
	{
		if (action.equals("ACTION-INIT"))// 产生初始化子连
		{
			InitChain initchain = new InitChain();
			TaskChain = initchain.getChain();
			// 扫描到ACTION_Huanliao&&cm_status=0才产生子连
			// 子连工作步骤0，2，3，4编号
		}
		  if(action.equals("ACTION-CHG-CLS")&&(Machine.getInstance().cmand_status==0))//产生换班子连
		 {
		     CLSChain clschain=new CLSChain();
		     TaskChain=clschain.getChain();
		 }
		  if(action.equals("ACTION-CHG-WO")&&Machine.getInstance().cmand_status==0)//产生换线子连
		 {
		     CHGWOChain CHGWOch=new CHGWOChain();
		     TaskChain=CHGWOch.getChain();
		 }
		 if ((action.equals("ACTION-CHG-KP"))&& (Machine.getInstance().cmand_status == 0))// 产生换料子连
		{
			KPChain kpchain = new KPChain();
			TaskChain = kpchain.getChain();
		}
		 if(action.equals("ACTION-SCARCITY")&&Machine.getInstance().cmand_status==0)//产生缺料子连
		 {
		    SCARCITYChain scarcitychainChain=new SCARCITYChain();
		    TaskChain=scarcitychainChain.getChain();
		 }
		return TaskChain;
	}
}
