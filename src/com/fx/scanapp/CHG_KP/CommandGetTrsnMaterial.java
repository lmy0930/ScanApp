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
	public void doTask() {// ��ȡ������Ϣ
		//msg = "1512040002561";
		Machine.getInstance().inputmsg = msg;
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (FileAnalyze.ReadINI()) {
					Machine.cmand_bstatus = Machine.cmand_status;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "��ˢ����Ȩ��";
				} else {
					String Material;// Trsn��Ϣ
					if ((Material = PublicMethods.GetTrsnMaterial(msg,
							Machine.getInstance().masterID.split(" ")[1])) == null) {
						// PlayFailSound();
						FileAnalyze.WriteINI("1");
					} else {
						if (Material.split("\\|").length != 5) {
							Machine.getInstance().nextdo = "���һ�����ʽ����\n��ˢ���̱��.. ";
							// PlayFailSound();
							FileAnalyze.WriteINI("1");
						} else {
							if (!CheckKpInSEQ(
									Machine.getInstance().masterID.split(" ")[0],
									Machine.getInstance().masterID.split(" ")[1],
									Material.split("\\|")[0], "")) {
								Machine.getInstance().nextdo = "���ϺŲ������ϱ���\n��ˢ���̱��.. ";
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
									Machine.getInstance().nextdo = "�߲߱�δ����\n��ˢ���̱��.. ";
									// PlayFailSound();
									FileAnalyze.WriteINI("1");
									Machine.cmand_bstatus = Machine.cmand_status;
									Machine.cmand_status = 2;
								} else {
									if (CheckTrsn(Material)) {
										Machine.getInstance().nextdo += "\n��ˢ�����������к� ";
										// PlayFailSound();
										FileAnalyze.WriteINI("1");
										Machine.cmand_bstatus = Machine.cmand_status;
										Machine.cmand_status = 2;
									} else {
										Machine.getInstance().nextdo = "�������к�ˢ��ɹ�\n��ˢ��վ���";
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
	
	private boolean CheckKpInSEQ(String masterID, String woID, String KpNo,//������
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
        Machine.getInstance().����Model = dz .get(0).PARTNUMBER;
        Machine.getInstance().�������=  dz.get(0).SIDE;
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
		// �鿴�������Ƿ�����ڵ�ǰ�ϱ�
		// �ж������Ƿ����
		String[] arr�������к� = Material.split("\\|");
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
		WEB.setMethod("Check_MaterialScrap");
		HashMap map = new HashMap<String, String>();
		// map.put("UserId",msg.split("-")[0]);
		map.put("pn", arr�������к�[0]);
		map.put("vc", arr�������к�[1]);
		map.put("dc", arr�������к�[2]);
		map.put("lc", arr�������к�[3]);
		String rb;
		try {
			rb = (String) WEB.WebServices(map).toString().split("=")[1]
					.split(";")[0];
		} catch (Exception e) {
			rb = null;
			return true;
		}
		if (rb.equalsIgnoreCase("false")) {
			Machine.getInstance().nextdo = "����!! �����Ѿ����,���ܼ���ʹ��.";
			return true;
		}
		return false;

	}
}
