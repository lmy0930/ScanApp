package com.fx.scanapp.CHG_CLS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.CommonTask.PublicMethods;
import com.fx.scanapp.DateType.Material;
import com.fx.scanapp.DateType.NewDataSet;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;

public class CommandGetMaterialPreparation extends TaskNode {
	private List<String> stationno;
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

	public CommandGetMaterialPreparation() {
		Machine.getInstance().SmtKpMaterialPreparation = null;
		Machine.getInstance().stationno=null;
	}

	@Override
	public void doTask() {
		Machine.getInstance().inputmsg = msg;
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (FileAnalyze.ReadINI()) {//��֤����Ȩ��
					Machine.cmand_bstatus = Machine.cmand_status;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "��ˢ����Ȩ��";
				} else {
					String Material;// Trsn��Ϣ
					if ((Material = PublicMethods.GetTrsnMaterial(msg,
							Machine.getInstance().masterID.split(" ")[1])) == null) {
						// PlayFailSound();
						FileAnalyze.WriteINI("1");
						Machine.cmand_bstatus = Machine.cmand_status;
						Machine.cmand_status = 2;
					} else {
						if (Material.split("\\|").length != 5) {
							Machine.getInstance().nextdo = "���һ�����ʽ����\n��ˢ���̱��.. ";
							// PlayFailSound();
							FileAnalyze.WriteINI("1");
						} else {
							//��ȡ��վ����Ϣ
							if (Machine.getInstance().SmtKpMaterialPreparation == null) {
								Machine.getInstance().SmtKpMaterialPreparation = GetMaterialPreparation(
										Machine.getInstance().masterID
												.split(" ")[1], Machine
												.getInstance().masterID
												.split(" ")[0]);
								if (Machine.getInstance().SmtKpMaterialPreparation.size() < 1||Machine.getInstance().SmtKpMaterialPreparation == null) {
									Machine.getInstance().nextdo = "�����³�ʼ�� \nECN ���!!!!!!!!";
									// PlayFailSound();
									FileAnalyze.WriteINI("1");
								} else {
									//����վ����Ϣ����ȡ��վ������Ϣ
									Machine.getInstance().stationno = getStationno(Machine
											.getInstance().SmtKpMaterialPreparation);
								}
							} else {
								// �жϵڼ�����վ�Ƿ���������
								boolean flag = false;
								for (NewDataSet element : Machine.getInstance().SmtKpMaterialPreparation) {
									if (element.getSTATIONNO().equalsIgnoreCase(Machine.getInstance().stationno.get(0))) 
									{
										if (element.getKPNUMBER().equalsIgnoreCase(Material.split("\\|")[0])) 
										{
											flag = true;
											break;
										}
									}
								}
								if (!flag) {
									Machine.getInstance().nextdo = "ˢ���ϺŴ���\n�밴����վ��˳��ˢ��..";
									// PlayFailSound();
									FileAnalyze.WriteINI("1");
									Machine.cmand_bstatus = Machine.cmand_status;
									Machine.cmand_status = 2;
								} else {
									
									Machine.getInstance().material = Material;
									Machine.getInstance().nextdo = "�Ϻ��Ѿ���¼:"
											+ Material.split("\\|")[0]
											+ "��ˢ��վ���";
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

	private List<NewDataSet> GetMaterialPreparation(String WOID, String MASTERID) {
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
		WEB.setMethod("GetMaterialPreparation_ForMobile");
		HashMap map = new HashMap<String, String>();
		map.put("MASTERID", MASTERID);
		map.put("WOID", WOID);
		String str = JsonAnalyze.Jsoncreat(map);
		map.clear();
		map.put("Json", str);
		String dt;
		
		try {
			dt = (String) WEB.WebServices(map).toString().split("=")[1]
					.split(";")[0];
		} catch (Exception e) {
			return null;
		}
		try {
			List<NewDataSet> material = XMLAnalyze.getNewDataSet(dt,
					(new NewDataSet()).getClass().getName());
			return material;
			
		} catch (Exception e) {
			return null;

		}
	}

	private List<String> getStationno(List<NewDataSet> Table) {//��ȡ��վ
		List<String> stationno = new ArrayList<String>();
		String bStation = null;
		for (NewDataSet element : Table) {
			if (!element.getSTATIONNO().equalsIgnoreCase(bStation)) {
				bStation = element.getSTATIONNO();
				stationno.add(bStation);
			}
		}
		return stationno;

	}

}
