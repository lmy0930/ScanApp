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
					Machine.getInstance().nextdo = "请刷主管权限";
				} else {                        
                        String Err=null;
					    if ((Err=CheckSmtIO())!=null)
                        {                         
                            
                            Machine.getInstance().nextdo=Err+"\n请刷初始化指令";
                            //PlayFailSound();
                            FileAnalyze.WriteINI("1");
                            return;
                        }
                        else
                        {
                        }
					Machine.cmand_status = 3;
					Machine.getInstance().nextdo = "请刷料盘序列号";
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
		            Err = "错误!!  相同的备料序列号存在多笔";
		            return Err;
		        }
		        if (_dt.size() < 1)
		        {
		            Err = "错误!!  刷入的工单还没有备料,不能上线";
		            return Err;
		        }
		        if (_dt.get(0).getSTATUS().equalsIgnoreCase("0"))
		        {
		            Err = "错误!! 工单正在备料中,请在备料完成后再做上线";
		            return Err;
		        }
			WEB.setMethod("GetSmtIOMachineIdStatus_ForMobile");
			mst.clear();
			mst.put("machineId", _dt.get(0).getMACHINEID());
			mst.put("status", String.valueOf(ColParameter.已换线));
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
	    		mst.put("status", String.valueOf( ColParameter.下线));
	    	    WEB.WebServices(mst);
	            Err ="工单号:["+_dt.get(0).getWOID()+"]在机器:["+_dt.get(0).getMASTERID()+"]上被踢下线";
	    		mst.clear();
	    		mst.put("masterId",Machine.getInstance().masterID.split(" ")[0]);
	    		mst.put("woId",  Machine.getInstance().masterID.split(" ")[1]);
	    		mst.put("status", String.valueOf( ColParameter.正在换线));
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
