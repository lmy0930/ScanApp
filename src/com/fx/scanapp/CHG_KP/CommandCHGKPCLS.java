package com.fx.scanapp.CHG_KP;

import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.DateType.ColParameter;
import com.fx.scanapp.DateType.Kpnumber;
import com.fx.scanapp.DateType.Material;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;
import com.fx.scanapp.fileAnalyze.XMLException;

public class CommandCHGKPCLS extends TaskNode implements ColParameter {
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
				if (Machine.getInstance().station== null) {
					 if (!CHgKp())
					 {
					 Machine.getInstance().materialID=null;
					 //PlayFailSound();
					 FileAnalyze.WriteINI("1");
						Machine.cmand_bstatus = Machine.cmand_status;
						Machine.cmand_status = 2;
					 }
					 else
					 {
					   Machine.getInstance().nextdo ="������� ";
				       Machine.getInstance().nextdo +="\n��ˢ��ҵ����..";
						Machine.cmand_status = 0;
					}
				} else {
					 Machine.getInstance().nextdo ="����!!û��ˢ��ʼ������,�����¿�ʼ..";
					 FileAnalyze.WriteINI("1");
					// PlayFailSound();
				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
				
			}
		}).start();
	}

	private boolean CHgKp() {
		String[] arr�������к� = Machine.getInstance().materialID.split("\\|");
		if (Machine.getInstance().masterID != null
				&& Machine.getInstance().materialID != null && msg != null)// ��վ��Ϊ��
		{
			WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
			WEB.setMethod("CheckKpSupply_ForMobile");
			HashMap<String, String> mst = new HashMap<String, String>();
			mst.put("MASTERID", Machine.getInstance().masterID.split(" ")[0]);
			mst.put("WOID", Machine.getInstance().masterID.split(" ")[1]);
			mst.put("STATION", msg);
			mst.put("KPNUMBER","");
			mst.put("CDATA", String.valueOf(ColParameter.�Ѿ�����));
			String str = JsonAnalyze.Jsoncreat(mst);
			mst.clear();
			mst.put("Json", str);
			String rb = (String) WEB.WebServices(mst).toString().split("=")[1]
					.split(";")[0];
			/**
			 * <NewDataSet>
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
			 * </NewDataSet>
			 */
			List<Material> dt;
			try {
				dt = XMLAnalyze.getNewDataSet(rb, (new Material())
						.getClass().getName());
				if (dt.size() > 1) {
					Machine.getInstance().nextdo = String.format(
							"����!!!�ڲ����з���, һ�����ϱ����,վλ[{0}]���ڶ������", msg);
					return false;
				}

				if (dt.size() < 1) {
					Machine.getInstance().nextdo = String.format(
							"����!!! վλ<{0}>�����ڱ�����Ϣ", msg);
					return false;
				}

				// �ж����ϱ������Ƿ��Ѿ�����
				if (!dt.get(0).getCDATA()
						.equalsIgnoreCase(String.valueOf(ColParameter.�Ѿ�����)))
					Machine.getInstance().nextdo = "����!!! �����ϻ�û�б���,���ܽ��л���";
				// �ȶ��Ϻ�
				if (!dt.get(0).getKPNUMBER().equalsIgnoreCase(arr�������к�[0])) {
					Machine.getInstance().nextdo = "����!!! ����,��������Ϣ������λ�ò���!";
					return false;
				}
				// //�ȶԳ���
				if (!dt.get(0).getVENDERCODE().equalsIgnoreCase(arr�������к�[1])){
					Machine.getInstance().nextdo = "����!!! ���ϵ��������ϴ����ϵĳ��̲�һ��!";
					return false;
				}
				 //�ȶ�datecode
				 if (!dt.get(0).getDATECODE().equalsIgnoreCase(arr�������к�[2])){
						Machine.getInstance().nextdo = "����!!! ���ϵ��������ϴ����ϵ�DATECODE��һ��!";
						return false;
				 }
				    
				 //�ȶ�����
				 if (!dt.get(0).getLOTID().equalsIgnoreCase(arr�������к�[3])){
						Machine.getInstance().nextdo = "����!!! ���ϵ��������ϴ����ϵ����β�һ��!";
						return false;
				 }
				 
			} catch (XMLException e) {
				e.printStackTrace();
				Machine.getInstance().nextdo = e.toString();
				return false;
			}
			 mst.clear();
			 mst.put("USERID", Machine.getInstance().user + "-" + Machine.getInstance().mlist.USERID);
			 mst.put("MASTERID",
			 Machine.getInstance().masterID.split(" ")[0]);
			 mst.put("WOID", Machine.getInstance().masterID.split(" ")[1]);
			 mst.put("PCBSIDE", Machine.getInstance().�������);
			 mst.put("MACHINEID",dt.get(0).getMACHINEID());//this.���ϻ�̨��);
			 mst.put("STATIONID",msg);
			 mst.put("FEEDERID", "NA");
			 mst.put("LOTID", arr�������к�[3]);
			 mst.put("KPNUMBER", arr�������к�[0]);
			 mst.put("DATA",ColParameter.changekp.toUpperCase());
			 mst.put("VENDERCODE", arr�������к�[1]);
			 mst.put("LOTQTY", arr�������к�[4]);
			 mst.put("MODELNAME",Machine.getInstance().����Model);
			 mst.put("DATECODE", arr�������к�[2]);
			 mst.put("KP_SN", Machine.getInstance().Trsn);
		     String Djson = JsonAnalyze.Jsoncreat(mst);
		     mst.clear();
		     mst.put("distring",Djson);
		     mst.put("ROWID", dt.get(0).getROWID());
		     mst.put("cdata", "2");
			 WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
			 WEB.setMethod("InsertSmtKpNormalLog");
			 Machine.getInstance().nextdo=(String)WEB.WebServices(mst).toString().split("=")[1]
						.split(";")[0];
			 if (!Machine.getInstance().nextdo.equalsIgnoreCase("OK"))
			     return false;
			
			 return true;
		}
		return false;
	}
}
