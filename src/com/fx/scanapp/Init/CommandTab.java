package com.fx.scanapp.Init;

import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.DateType.Condition;
import com.fx.scanapp.DateType.NewDataSet;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;


public class CommandTab extends TaskNode {
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
		System.out.println("���������ϱ����");
		Machine.getInstance().inputmsg = msg;
		if (msg.split(" ").length != 2) {
			Machine.getInstance().nextdo = "ˢ��ı��ϱ��ʽ����ȷ������������";
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// ����web�ӿ�
					WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
					HashMap map = new HashMap<String, String>();
					map.put("WOID", msg.split(" ")[1]);
					map.put("MASTERID", msg.split(" ")[0]);

					String str = JsonAnalyze.Jsoncreat(map);
					map.clear();
					map.put("Json", str);
					WEB.setMethod("GetMaterialPreparation_ForMobile");
					try {
						String result = (String) WEB.WebServices(map)
								.toString();
						if (result == null
								|| (Machine.getInstance().list = XMLAnalyze.getNewDataSet(
										result.split("=")[1].split(";")[0],
										(new NewDataSet()).getClass().getName()))
										.size() < 1) {
							Machine.getInstance().nextdo = "ENC����\n��ˢ��ȷ���ϱ���";
						} else {

							WEB.setMethod("CheckKpMasterIdStatus");
							map.clear();
							map.put("masterId", msg.split(" ")[0]);
							String rb = (String) WEB.WebServices(map)
									.toString().split("=")[1].substring(0, 4);
							if (!rb.equals("true")) {
								Machine.getInstance().nextdo = "��վ��û�о���Ʒ��ȷ��\n���ʵ";
							} else {
								WEB.setMethod("GetSmtIO_ForMobile");
								map.clear();
								map.put("Json", str);
								String rb2 = (String) WEB.WebServices(map)
										.toString().split("=")[1].split(";")[0];// ȥ������
								List<Condition> clist;
								if (rb2 == null
										|| (clist = XMLAnalyze.getNewDataSet(rb2,
												(new Condition()).getClass()
														.getName())).size() < 1) {
									Machine.getInstance().nextdo = "�˱��ϱ�û�б���,���ұ����ұ���\n��ˢ���ϱ���";
								} else {
									Machine.getInstance().nextdo = "���ϱ����Ѿ���¼\n��ˢ��ҵ����";
									Machine.getInstance().masterID = msg;
									Machine.getInstance().cmand_status = 0;
									Machine.getInstance().clist=clist;
									Log.v("WEB1", "xxxx====="+clist.size());
								}
							}
						}

					} catch (Exception e) {
						Machine.getInstance().nextdo = e.toString();
					}
					Message msg = handler.obtainMessage();
					msg.what = 0x11;
					handler.sendMessage(msg);
				}
			}).start();

		}
		Machine.getInstance().execute();

	}

	@Override
	public void setTaskLevel(int level) {
		taskLevel = level;

	}

}
