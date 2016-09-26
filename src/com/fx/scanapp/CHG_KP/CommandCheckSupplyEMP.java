package com.fx.scanapp.CHG_KP;

import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.DateType.EMPType;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;
import com.fx.scanapp.fileAnalyze.XMLException;

public class CommandCheckSupplyEMP extends TaskNode {
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
				if (FileAnalyze.ReadINI()) {
					Machine.cmand_bstatus=Machine.cmand_status;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "验证到无主管权限请刷主管权限";
				} 
				else 
				{
					if (!CheckSupplyEmp(msg)) {
						Machine.getInstance().nextdo = "送料权限错误请刷送料权限，不过之前请刷主管权限解锁";
						// PlayFailSound();
						FileAnalyze.WriteINI("1");
						Machine.cmand_bstatus=Machine.cmand_status;
						Machine.cmand_status = 2;
					} else {
						Machine.getInstance().nextdo = "送料权限正确\n请刷料盘";
						Machine.cmand_status = 4;
					}
				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
				
			}
		}).start();

	}

	public boolean CheckSupplyEmp(String msg) {
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tUserInfo.asmx");
		WEB.setMethod("GetUserInfo_Xml");
		HashMap map = new HashMap<String, String>();
		try {
			map.put("UserId",msg.split("-")[0]);
			map.put("PWD",msg.split("-")[1]);
		} catch (Exception e) {
			return false;
		}

		String str = JsonAnalyze.Jsoncreat(map);
		map.clear();
		map.put("Json", str);
		String result = (String) WEB.WebServices(map).toString().split("=")[1].split(";")[0];
		List<EMPType> master;
		try {
			if ((master = XMLAnalyze.getNewDataSet(result, (new EMPType())
					.getClass().getName())).size() > 0) {
				if (!master.get(0).ROLECAPTION.equalsIgnoreCase("系统开发员")) {//生产送料员
					Machine.getInstance().nextdo = "没有送料权限";
					return false;
				} else {
					Machine.getInstance().nextdo = "OK";
					Machine.getInstance().mlist=master.get(0);//记录送料人权限
					return true;
				}

			} else {
				return false;
			}
		} catch (XMLException e) {
			e.printStackTrace();
			Machine.getInstance().nextdo = "解析错误 " + e.toString();
			return false;
		} catch (Exception e) {
			Machine.getInstance().nextdo = "格式错误 " + e.toString();
			return false;
		}
	}
}


