package com.fx.scanapp.CHG_CLS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.CommonTask.PublicMethods;
import com.fx.scanapp.DateType.ColParameter;
import com.fx.scanapp.DateType.Condition;
import com.fx.scanapp.DateType.Material;
import com.fx.scanapp.DateType.NewDataSet;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;
import com.fx.scanapp.fileAnalyze.XMLException;

public class CommandGetMaterialPreparation extends TaskNode {
	public List<String> stationno;
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0x11) {
				Machine.getInstance().execute();
			}
			if (msg.what == 0x12) {
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
							Machine.cmand_bstatus = Machine.cmand_status;
							Machine.cmand_status = 2;
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
									Machine.cmand_bstatus = Machine.cmand_status;
									Machine.cmand_status = 2;
								} else {
									//����վ����Ϣ����ȡ��վ������Ϣ
									Log.v("WEB","===="+Machine.getInstance().SmtKpMaterialPreparation.size());
									Machine.getInstance().stationno = getStationno(Machine
											.getInstance().SmtKpMaterialPreparation);
									String Err=null;
									if((Err=CheckSmtIO())!= null){
										Machine.getInstance().nextdo = Err;
										// PlayFailSound();
										FileAnalyze.WriteINI("1");
										Machine.cmand_bstatus = Machine.cmand_status;
										Machine.cmand_status = 2;
										//������Ϣ
										Message msg = handler.obtainMessage();
										msg.what = 0x11;
										handler.sendMessage(msg);
										return;
									}
									
								}
							} 
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
									Machine.getInstance().nextdo = "ˢ���ϺŴ���\n�밴����վ��˳��ˢ��.."+"��ˢ��"+Machine.getInstance().stationno.get(0)+"վ������";;
									// PlayFailSound();
									FileAnalyze.WriteINI("1");
									Machine.cmand_bstatus = Machine.cmand_status;
									Machine.cmand_status = 2;
								} else {
									
									
									
									Machine.getInstance().material = Material;
									Machine.getInstance().Trsn = msg;
									Machine.getInstance().nextdo = "�Ϻ��Ѿ���¼:"
											+ Material.split("\\|")[0]
											+ "��ˢ��վ���";
									Machine.cmand_status = 4;
									String kk="";
									for(int k=0;k<Machine.getInstance().stationno.size();k++){
										kk+=Machine.getInstance().stationno.get(k).toString();
									};
									Log.v("WEB1","lalallalalala"+kk );
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
		String dt="";
		String rb="";
		
		try {
			rb=WEB.WebServices(map).toString();//ԭ����������Ϣ�д��ڶ��=��
			Log.v("WEB","back_legth======XXXX"+rb.length());
			int counts=rb.split("=").length;
			for(int j=1;j<counts;j++)
			{
				dt+=rb.split("=")[j];
			}
			Log.v("WEB","back_count======XXXX"+counts);
			dt = dt.split(";")[0];
			Log.v("WEB","dt======XXXX"+dt);
			Log.v("WEB","dt_legth======XXXX"+dt.length());
		} catch (Exception e) {
			return null;
		}
		Log.v("WEB","======XXXX"+"startxml");
		try {
			List<NewDataSet> material = XMLAnalyze.getNewDataSet(dt,
					(new NewDataSet()).getClass().getName());
			Log.v("WEB","======XXXX"+material.size());
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
    
	public String CheckSmtIO() //�޸Ļ��ཫ��һ���ϱ������� michael 20130403
    {
        String Err=null;
        HashMap<String, String> mst = new HashMap<String, String>();
        mst.put("MASTERID", Machine.getInstance().masterID.split(" ")[0]);
        mst.put("WOID", Machine.getInstance().masterID.split(" ")[1]);
		String str = JsonAnalyze.Jsoncreat(mst);
		mst.clear();
	    mst.put("Json",str);
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
		WEB.setMethod("GetSmtIO_ForMobile");
		String rb=WEB.WebServices(mst).toString().split("=")[1].split(";")[0];
		List<Condition> _dt;
		try {
			_dt = XMLAnalyze.getNewDataSet(rb, (new Condition()).getClass().getName().toString());
			WEB.setMethod("GetSmtIOMachineIdStatus_ForMobile");
			mst.clear();
			mst.put("machineId", _dt.get(0).getMACHINEID());
			mst.put("status", String.valueOf(ColParameter.�ѻ���));
			str=JsonAnalyze.Jsoncreat(mst);
			mst.clear();
		    mst.put("Json",str);
		    rb=WEB.WebServices(mst).toString().split("=")[1].split(";")[0];
		    _dt=XMLAnalyze.getNewDataSet(rb, (new Condition()).getClass().getName().toString());
	        if (_dt.size()> 0)
	        {
	            
	        	WEB.setMethod("EditSmtIOStatus");
	    		mst.clear();
	    		mst.put("masterId", _dt.get(0).getMASTERID());
	    		mst.put("woId", _dt.get(0).getWOID());
	    		mst.put("status", String.valueOf( ColParameter.����));
	    	    WEB.WebServices(mst);
	            Err ="������:["+_dt.get(0).getWOID()+"]�ڻ���:["+_dt.get(0).getMASTERID()+"]�ϱ�������";
	    		mst.clear();
	    		mst.put("masterId",Machine.getInstance().masterID.split(" ")[0]);
	    		mst.put("woId",  Machine.getInstance().masterID.split(" ")[1]);
	    		mst.put("status", String.valueOf( ColParameter.���ڻ���));
	    		WEB.WebServices(mst);
	    		Machine.getInstance().nextdo=Err;
	    		Message msg = handler.obtainMessage();
				msg.what = 0x12;
				handler.sendMessage(msg);
				Err=null;
	        }
		} catch (XMLException e) {
			e.printStackTrace();
			return e.toString();
		}
        //<Table> <MASTERID>S021488</MASTERID> <WOID>701001717</WOID> <MACHINEID>060101SL</MACHINEID> </Table>
        return Err;
    }
}
