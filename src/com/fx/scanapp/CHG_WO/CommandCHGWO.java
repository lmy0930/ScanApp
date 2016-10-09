package com.fx.scanapp.CHG_WO;

import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.DateType.ColParameter;
import com.fx.scanapp.DateType.Condition;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;
import com.fx.scanapp.fileAnalyze.XMLException;

public class CommandCHGWO extends TaskNode {
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

	@Override
	public void doTask() {
		Machine.getInstance().inputmsg = msg;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if (FileAnalyze.ReadINI()) {
					Machine.cmand_bstatus=3;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "��ˢ����Ȩ��";
				} else {                        
                        String Err=null;
					    if ((Err=CheckSmtIO())!=null)
                        {                         
                            
                            Machine.getInstance().nextdo=Err+"\n��ˢ��ʼ��ָ��";
                            //PlayFailSound();
                            FileAnalyze.WriteINI("1");
                            return;
                        }
                        else
                        {
                        }
					Machine.cmand_status = 3;
					Machine.getInstance().nextdo = "��ˢ�������к�";
				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
				
			}
		}).start();
	}
	
	public String CheckSmtIO()
    {
        String Err =null;
        String msg = "";
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
			 if (_dt.size() > 1)
		        {
		            Err = "����!!  ��ͬ�ı������кŴ��ڶ��";
		            return Err;
		        }
		        if (_dt.size() < 1)
		        {
		            Err = "����!!  ˢ��Ĺ�����û�б���,��������";
		            return Err;
		        }
		        if (_dt.get(0).getSTATUS().equalsIgnoreCase("0"))
		        {
		            Err = "����!! �������ڱ�����,���ڱ�����ɺ���������";
		            return Err;
		        }
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
	    		Message m = handler.obtainMessage();
				m.what = 0x12;
				handler.sendMessage(m);
				Err=null;
	        }
		} catch (XMLException e) {
			e.printStackTrace();
			return e.toString();
		}
		return "err";
    }

}
