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
		System.out.println("����Ȩ�޲���");
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
						// ����web�ӿ�
						WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tUserInfo.asmx");
		            	HashMap map=new HashMap<String, String>();
		            	
		            	map.put("userId",Machine.getInstance().user);
		            	map.put("pwd", Machine.getInstance().PWD);		            	
		            	WEB.setMethod("ChkUserInfoIdAndPwd");
		            	String result=(String) WEB.WebServices(map).toString().split("=")[1].split(";")[0];
		            	if(result.equalsIgnoreCase("true"))
		            	{
		    				Machine.getInstance().nextdo = "�û���Ϣ�Ѽ�¼\n��ˢ���ϱ���";
		    				Machine.cmand_status = 3;
		            	}
		            	else
		            	{
		    				Machine.getInstance().nextdo = "�û�������";
		            	}
						Message msg = handler.obtainMessage();
						msg.what = 0x11;
						handler.sendMessage(msg);
					}
				}).start();

			}
			else
			{
				Machine.getInstance().nextdo = "Ȩ�޸�ʽ����ȷ";
			}
		} catch (Exception e) {
			Machine.getInstance().nextdo = "Ȩ�޸�ʽ����"+e.toString();
		}
		Machine.getInstance().execute();

	}

	@Override
	public void setTaskLevel(int level) {
		taskLevel = level;

	}

}
