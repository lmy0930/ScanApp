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
		public void handleMessage(Message m) {
			super.handleMessage(m);
			if (m.what == 0x11) {
				Machine.getInstance().execute();
			}
			else if (m.what == 0x12) {
				Machine.getInstance().execute();
			}
			else if (m.what == 0x13) {
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
					Machine.cmand_bstatus=0;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "��ˢ����Ȩ��";
				} else {    
					    Machine.getInstance().nextdo = "����Ȩ��OK";
                        String Err=null;
					    if ((Err=CheckSmtIO())!=null)
                        {                         
                            
                            Machine.getInstance().nextdo=Err;
                            //PlayFailSound();
                            FileAnalyze.WriteINI("1");
                        }
                        else
                        {
        					Machine.cmand_status = 3;
        					//Machine.getInstance().nextdo += "\n��ˢ�������к�";
                        }
				}
				Message m = handler.obtainMessage();
				m.what = 0x11;
				handler.sendMessage(m);
				
			}
		}).start();
	}
	
	public String CheckSmtIO()
    {
		String Err =null;
		if ((Integer.parseInt(Machine.getInstance().clist.get(0).getSTATUS())== ColParameter.�������)||
				(Integer.parseInt(Machine.getInstance().clist.get(0).getSTATUS())== ColParameter.���ڻ���)) 
            {
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
		    		Err="��ˢ�뻻�� ����ָ��\n��ˢ�뱸�ϱ����к�,��ˢ�Ϻ�";
		    		Machine.getInstance().nextdo=Err;
		    		//Message m = handler.obtainMessage();
		    		Message m = handler.obtainMessage();
		    		m.what = 0x12;
					handler.sendMessage(m);
					Err=null;
		        }
			} catch (XMLException e) {
				e.printStackTrace();
				return e.toString();
			}catch (Exception e) {
				return e.toString();
			}
            }
            else
            {
                if (Integer.parseInt(Machine.getInstance().clist.get(0).getSTATUS())== ColParameter.���ڱ���)
                {
                    
                	Err="��ǰ�ı��ϱ��ֱ߲߱���δ���..\n���ȱ���...";
                    FileAnalyze.WriteINI("1");
                }
                else
                    if (Integer.parseInt(Machine.getInstance().clist.get(0).getSTATUS())== ColParameter.����)
                    {
                    	Err="��ǰ�ı��ϱ��Ѿ�����..\n��ȷ�ϱ��ϱ�...";
                        FileAnalyze.WriteINI("1");
                    }
                    else
                    {
                    	Err="��ǰ�ı��ϱ��Ѿ�������..\n��ˢ��ҵָ��";
                        FileAnalyze.WriteINI("1");
                    }
            }
		return Err;
    }

}
