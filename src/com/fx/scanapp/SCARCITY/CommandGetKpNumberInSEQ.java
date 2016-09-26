package com.fx.scanapp.SCARCITY;

import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.CommonTask.PublicMethods;
import com.fx.scanapp.DateType.Material;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;

public class CommandGetKpNumberInSEQ extends TaskNode {
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
					Machine.cmand_bstatus = Machine.cmand_status;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "请刷主管权限";
				} else {
					String Material;// Trsn信息
					if ((Material = PublicMethods.GetTrsnMaterial(msg,
							Machine.getInstance().masterID.split(" ")[1])) == null) {
						// PlayFailSound();
						FileAnalyze.WriteINI("1");
						Machine.cmand_bstatus=Machine.cmand_status;
						Machine.cmand_status = 2;
					} else {
						if (Material.split("\\|").length != 5) {
							Machine.getInstance().nextdo = "五合一条码格式错误\n请刷料盘编号.. ";
							// PlayFailSound();
							FileAnalyze.WriteINI("1");
						} else {
							List<Material> dt= GetKpNumberInSEQ(
									Machine.getInstance().masterID.split(" ")[0],
									Machine.getInstance().masterID.split(" ")[1],
									Material.split("\\|")[0], "");
							if (dt == null || dt.size() == 0) {
								Machine.getInstance().nextdo = "料号不在料站表内,请刷正确的五合一条码\n请刷料盘编号..";
								// PlayFailSound();
								FileAnalyze.WriteINI("1");
								Machine.cmand_bstatus=Machine.cmand_status;
								Machine.cmand_status = 2;

							} else {
								Machine.getInstance().data_KpNumberInSEQ=dt;
								Machine.cmand_status = 4;
								Machine.getInstance().material=Material;
								Machine.getInstance().nextdo = "缺料料号已经记录\n请刷料站编号..";
							}

						}
					}

				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private List<Material> GetKpNumberInSEQ(String MasterId,String WoId,String kpnumber,String station){
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
		WEB.setMethod("GetKpNumberInSEQ_ForMobile");
		HashMap map = new HashMap<String, String>();
		map.put("MASTERID", MasterId);
		map.put("WOID",WoId);
		map.put("KPNUMBER",kpnumber);
		map.put("STATION",station);
		String str = JsonAnalyze.Jsoncreat(map);
		map.clear();
		map.put("Json", str);
		String rb;
		List<Material> dt;
		try {
			rb = (String) WEB.WebServices(map).toString().split("=")[1]
					.split(";")[0];
			dt=XMLAnalyze.getNewDataSet(rb, (new Material()).getClass().getName().toString());
			return dt;
			
		} catch (Exception e) {
			dt = null;
			return dt;
		}		
	}
}
