package com.fx.scanapp.CHG_KP;

import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.CommonTask.PublicMethods;
import com.fx.scanapp.DateType.KpNumberInSEQ;
import com.fx.scanapp.DateType.Kpnumber;
import com.fx.scanapp.DateType.Material;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;
import com.fx.scanapp.fileAnalyze.XMLException;

public class CommandGetTrsnMaterial extends TaskNode {
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
	public void doTask() {// 获取条码信息
		//msg = "1512040002561";
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
					} else {
						if (Material.split("\\|").length != 5) {
							Machine.getInstance().nextdo = "五合一条码格式错误\n请刷料盘编号.. ";
							// PlayFailSound();
							FileAnalyze.WriteINI("1");
						} else {
							if (!CheckKpInSEQ(
									Machine.getInstance().masterID.split(" ")[0],
									Machine.getInstance().masterID.split(" ")[1],
									Material.split("\\|")[0], "")) {
								Machine.getInstance().nextdo = "此料号不存在料表内\n请刷料盘编号.. ";
								// PlayFailSound();
								FileAnalyze.WriteINI("1");
								Machine.cmand_bstatus = Machine.cmand_status;
								Machine.cmand_status = 2;
							} else {
								if (!CheckKpnumberSupply(
										Machine.getInstance().masterID
												.split(" ")[0], Machine
												.getInstance().masterID
												.split(" ")[1], Material
												.split("\\|")[0], "1", msg)) {
									Machine.getInstance().nextdo = "线边仓未补料\n请刷料盘编号.. ";
									// PlayFailSound();
									FileAnalyze.WriteINI("1");
									Machine.cmand_bstatus = Machine.cmand_status;
									Machine.cmand_status = 2;
								} else {
									if (CheckTrsn(Material)) {
										Machine.getInstance().nextdo += "\n请刷入新料盘序列号 ";
										// PlayFailSound();
										FileAnalyze.WriteINI("1");
										Machine.cmand_bstatus = Machine.cmand_status;
										Machine.cmand_status = 2;
									} else {
										Machine.getInstance().nextdo = "料盘序列号刷入成功\n请刷料站编号";
										Machine.getInstance().materialID = Material;
										Machine.getInstance().Trsn = msg;
										Machine.cmand_status = 5;
									}
								}

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
	
	private boolean CheckKpInSEQ(String masterID, String woID, String KpNo,//有问题
			String station) {
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
		WEB.setMethod("GetKpNumberInSEQ_ForMobile");
		HashMap map = new HashMap<String, String>();
		map.put("MASTERID", masterID);
		map.put("WOID", woID);
		map.put("KPNUMBER", KpNo);
		map.put("STATION", "");
		String str = JsonAnalyze.Jsoncreat(map);
		map.clear();
		map.put("Json", str);
        String rb = (String) WEB.WebServices(map).toString().split("=")[1].split(";")[0];
        List<KpNumberInSEQ> dz = null;
		try {
			dz = XMLAnalyze.getNewDataSet(rb, (new KpNumberInSEQ()).getClass().getName().toString());
		} catch (XMLException e) {
			
			e.printStackTrace();
			 return false;
		}
        if (dz.size() < 1)
        {
            return false;
        }
        Machine.getInstance().换料Model = dz .get(0).PARTNUMBER;
        Machine.getInstance().换料面别=  dz.get(0).SIDE;
		return true;

	}

	private boolean CheckKpnumberSupply(String masterID, String woID,
			String kpNumber, String cDate, String msg) {
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
		WEB.setMethod("CheckKpSupply_ForMobile");
		HashMap map = new HashMap<String, String>();
		map.put("MASTERID", masterID);
		map.put("WOID", woID);
		map.put("STATION", "");
		map.put("KPNUMBER", kpNumber);
		map.put("CDATA", cDate);
		String str = JsonAnalyze.Jsoncreat(map);
		map.clear();
		map.put("Json", str);
		String dt;
		try {
			dt = (String) WEB.WebServices(map).toString().split("=")[1]
					.split(";")[0];
		} catch (Exception e) {
			dt = null;
			return false;
		}
		Log.v("WEB", "CheckKpSupply_ForMobile");
		try {
			List<Kpnumber> kp = XMLAnalyze.getNewDataSet(dt, (new Kpnumber())
					.getClass().getName());
			if (kp.size() < 1) {
				return false;
			} else {
				if (!kp.get(0).TRSN.equalsIgnoreCase(msg)) {
					Log.v("WEB", kp.get(0).TRSN);
					return false;
				}
			}
		} catch (XMLException e) {
			e.printStackTrace();
		}
		return true;
		/**
		 * "><NewDataSet>
		 * <Table>
		 * <ROWID>761193</ROWID> <MASTERID>S021672</MASTERID>
		 * <WOID>702000973</WOID> <MACHINEID>060101SL</MACHINEID>
		 * <STATIONNO>0113L</STATIONNO> <CDATA>1</CDATA>
		 * <KPNUMBER>AF-C1000683</KPNUMBER>
		 * <SCARCITYTIME>2015-12-23T21:10:18+08:00</SCARCITYTIME>
		 * <SUPPLYUSER>K01408</SUPPLYUSER> <FLAG>0</FLAG> <QTY>10000</QTY>
		 * <VENDERCODE>200216</VENDERCODE> <DATECODE>20151204</DATECODE>
		 * <LOTID>20151204</LOTID> <TRSN>1512040002561</TRSN>
		 * </Table>
		 * </NewDataSet></string>
		 * */
	}

	private boolean CheckTrsn(String Material) {
		if (Material == null)
			return true;
		// 查看该物料是否存在于当前料表
		// 判定物料是否清仓
		String[] arr料盘序列号 = Material.split("\\|");
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
		WEB.setMethod("Check_MaterialScrap");
		HashMap map = new HashMap<String, String>();
		// map.put("UserId",msg.split("-")[0]);
		map.put("pn", arr料盘序列号[0]);
		map.put("vc", arr料盘序列号[1]);
		map.put("dc", arr料盘序列号[2]);
		map.put("lc", arr料盘序列号[3]);
		String rb;
		try {
			rb = (String) WEB.WebServices(map).toString().split("=")[1]
					.split(";")[0];
		} catch (Exception e) {
			rb = null;
			return true;
		}
		if (rb.equalsIgnoreCase("false")) {
			Machine.getInstance().nextdo = "错误!! 该料已经清仓,不能继续使用.";
			return true;
		}
		return false;

	}
}
