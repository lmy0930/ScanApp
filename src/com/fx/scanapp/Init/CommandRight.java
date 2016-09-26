package com.fx.scanapp.Init;

import java.util.HashMap;

import android.os.Handler;
import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.Web.WEB;

public class CommandRight extends TaskNode {
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0x11) {
				Machine.getInstance().execute();
			}
		}
	};
	@Override
	public float getTaskLevel() {
		return taskLevel;
	}

	@Override
	public void doTask() {
		System.out.println("进行权限操作");
		try {
			Machine.getInstance().inputmsg = msg;
			String user = msg.split("-")[0];
			String PWD = msg.split("-")[1];
			if(PWD!=null)
			{
				Machine.getInstance().user =user;
				Machine.getInstance().PWD = PWD;
				//FX003358,1234
				new Thread(new Runnable() {

					@Override
					public void run() {
						// 调用web接口
						WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tUserInfo.asmx");
		            	HashMap map=new HashMap<String, String>();
		            	
		            	map.put("userId",Machine.getInstance().user);
		            	map.put("pwd", Machine.getInstance().PWD);		            	
		            	WEB.setMethod("ChkUserInfoIdAndPwd");
		            	String result=(String) WEB.WebServices(map).toString().split("=")[1].split(";")[0];
		            	if(result.equalsIgnoreCase("true"))
		            	{
		    				Machine.getInstance().nextdo = "用户信息已记录\n请刷备料表编号";
		    				Machine.cmand_status = 3;
		            	}
		            	else
		            	{
		    				Machine.getInstance().nextdo = "用户不存在";
		            	}
						Message msg = handler.obtainMessage();
						msg.what = 0x11;
						handler.sendMessage(msg);
					}
				}).start();

			}
			else
			{
				Machine.getInstance().nextdo = "权限格式不正确";
			}
		} catch (Exception e) {
			Machine.getInstance().nextdo = "权限格式错误"+e.toString();
		}
		Machine.getInstance().execute();

	}

	@Override
	public void setTaskLevel(int level) {
		taskLevel = level;

	}

}
