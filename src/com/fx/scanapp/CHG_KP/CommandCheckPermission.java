package com.fx.scanapp.CHG_KP;

import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.DateType.EMPType;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;
import com.fx.scanapp.fileAnalyze.XMLException;

public class CommandCheckPermission extends TaskNode{
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
	public void setTaskLevel(int level) {
		taskLevel = level;
		
	}

	@Override
	public void doTask() {
		Machine.getInstance().inputmsg = msg;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
				if (msg.split("-").length > 0) {
					WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tUserInfo.asmx");
					WEB.setMethod("GetUserInfo_Xml");
					HashMap map = new HashMap<String, String>();
					map.put("UserId",msg.split("-")[0]);
					map.put("PWD",msg.split("-")[1]);
					String str = JsonAnalyze.Jsoncreat(map);
					map.put("Json", str);
		        	String result=(String) WEB.WebServices(map).toString().split("=")[1].split(";")[0];
		        	List<EMPType> master;
					try {
						if ((master=XMLAnalyze.getNewDataSet(result,
								(new EMPType()).getClass()
								.getName())).size()>0) {

							if (master.get(0).DEPTNAME.equalsIgnoreCase("������һ��")
									&&master.get(0).ROLECAPTION.equalsIgnoreCase("��������")) {
								FileAnalyze.WriteINI("0");
								Machine.getInstance().nextdo = "����Ȩ����ȷ\n�������һ��ָ��";
								Log.v("WEB", "����Ȩ��0��next="+Machine.cmand_bstatus);
								Machine.cmand_status=Machine.cmand_bstatus;
							} else {
								Machine.getInstance().nextdo = "����Ȩ�޴���3\n��ˢ����ȷ������Ȩ��";
							}

						} else {
							Machine.getInstance().nextdo = "����Ȩ�޴���\n��ˢ����ȷ������Ȩ��";
						}
					} catch (XMLException e) {
						e.printStackTrace();
						Machine.getInstance().nextdo =e.toString();
					}
				} else {
					Machine.getInstance().nextdo = "��һ���쳣δ���\n�������ܽ���";
				}
				} catch (Exception e) {
					Machine.getInstance().nextdo = "����쳣������½���";
				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
				
			}
		}).start();
	}
		
}
